package chaos.common.free;

import junit.framework.Assert;
import chaos.board.World;
import chaos.common.wizard.Wizard;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Event;
import chaos.util.EventListener;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public abstract class AbstractFreePowerUpTest extends AbstractFreeCastableTest {

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
        } else if (ce.getEventType() == CellEffectType.POWERUP) {
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
    final FreePowerUp x = (FreePowerUp) getCastable();
    final Wizard w = new Wizard1();
    final World world = new World(1, 1);
    world.getWizardManager().setWizard(1, w);
    final MyListener listen = new MyListener();
    final MyBool state = listen.getState();
    world.register(listen);
    w.set(x.getPowerUpType(), 42);
    x.cast(world, w, world.getCell(0));
    final int q = w.get(x.getPowerUpType());
    if (x.cumulative()) {
      assertEquals(42 + x.getPowerUpCount(), q);
    } else {
      assertEquals(x.getPowerUpCount(), q);
    }
    assertTrue(q > 0);
    assertTrue(state.mRedraw);
    assertTrue(state.mCast);
    world.deregister(listen);
    x.cast(world, w, null);
    x.cast(world, null, world.getCell(0));
    x.cast(null, w, world.getCell(0));
  }

}
