package chaos.common.free;

import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Castable;
import chaos.common.monster.Horse;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Event;
import chaos.util.EventListener;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;
import junit.framework.Assert;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public abstract class AbstractDecrementTest extends AbstractCastableTest {

  private static class MyBool {
    boolean mCast = false;
    boolean mRedraw = false;
    boolean mWeapon = false;
  }

  private static class MyListener implements EventListener {
    final MyBool mState = new MyBool();
    @Override
    public void update(final Event e) {
      if (e instanceof CellEffectEvent) {
        final CellEffectType ce = ((CellEffectEvent) e).getEventType();
        if (ce == CellEffectType.REDRAW_CELL) {
          Assert.assertFalse(mState.mRedraw);
          mState.mRedraw = true;
        } else if (ce == CellEffectType.SHIELD_DESTROYED) {
          Assert.assertFalse(mState.mCast);
          mState.mCast = true;
        } else {
          Assert.fail();
        }
      } else if (e instanceof WeaponEffectEvent) {
        Assert.assertEquals(WeaponEffectType.BALL, ((WeaponEffectEvent) e).getEventType());
        Assert.assertFalse(mState.mWeapon);
        mState.mWeapon = true;
      }
    }
    MyBool getState() {
      return mState;
    }
  }

  public void testCastEffects() {
    final Castable x = getCastable();
    final Wizard1 w = new Wizard1();
    final World world = new World(2, 1);
    final MyListener listen = new MyListener();
    final MyBool state = listen.getState();
    world.getCell(0).push(w);
    world.register(listen);
    try {
      world.getCell(1).push(new Horse());
      x.cast(world, w, world.getCell(1), world.getCell(0));
      assertTrue(state.mRedraw);
      assertTrue(state.mCast);
      if (x.getCastRange() < 10) {
        assertTrue(state.mWeapon);
      } else {
        assertFalse(state.mWeapon);
      }
    } finally {
    world.deregister(listen);
    }
    x.cast(world, w, world.getCell(0), null);
    x.cast(world, w, null, world.getCell(0));
    x.cast(world, null, world.getCell(0), world.getCell(0));
    x.cast(null, w, world.getCell(0), world.getCell(0));
  }
}
