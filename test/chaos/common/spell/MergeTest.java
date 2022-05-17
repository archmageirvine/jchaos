package chaos.common.spell;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.monster.Horse;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.EventListener;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class MergeTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new Merge();
  }

  private boolean mCast = false;

  private void setCast() {
    mCast = true;
  }

  private boolean getCast() {
    return mCast;
  }

  private boolean mWarp = false;

  private void setWarp() {
    mWarp = true;
  }

  private boolean getWarp() {
    return mWarp;
  }

  private int mRedraw = 0;

  private void bumpRedraw() {
    ++mRedraw;
  }

  private int getRedraw() {
    return mRedraw;
  }

  public void testCast() {
    final Castable x = new Merge();
    assertEquals(Castable.CAST_LIVING | Castable.CAST_NOEXPOSEDWIZARD | Castable.CAST_LOS, x.getCastFlags());
    assertEquals(9, x.getCastRange());
    final Wizard1 w = new Wizard1();
    final World world = new World(1, 2);
    final EventListener listen = e -> {
      if (e instanceof CellEffectEvent) {
        final CellEffectEvent ce = (CellEffectEvent) e;
        if (ce.getEventType() == CellEffectType.REDRAW_CELL) {
          bumpRedraw();
        } else if (ce.getEventType() == CellEffectType.WARP_OUT) {
          assertFalse(getCast());
          setCast();
        } else if (ce.getEventType() == CellEffectType.WARP_IN) {
          assertFalse(getWarp());
          setWarp();
        }
      }
    };
    world.getCell(0).push(w);
    w.setState(State.ACTIVE);
    final Lion l = new Lion();
    world.getCell(1).push(l);
    world.register(listen);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(State.ACTIVE, w.getState());
    assertEquals(w.getDefault(Attribute.INTELLIGENCE), w.get(Attribute.INTELLIGENCE));
    assertEquals(w.getDefault(Attribute.LIFE) + l.getDefault(Attribute.LIFE), w.get(Attribute.LIFE));
    assertEquals(2, getRedraw());
    assertTrue(getCast());
    assertTrue(getWarp());
    world.deregister(listen);
    x.cast(world, w, world.getCell(0), null);
    x.cast(world, w, null, world.getCell(0));
    x.cast(world, null, world.getCell(0), world.getCell(0));
    x.cast(null, w, world.getCell(0), world.getCell(0));
  }

  public void testDontMergeWithSelfMount() {
    final Merge x = new Merge();
    final Wizard1 w = new Wizard1();
    final World world = new World(1, 2);
    w.setState(State.ACTIVE);
    final Horse l = new Horse();
    l.setMount(w);
    world.getCell(0).push(l);
    final HashSet<Cell> cells = new HashSet<>();
    cells.add(world.getCell(0));
    x.filter(cells, w, world);
    assertEquals(0, cells.size());
  }

  public void testDontMergeWithOtherMount() {
    final Merge x = new Merge();
    final Wizard1 w = new Wizard1();
    final Wizard1 w1 = new Wizard1();
    final World world = new World(1, 2);
    w.setState(State.ACTIVE);
    w1.setState(State.ACTIVE);
    final Horse l = new Horse();
    l.setMount(w1);
    world.getCell(0).push(w);
    world.getCell(1).push(l);
    final HashSet<Cell> cells = new HashSet<>();
    cells.add(world.getCell(1));
    x.filter(cells, w, world);
    assertEquals(1, cells.size());
  }
}
