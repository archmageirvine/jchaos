package chaos.common.free;

import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.inanimate.Rock;
import chaos.common.monster.Horse;
import chaos.common.monster.StoneGiant;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectType;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class MeteorStormTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new MeteorStorm();
  }

  public void test1() {
    final MeteorStorm a = new MeteorStorm();
    final World world = new World(1, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    final StoneGiant l = new StoneGiant();
    l.setOwner(1);
    world.getCell(0).push(l);
    final TestListener listen = new TestListener(10, 10, 20, CellEffectType.REDRAW_CELL, CellEffectType.WHITE_CIRCLE_EXPLODE);
    world.register(listen);
    a.cast(world, w, null);
    world.deregister(listen);
    listen.checkAndReset();
    assertTrue(world.actor(0) instanceof Rock);
    a.cast(null, null, null);
    a.cast(world, null, null);
    a.cast(null, w, null);
    a.cast(world, w, null);
  }

  public void test2() {
    final MeteorStorm a = new MeteorStorm();
    final World world = new World(1, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    final StoneGiant l = new StoneGiant();
    l.setState(State.DEAD);
    world.getCell(0).push(l);
    a.cast(world, w, null);
    assertTrue(world.actor(0) instanceof Rock);
  }

  public void test3() {
    final MeteorStorm a = new MeteorStorm();
    final World world = new World(1, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    w.setState(State.ACTIVE);
    world.getCell(0).push(w);
    a.cast(world, w, null);
    assertEquals(w, world.actor(0));
  }

  public void test4() {
    final MeteorStorm a = new MeteorStorm();
    final World world = new World(1, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    a.cast(world, w, null);
    assertTrue(world.actor(0) instanceof Rock);
  }

  public void test5() {
    final MeteorStorm a = new MeteorStorm();
    final World world = new World(1, 2);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    final StoneGiant l = new StoneGiant();
    l.set(Attribute.LIFE, Attribute.LIFE.max());
    world.getCell(0).push(l);
    final StoneGiant l1 = new StoneGiant();
    l1.set(Attribute.LIFE, Attribute.LIFE.max());
    world.getCell(1).push(l1);
    a.cast(world, w, null);
    assertTrue(l == world.actor(0) || l1 == world.actor(1));
  }

  public void test6() {
    final MeteorStorm a = new MeteorStorm();
    final World world = new World(1, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    final StoneGiant l = new StoneGiant();
    l.set(Attribute.LIFE, 100);
    world.getCell(0).push(l);
    a.cast(world, w, null);
    assertTrue(world.actor(0) instanceof Rock);
  }

  public void testSquashMountedWizard() {
    final MeteorStorm a = new MeteorStorm();
    final World world = new World(1, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    final Horse l = new Horse();
    l.set(Attribute.LIFE, 1);
    l.setMount(w);
    world.getCell(0).push(l);
    a.cast(world, w, null);
    assertEquals(w, world.actor(0));
  }
}
