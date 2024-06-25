package chaos.common.spell;

import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Monster;
import chaos.common.State;
import chaos.common.beam.PlasmaBeam;
import chaos.common.inanimate.WeakWall;
import chaos.common.monster.Horse;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.EventListener;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class LightningTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new Lightning();
  }

  private boolean mCast = false;

  protected void setCast() {
    mCast = true;
  }

  protected boolean getCast() {
    return mCast;
  }

  private boolean mRedraw = false;

  protected void setRedraw() {
    mRedraw = true;
  }

  protected boolean getRedraw() {
    return mRedraw;
  }

  protected EventListener getListener() {
    return e -> {
      if (e instanceof CellEffectEvent && ((CellEffectEvent) e).getEventType() == CellEffectType.REDRAW_CELL) {
        assertFalse(getRedraw());
        setRedraw();
      } else if (e instanceof WeaponEffectEvent && ((WeaponEffectEvent) e).getEventType() == WeaponEffectType.LIGHTNING) {
        assertFalse(getCast());
        setCast();
      }
    };
  }

  public void testFlags() {
    final Castable x = getCastable();
    assertEquals(Castable.CAST_LIVING | Castable.CAST_DEAD | Castable.CAST_INANIMATE | Castable.CAST_LOS | Castable.CAST_GROWTH, x.getCastFlags());
    assertEquals(8, x.getCastRange());
  }

  protected int getDamage() {
    return 30;
  }

  public void testCast() {
    final Castable x = getCastable();
    final Wizard1 w = new Wizard1();
    final World world = new World(2, 1);
    final EventListener listen = getListener();
    world.getCell(0).push(w);
    world.register(listen);
    Monster h = new Horse();
    world.getCell(1).push(h);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(0, h.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(0, h.get(Attribute.LIFE));
    assertEquals(State.DEAD, h.getState());
    assertTrue(getRedraw());
    assertTrue(getCast());
    assertEquals(h, world.actor(1));
    mCast = false;
    mRedraw = false;
    assertEquals(8, w.getScore());
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(13, w.getScore());
    assertEquals(null, world.actor(1));
    mCast = false;
    mRedraw = false;
    h = new Horse();
    h.set(Attribute.LIFE, 16);
    world.getCell(1).pop();
    world.getCell(1).push(h);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(0, h.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(0, h.get(Attribute.LIFE));
    assertEquals(State.DEAD, h.getState());
    assertTrue(getRedraw());
    assertTrue(getCast());
    assertEquals(h, world.actor(1));
    mCast = false;
    mRedraw = false;
    h = new Horse();
    h.set(Attribute.MAGICAL_RESISTANCE, 31);
    world.getCell(1).pop();
    world.getCell(1).push(h);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    if (x instanceof PlasmaBeam) {
      assertEquals(0, h.get(Attribute.MAGICAL_RESISTANCE));
      assertEquals(0, h.get(Attribute.LIFE));
      assertEquals(State.DEAD, h.getState());
    } else {
      assertEquals(1, h.get(Attribute.MAGICAL_RESISTANCE));
      assertEquals(h.getDefault(Attribute.LIFE), h.get(Attribute.LIFE));
      assertEquals(State.ACTIVE, h.getState());
    }
    assertTrue(getRedraw());
    assertTrue(getCast());
    assertEquals(h, world.actor(1));
    mCast = false;
    mRedraw = false;
    final Actor i = new WeakWall();
    i.set(Attribute.MAGICAL_RESISTANCE, 0);
    world.getCell(1).pop();
    world.getCell(1).push(i);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(i.getDefault(Attribute.LIFE) - getDamage(), i.get(Attribute.LIFE));
    assertEquals(0, i.get(Attribute.MAGICAL_RESISTANCE));
    assertTrue(getRedraw());
    assertTrue(getCast());
    assertEquals(i, world.actor(1));
    world.deregister(listen);
    i.set(Attribute.LIFE, 1);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertNull(world.actor(1));
    assertEquals(1, w.getBonusCount());
    assertEquals(1, w.getBonusSelect());
    x.cast(world, w, world.getCell(0), null);
    x.cast(world, w, null, world.getCell(0));
    x.cast(world, null, world.getCell(0), world.getCell(0));
    x.cast(null, w, world.getCell(0), world.getCell(0));
  }
}
