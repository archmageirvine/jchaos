package chaos.common.free;

import chaos.board.World;
import chaos.common.AbstractFreeDecrement;
import chaos.common.Attribute;
import chaos.common.State;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectType;
import chaos.util.Event;
import chaos.util.EventListener;
import chaos.util.PolycellEffectEvent;
import chaos.util.PolyshieldDestroyEvent;
import junit.framework.Assert;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public abstract class AbstractFreeDecrementTest extends AbstractFreeCastableTest {

  private static class MyBool {
    boolean mCast = false;
    boolean mWeapon = false;
  }

  private static class MyListener implements EventListener {
    final MyBool mState = new MyBool();

    @Override
    public void update(final Event e) {
      if (e instanceof PolyshieldDestroyEvent) {
        Assert.assertFalse(mState.mWeapon);
        mState.mWeapon = true;
      } else if (e instanceof PolycellEffectEvent) {
        Assert.assertEquals(CellEffectType.REDRAW_CELL, ((PolycellEffectEvent) e).getEventType());
        Assert.assertFalse(mState.mCast);
        mState.mCast = true;
      }
    }

    MyBool getState() {
      return mState;
    }
  }

  public void testAFDEffects() {
    final AbstractFreeDecrement a = (AbstractFreeDecrement) getCastable();
    assertEquals(0, a.getCastFlags());
    final World world = new World(1, 5);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    a.cast(null, null, null);
    a.cast(world, null, null);
    a.cast(null, w, null);
    a.cast(world, w, null);
    final MyListener listen = new MyListener();
    final MyBool state = listen.getState();
    world.register(listen);
    try {
      final Lion l = new Lion();
      final Attribute at = a.attribute();
      l.setOwner(2);
      l.increment(at, a.decrement());
      world.getCell(0).push(l);
      a.cast(world, w, null);
      assertEquals(l.getDefault(at), l.get(at));
      assertEquals(State.ACTIVE, l.getState());
      assertTrue(state.mWeapon);
      assertTrue(state.mCast);
    } finally {
      world.deregister(listen);
    }
  }
}
