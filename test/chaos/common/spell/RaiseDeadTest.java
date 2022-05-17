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
public class RaiseDeadTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new RaiseDead();
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
    final Castable x = new RaiseDead();
    assertEquals(Castable.CAST_DEAD | Castable.CAST_LOS, x.getCastFlags());
    assertEquals(8, x.getCastRange());
    final Wizard1 w = new Wizard1();
    final World world = new World(1, 1);
    final EventListener listen = e -> {
      if (e instanceof CellEffectEvent) {
        final CellEffectEvent ce = (CellEffectEvent) e;
        if (ce.getEventType() == CellEffectType.REDRAW_CELL) {
          assertFalse(getRedraw());
          setRedraw();
        } else if (ce.getEventType() == CellEffectType.RAISE_DEAD) {
          assertFalse(getCast());
          setCast();
        }
      }
    };
    world.getCell(0).push(w);
    w.setState(State.DEAD);
    world.register(listen);
    w.set(Attribute.INTELLIGENCE, 42);
    w.set(Attribute.AGILITY, 1);
    x.cast(world, w, world.getCell(0), world.getCell(0));
    assertEquals(State.ACTIVE, w.getState());
    assertEquals(w.getDefault(Attribute.INTELLIGENCE), w.get(Attribute.INTELLIGENCE));
    assertEquals(w.getDefault(Attribute.AGILITY), w.get(Attribute.AGILITY));
    assertEquals(20, w.getScore());
    assertTrue(getRedraw());
    assertTrue(getCast());
    world.deregister(listen);
    x.cast(world, w, world.getCell(0), null);
    x.cast(world, w, null, world.getCell(0));
    x.cast(world, null, world.getCell(0), world.getCell(0));
    x.cast(null, w, world.getCell(0), world.getCell(0));
  }

  public void testFilter() {
    final RaiseDead x = new RaiseDead();
    final HashSet<Cell> t = new HashSet<>();
    x.filter(t, null, null);
    assertEquals(0, t.size());
    final Cell cn = new Cell(23);
    t.add(cn);
    x.filter(t, null, null);
    assertEquals(1, t.size());
    assertTrue(t.contains(cn));
    final Cell c = new Cell(0);
    final Lion l = new Lion();
    l.setState(State.DEAD);
    c.push(l);
    t.add(c);
    x.filter(t, null, null);
    assertEquals(1, t.size());
    assertTrue(t.contains(c));
    final Cell c2 = new Cell(1);
    final Lion l2 = new Lion();
    l2.setState(State.DEAD);
    c2.push(l2);
    t.add(c2);
    x.filter(t, null, null);
    assertEquals(2, t.size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
    t.add(new Cell(42));
    x.filter(t, null, null);
    assertEquals(2, t.size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
    final Horse h = new Horse();
    h.setState(State.DEAD);
    final Cell c3 = new Cell(3);
    c3.push(h);
    t.add(c3);
    x.filter(t, null, null);
    assertEquals(2, t.size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
  }
}
