package chaos.common;

import chaos.board.World;
import chaos.common.growth.GooeyBlob;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Event;
import chaos.util.EventListener;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;
import junit.framework.Assert;

/**
 * Tests basic functionality that all trees should satisfy.
 *
 * @author Sean A. Irvine
 */
public abstract class AbstractTreeTest extends AbstractActorTest {

  public void testReincarnation() {
    // Trees cannot be reincarnated
    final Castable c = getCastable();
    if (c instanceof Monster) {
      assertEquals(null, ((Monster) c).reincarnation());
    }
  }

  @Override
  public void testInstanceOf() {
    assertTrue(mCastable instanceof Tree);
  }

  private static class MyBool {
    boolean mCast = false;
    boolean mWeapon = false;
    boolean mRedraw = false;
  }

  private static class MyListener implements EventListener {
    final MyBool mState = new MyBool();
    @Override
    public void update(final Event e) {
      if (e instanceof WeaponEffectEvent) {
        Assert.assertEquals(WeaponEffectType.TREE_CAST_EVENT, ((WeaponEffectEvent) e).getEventType());
        Assert.assertFalse(mState.mWeapon);
        mState.mWeapon = true;
      } else if (e instanceof CellEffectEvent) {
        if (((CellEffectEvent) e).getEventType() == CellEffectType.MONSTER_CAST_EVENT) {
          Assert.assertEquals(1, ((CellEffectEvent) e).getCellNumber());
          Assert.assertFalse(mState.mCast);
          mState.mCast = true;
        } else {
          Assert.assertEquals(CellEffectType.REDRAW_CELL, ((CellEffectEvent) e).getEventType());
          Assert.assertFalse(mState.mRedraw);
          mState.mRedraw = true;
        }
      }
    }
    MyBool getState() {
      return mState;
    }
  }

  public void testTreeCasting() {
    final Castable c = getCastable();
    assertEquals(Castable.CAST_GROWTH | Castable.CAST_EMPTY | Castable.CAST_LOS, c.getCastFlags());
    final World world = new World(2, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    world.getCell(0).push(w);
    final MyListener listen = new MyListener();
    final MyBool state = listen.getState();
    world.register(listen);
    c.cast(world, w, world.getCell(1), world.getCell(0));
    assertTrue(state.mWeapon);
    assertTrue(state.mCast);
    assertTrue(state.mRedraw);
    world.deregister(listen);
    // check that null parameters do not cause an exception
    c.cast(null, w, world.getCell(1), world.getCell(0));
    c.cast(world, null, world.getCell(1), world.getCell(0));
    c.cast(world, w, null, world.getCell(0));
    c.cast(world, w, world.getCell(1), null);
  }

  public void testAgainstSource() {
    AbstractMonsterTest.checkAgainstSource(getActor());
  }

  public void testCastOnGrowth() {
    final Castable c = getCastable();
    final World world = new World(2, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    world.getCell(0).push(w);
    final Lion lion = new Lion();
    world.getCell(1).push(lion);
    final GooeyBlob gb = new GooeyBlob();
    world.getCell(1).push(gb);
    c.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(c, world.actor(1));
    world.getCell(1).reinstate();
    assertNull(world.actor(1));
  }
}
