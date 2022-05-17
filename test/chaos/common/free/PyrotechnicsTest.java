package chaos.common.free;

import irvine.TestUtils;
import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.FreeCastable;
import chaos.common.State;
import chaos.common.monster.StoneGiant;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectType;
import chaos.util.WeaponEffectType;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class PyrotechnicsTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Pyrotechnics();
  }

  public void test1() {
    final FreeCastable a = (FreeCastable) getCastable();
    final World world = new World(1, 2);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    w.setState(State.ACTIVE);
    a.cast(null, null, null);
    a.cast(world, null, null);
    a.cast(null, w, null);
    a.cast(world, w, null);
    world.getCell(1).push(w);
    final StoneGiant l = new StoneGiant();
    l.set(Attribute.LIFE, 4 * 15);
    world.getCell(0).push(l);
    final TestListener listen = new TestListener(15, 15, -2, CellEffectType.REDRAW_CELL, CellEffectType.WHITE_CIRCLE_EXPLODE);
    world.register(listen);
    a.cast(world, w, world.getCell(1));
    world.deregister(listen);
    assertEquals(State.DEAD, l.getState());
    assertEquals(State.ACTIVE, w.getState());
    listen.checkAndReset();
    assertEquals(2, w.getBonusCount());
    assertEquals(1, w.getBonusSelect());
    assertEquals(l.getDefault(Attribute.LIFE), w.getScore());
  }

  public void test2() {
    final FreeCastable a = (FreeCastable) getCastable();
    final World world = new World(1, 2);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    w.setState(State.ACTIVE);
    world.getCell(1).push(w);
    final StoneGiant l = new StoneGiant();
    l.set(Attribute.LIFE, 4 * 15 + 1);
    world.getCell(0).push(l);
    a.cast(world, w, world.getCell(1));
    assertEquals(State.ACTIVE, l.getState());
    assertEquals(1, l.get(Attribute.LIFE));
    assertEquals(State.ACTIVE, w.getState());
  }

  public void test3() {
    final FreeCastable a = (FreeCastable) getCastable();
    final World world = new World(1, 2);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    w.setState(State.ACTIVE);
    world.getCell(1).push(w);
    final StoneGiant l = new StoneGiant();
    l.set(Attribute.LIFE, 4 * 14);
    world.getCell(0).push(l);
    a.cast(world, w, world.getCell(1));
    assertEquals(State.DEAD, l.getState());
    assertEquals(State.ACTIVE, w.getState());
    assertEquals(2, w.getBonusCount());
    assertEquals(1, w.getBonusSelect());
    assertEquals(l.getDefault(Attribute.LIFE) + 5, w.getScore());
  }

  public void testNasty() {
    final Object obj = TestUtils.getField("WEAPONS", Pyrotechnics.class);
    assertTrue(obj instanceof WeaponEffectType[]);
    final WeaponEffectType[] w = (WeaponEffectType[]) obj;
    assertEquals(5, w.length);
    assertEquals(WeaponEffectType.LIGHTNING, w[0]);
    assertEquals(WeaponEffectType.ICE_BEAM, w[1]);
    assertEquals(WeaponEffectType.FIREBALL, w[2]);
    assertEquals(WeaponEffectType.BALL, w[3]);
    assertEquals(WeaponEffectType.SPINNER, w[4]);
  }
}
