package chaos.common;

import chaos.board.World;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Event;
import chaos.util.EventListener;
import junit.framework.Assert;

/**
 * Superclass for testing shield spells.
 *
 * @author Sean A. Irvine
 */
public abstract class AbstractShieldTest extends AbstractCastableTest {


  private static class MyBool {
    boolean mCast = false;
    boolean mRedraw = false;
  }

  private static class MyListener implements EventListener {
    final MyBool mState = new MyBool();
    @Override
    public void update(final Event e) {
      if (e instanceof CellEffectEvent) {
        final CellEffectEvent ce = (CellEffectEvent) e;
        if (ce.getEventType() == CellEffectType.REDRAW_CELL) {
          Assert.assertFalse(mState.mRedraw);
          mState.mRedraw = true;
        } else if (ce.getEventType() == CellEffectType.SHIELD_GRANTED) {
          Assert.assertFalse(mState.mCast);
          mState.mCast = true;
        }
      }
    }
    MyBool getState() {
      return mState;
    }
  }

  public void testCast() {
    final AbstractShield x = (AbstractShield) getCastable();
    final Wizard1 w = new Wizard1();
    final World world = new World(1, 1);
    final MyListener listen = new MyListener();
    final MyBool state = listen.getState();
    world.getCell(0).push(w);
    world.register(listen);
    final Attribute attr = x.attribute();
    final int max = attr.max();
    w.set(attr, 0);
    x.cast(world, w, world.getCell(0), world.getCell(0));
    assertEquals(x.increment(), w.get(attr));
    assertTrue(state.mRedraw);
    assertTrue(state.mCast);
    world.deregister(listen);
    w.increment(attr, max - w.get(attr) - 1);
    x.cast(world, w, world.getCell(0), world.getCell(0));
    assertEquals(max, w.get(attr));
    final Attribute rec = attr.recovery();
    final int delta = w.getDefault(rec);
    if (rec != null) {
      for (int i = 1; i <= rec.max(); ++i) {
        x.cast(world, w, world.getCell(0), world.getCell(0));
        assertEquals(delta + i, w.get(rec));
      }
      x.cast(world, w, world.getCell(0), world.getCell(0));
      assertEquals(rec.max(), w.get(rec));
    }
    x.cast(world, w, world.getCell(0), null);
    x.cast(world, w, null, world.getCell(0));
    x.cast(world, null, world.getCell(0), world.getCell(0));
    x.cast(null, w, world.getCell(0), world.getCell(0));
  }

}
