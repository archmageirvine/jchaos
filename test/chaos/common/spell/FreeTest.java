package chaos.common.spell;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.monster.EarthElemental;
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
public class FreeTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new Free();
  }

  private boolean mCast = false;

  private void setCast() {
    mCast = true;
  }

  private boolean getCast() {
    return mCast;
  }

  private boolean mRedraw = false;

  private void setRedraw() {
    mRedraw = true;
  }

  private boolean getRedraw() {
    return mRedraw;
  }

  public void testCast() {
    final Castable x = new Free();
    assertEquals(Castable.CAST_LIVING | Castable.CAST_GROWTH | Castable.CAST_INANIMATE | Castable.CAST_NOWIZARDCELL, x.getCastFlags());
    assertEquals(14, x.getCastRange());
    final Wizard1 w = new Wizard1();
    final World world = new World(1, 1);
    final EventListener listen = e -> {
      if (e instanceof CellEffectEvent) {
        final CellEffectEvent ce = (CellEffectEvent) e;
        if (ce.getEventType() == CellEffectType.REDRAW_CELL) {
          assertFalse(getRedraw());
          setRedraw();
        } else if (ce.getEventType() == CellEffectType.OWNER_CHANGE) {
          assertFalse(getCast());
          setCast();
        }
      }
    };
    final Lion l = new Lion();
    l.setOwner(2);
    world.getCell(0).push(l);
    world.register(listen);
    x.cast(world, w, world.getCell(0), world.getCell(0));
    assertEquals(Actor.OWNER_INDEPENDENT, l.getOwner());
    assertEquals(State.ACTIVE, l.getState());
    assertTrue(getRedraw());
    assertTrue(getCast());
    world.deregister(listen);
    x.cast(world, w, world.getCell(0), world.getCell(0));
    assertEquals(State.ASLEEP, l.getState());
    x.cast(world, w, world.getCell(0), null);
    x.cast(world, w, null, world.getCell(0));
    x.cast(world, null, world.getCell(0), world.getCell(0));
    x.cast(null, w, world.getCell(0), world.getCell(0));
  }

  public void testFilter() {
    final World w = new World(2, 1);
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(1);
    final Free x = new Free();
    final HashSet<Cell> t = new HashSet<>();
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    final Cell cn = new Cell(23);
    t.add(cn);
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    final Cell c = new Cell(0);
    final Lion l = new Lion();
    l.setOwner(2);
    c.push(l);
    t.add(c);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertTrue(t.contains(c));
    final Cell c2 = new Cell(1);
    final EarthElemental l2 = new EarthElemental();
    l2.setOwner(2);
    c2.push(l2);
    t.add(c2);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertTrue(t.contains(c2));
  }

  public void testMount() {
    final Castable x = new Free();
    final Wizard1 w = new Wizard1();
    final World world = new World(1, 1);
    final Horse l = new Horse();
    l.setOwner(2);
    w.setOwner(2);
    l.setMount(w);
    world.getCell(0).push(l);
    x.cast(world, w, world.getCell(0), world.getCell(0));
    assertEquals(2, l.getOwner());
    assertEquals(State.ACTIVE, l.getState());
  }
}
