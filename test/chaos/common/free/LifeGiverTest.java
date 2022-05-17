package chaos.common.free;

import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.monster.StoneGiant;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.EventListener;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class LifeGiverTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new LifeGiver();
  }

  private boolean mRedraw = false;

  private void setRedraw() {
    mRedraw = true;
  }

  private boolean getRedraw() {
    return mRedraw;
  }

  private boolean mFailed = false;

  private void setFailed() {
    mFailed = true;
  }

  private boolean getFailed() {
    return mFailed;
  }

  public void test1() {
    final LifeGiver a = new LifeGiver();
    final World world = new World(1, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    a.cast(null, null, null);
    a.cast(world, null, null);
    a.cast(null, w, null);
    a.cast(world, w, null);
    final EventListener listen = e -> {
      if (e instanceof CellEffectEvent) {
        final CellEffectEvent ce = (CellEffectEvent) e;
        if (ce.getEventType() == CellEffectType.REDRAW_CELL) {
          assertFalse(getRedraw());
          setRedraw();
        } else if (ce.getEventType() == CellEffectType.RAISE_DEAD) {
          assertFalse(getFailed());
          setFailed();
        } else if (ce.getEventType() == CellEffectType.SPELL_FAILED) {
          fail("bad event");
        }
      }
    };
    world.getCell(0).register(listen);
    final StoneGiant l = new StoneGiant();
    l.setOwner(10);
    l.set(Attribute.LIFE, 0);
    l.setState(State.DEAD);
    world.getCell(0).push(l);
    mRedraw = false;
    mFailed = false;
    a.cast(world, w, null);
    assertEquals(State.ACTIVE, l.getState());
    assertEquals(w.getOwner(), l.getOwner());
    assertEquals(l.getDefault(Attribute.LIFE), l.get(Attribute.LIFE));
    assertTrue(getRedraw());
    assertTrue(getFailed());
  }

  public void testNone() {
    final LifeGiver a = new LifeGiver();
    final World world = new World(1, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    w.setState(State.ACTIVE);
    final EventListener listen = e -> {
      if (e instanceof CellEffectEvent) {
        final CellEffectEvent ce = (CellEffectEvent) e;
        if (ce.getEventType() == CellEffectType.SPELL_FAILED) {
          assertFalse(getFailed());
          setFailed();
        }
      }
    };
    world.getCell(0).register(listen);
    world.getCell(0).push(w);
    mFailed = false;
    a.cast(world, w, world.getCell(0));
    assertTrue(getFailed());
  }
}
