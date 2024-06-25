package chaos.common;

import chaos.board.World;
import chaos.common.inanimate.Generator;
import chaos.common.inanimate.Roper;
import chaos.common.monster.Bolter;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Event;
import chaos.util.EventListener;
import chaos.util.TextEvent;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;
import junit.framework.Assert;

/**
 * Tests this generator.
 * @author Sean A. Irvine
 */
public class DummyGeneratorTest extends AbstractGeneratorTest {

  @Override
  public Castable getCastable() {
    return new Generator();
  }

  public void testValues() {
    final Generator g = new Generator();
    assertEquals(63, g.get(Attribute.LIFE));
    assertEquals(63, g.get(Attribute.LIFE_RECOVERY));
    assertEquals(100, g.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(100, g.get(Attribute.MAGICAL_RESISTANCE_RECOVERY));
    assertEquals(14, g.getCastRange());
    assertEquals(~0, g.getLosMask());
    assertEquals(10, g.getBonus());
    assertEquals(0, g.getDefaultWeight());
    assertEquals(Castable.CAST_DEAD | Castable.CAST_EMPTY | Castable.CAST_GROWTH | Castable.CAST_LOS, g.getCastFlags());
  }

  private boolean mWeapon = false;

  private void setWeapon() {
    mWeapon = true;
  }

  private boolean getWeapon() {
    return mWeapon;
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

  public void testGeneratorCasting() {
    final Castable c = getCastable();
    final World world = new World(2, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    world.getCell(0).push(w);
    final EventListener listen = e -> {
      if (e instanceof WeaponEffectEvent) {
        assertEquals(WeaponEffectType.STONE_CAST_EVENT, ((WeaponEffectEvent) e).getEventType());
        assertFalse(getWeapon());
        setWeapon();
      } else if (e instanceof CellEffectEvent) {
        if (((CellEffectEvent) e).getEventType() == CellEffectType.MONSTER_CAST_EVENT) {
          assertEquals(1, ((CellEffectEvent) e).getCellNumber());
          assertFalse(getCast());
          setCast();
        } else {
          assertEquals(CellEffectType.REDRAW_CELL, ((CellEffectEvent) e).getEventType());
          assertFalse(getRedraw());
          setRedraw();
        }
      }
    };
    world.register(listen);
    c.cast(world, w, world.getCell(1), world.getCell(0));
    assertTrue(getWeapon());
    assertTrue(getCast());
    assertTrue(getRedraw());
    world.deregister(listen);
    // check that null parameters do not cause an exception
    c.cast(null, w, world.getCell(1), world.getCell(0));
    c.cast(world, null, world.getCell(1), world.getCell(0));
    c.cast(world, w, null, world.getCell(0));
    c.cast(world, w, world.getCell(1), null);
  }

  private static class MyEventListener implements EventListener {
    @Override
    public void update(final Event e) {
      if (e instanceof TextEvent) {
        Assert.assertTrue(e.toString().startsWith("Generator:"));
      }
    }
  }

  public void testGenerate() {
    final World world = new World(3, 3);
    final Generator g = new Generator();
    g.setOwner(2);
    final EventListener listen = new MyEventListener();
    world.register(listen);
    world.getCell(4).push(g);
    for (int i = 0; i < 8; ++i) {
      g.generate(world, 4);
    }
    world.deregister(listen);
    for (int i = 0; i < world.size(); ++i) {
      final Actor a = world.actor(i);
      if (i != 4) {
        assertTrue(a instanceof Monster);
        if (!(a instanceof Roper) && !(a instanceof Bolter)) {
          assertTrue(a.getName(), a.get(Attribute.MOVEMENT) > 0);
        }
      } else {
        assertEquals(g, a);
      }
    }
    g.generate(world, 4);
    for (int i = 0; i < world.size(); ++i) {
      final Actor a = world.actor(i);
      if (i != 4) {
        assertTrue(a instanceof Monster);
        if (!(a instanceof Roper) && !(a instanceof Bolter)) {
          assertTrue(a.getName(), a.get(Attribute.MOVEMENT) > 0);
        }
      } else {
        assertEquals(g, a);
      }
    }
  }

  public void testMoveInc() {
    final Generator g = new Generator();
    assertEquals(0, g.get(Attribute.MOVEMENT));
    g.increment(Attribute.MOVEMENT, 1);
    assertEquals(0, g.get(Attribute.MOVEMENT));
  }
}
