package chaos.common.free;

import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.monster.Horse;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectType;
import irvine.TestUtils;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class AbductionTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Abduction();
  }

  public void test1() {
    final Abduction a = new Abduction();
    final World world = new World(1, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    a.cast(null, null, null);
    a.cast(world, null, null);
    a.cast(null, w, null);
    a.cast(world, w, null);
    final TestListener listen = new TestListener(0, 2, -1, CellEffectType.REDRAW_CELL, CellEffectType.AUDIO, CellEffectType.OWNER_CHANGE, CellEffectType.SPELL_FAILED);
    world.getCell(0).register(listen);
    final Lion l = new Lion();
    l.setOwner(1);
    world.getCell(0).push(l);
    for (int i = 0; i < 200 && l.getOwner() == 1; ++i) {
      a.cast(world, w, null);
      listen.checkAndReset();
    }
    if (l.getOwner() == 1) {
      fail("abduction did not work");
    }
    l.setOwner(1);
    l.set(Attribute.INTELLIGENCE, 0);
    assertEquals(0, l.get(Attribute.INTELLIGENCE));
    a.cast(world, w, null);
    listen.checkAndReset();
    assertEquals(3, l.getOwner());
    l.set(Attribute.INTELLIGENCE, 80); // closer to the midpoint than 50 because of 3 attempts
    world.getCell(0).deregister(listen);
    int c = 0;
    for (int i = 0; i < 500; ++i) {
      l.setOwner(1);
      a.cast(world, w, null);
      if (l.getOwner() == 3) {
        ++c;
      }
    }
    assertTrue(c > 100);
    assertTrue(c < 400);
  }

  public void test2() {
    final Abduction a = new Abduction();
    final World world = new World(1, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    final TestListener listen = new TestListener(CellEffectType.SPELL_FAILED, CellEffectType.REDRAW_CELL);
    world.getCell(0).register(listen);
    a.cast(world, w, null);
    a.cast(world, w, null);
    a.cast(world, w, null);
    a.cast(world, w, null);
    final Lion l = new Lion();
    world.getCell(0).push(l);
    l.set(Attribute.INTELLIGENCE, Attribute.INTELLIGENCE.max());
    int c = 0;
    for (int i = 0; i < 500; ++i) {
      l.setOwner(1);
      a.cast(world, w, null);
      if (l.getOwner() == 3) {
        ++c;
      } else {
        listen.checkAndReset();
      }
    }
    assertTrue(c < 50);
  }

  public void test3() {
    final Abduction a = new Abduction();
    final World world = new World(1, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    final TestListener listen = new TestListener(CellEffectType.SPELL_FAILED);
    world.getCell(0).register(listen);
    world.getCell(0).push(w);
    a.cast(world, w, world.getCell(0));
    listen.checkAndReset();
  }

  public void testDead() {
    final Abduction a = new Abduction();
    final World world = new World(1, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    final Lion l = new Lion();
    l.setOwner(1);
    l.setState(State.DEAD);
    world.getCell(0).push(l);
    for (int i = 0; i < 20; ++i) {
      a.cast(world, w, null);
      assertEquals(1, l.getOwner());
    }
  }

  public void testMounted() {
    final Abduction a = new Abduction();
    final World world = new World(1, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    final Horse l = new Horse();
    l.setOwner(1);
    l.setMount(w);
    world.getCell(0).push(l);
    for (int i = 0; i < 20; ++i) {
      a.cast(world, w, null);
      assertEquals(1, l.getOwner());
    }
  }

  public void testWizard() {
    final Abduction a = new Abduction();
    final World world = new World(1, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    final Wizard1 l = new Wizard1();
    l.setOwner(1);
    world.getCell(0).push(l);
    for (int i = 0; i < 20; ++i) {
      a.cast(world, w, null);
      assertEquals(1, l.getOwner());
    }
  }

  public void testNasty() {
    Object obj = TestUtils.getField("ATTEMPTS_TO_FIND_TARGET", Abduction.class);
    assertTrue(obj instanceof Integer);
    assertEquals(1000, ((Integer) obj).intValue());
    obj = TestUtils.getField("ATTEMPTS_TO_SUBVERT", Abduction.class);
    assertTrue(obj instanceof Integer);
    assertEquals(3, ((Integer) obj).intValue());
    obj = TestUtils.getField("SUBVERSION_FACTOR", Abduction.class);
    assertTrue(obj instanceof Integer);
    assertEquals(1, (Integer) obj - Attribute.INTELLIGENCE.max());
  }
}
