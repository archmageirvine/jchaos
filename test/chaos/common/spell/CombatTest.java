package chaos.common.spell;

import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Monster;
import chaos.common.monster.Hippocrates;
import chaos.common.monster.Horse;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Event;
import chaos.util.EventListener;
import junit.framework.Assert;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class CombatTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new Combat();
  }

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
    final Castable x = new Combat();
    assertEquals(Castable.CAST_LIVING | Castable.CAST_LOS, x.getCastFlags());
    assertEquals(9, x.getCastRange());
    final Wizard1 w = new Wizard1();
    final World world = new World(2, 1);
    final MyListener listen = new MyListener();
    final MyBool state = listen.getState();
    world.getCell(0).push(w);
    world.register(listen);
    Monster h = new Horse();
    world.getCell(1).push(h);
    for (int i = 15; i <= Attribute.COMBAT.max(); i += 5) {
      state.mCast = false;
      state.mRedraw = false;
      x.cast(world, w, world.getCell(1), world.getCell(0));
      assertEquals(i, h.get(Attribute.COMBAT));
      assertTrue(state.mRedraw);
      assertTrue(state.mCast);
      assertEquals(h, world.actor(1));
      assertEquals(0, h.get(Attribute.COMBAT_RECOVERY));
    }
    world.deregister(listen);
    for (int i = 1; i < Attribute.COMBAT_RECOVERY.max(); ++i) {
      x.cast(world, w, world.getCell(1), world.getCell(0));
      assertEquals(i, h.get(Attribute.COMBAT_RECOVERY));
    }
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(Attribute.COMBAT_RECOVERY.max(), h.get(Attribute.COMBAT_RECOVERY));
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(Attribute.COMBAT_RECOVERY.max(), h.get(Attribute.COMBAT_RECOVERY));
    world.register(listen);
    h = new Hippocrates();
    world.getCell(1).push(h);
    for (int i = 15; i <= Attribute.COMBAT.max(); i += 5) {
      state.mCast = false;
      state.mRedraw = false;
      x.cast(world, w, world.getCell(1), world.getCell(0));
      assertEquals(-i, h.get(Attribute.COMBAT));
      assertTrue(state.mRedraw);
      assertTrue(state.mCast);
      assertEquals(h, world.actor(1));
      assertEquals(0, h.get(Attribute.COMBAT_RECOVERY));
    }
    world.deregister(listen);
    for (int i = 1; i < Attribute.COMBAT_RECOVERY.max(); ++i) {
      x.cast(world, w, world.getCell(1), world.getCell(0));
      assertEquals(i, h.get(Attribute.COMBAT_RECOVERY));
    }
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(Attribute.COMBAT_RECOVERY.max(), h.get(Attribute.COMBAT_RECOVERY));
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(Attribute.COMBAT_RECOVERY.max(), h.get(Attribute.COMBAT_RECOVERY));
    h.set(Attribute.COMBAT, -14);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(-15, h.get(Attribute.COMBAT));
    x.cast(world, w, world.getCell(0), null);
    x.cast(world, w, null, world.getCell(0));
    x.cast(world, null, world.getCell(0), world.getCell(0));
    x.cast(null, w, world.getCell(0), world.getCell(0));
  }
}
