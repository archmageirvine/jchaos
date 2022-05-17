package chaos.common.free;

import chaos.board.World;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.inanimate.Generator;
import chaos.common.monster.Lion;
import chaos.common.monster.Orc;
import chaos.common.wizard.Wizard1;
import chaos.common.wizard.Wizard2;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class RepulsionTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Repulsion();
  }

  public void test1() {
    final Repulsion a = new Repulsion();
    assertEquals(Castable.CAST_SINGLE, a.getCastFlags());
    final World world = new World(5, 5);
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    w.setOwner(3);
    world.getCell(5).push(w);
    world.getCell(0).push(new Lion());
    world.getCell(6).push(new Lion());
    world.getCell(8).push(new Lion());
    world.getCell(11).push(new Lion());
    world.getCell(23).push(new Lion());
    world.getCell(15).push(new Lion());
    a.cast(world, w, world.getCell(5));
    assertNull(world.getCell(1).peek());
    assertNull(world.getCell(2).peek());
    assertNull(world.getCell(3).peek());
    assertNull(world.getCell(4).peek());
    assertNull(world.getCell(6).peek());
    assertNull(world.getCell(7).peek());
    assertNull(world.getCell(10).peek());
    assertNull(world.getCell(15).peek());
    assertNull(world.getCell(16).peek());
    assertNull(world.getCell(24).peek());
    assertTrue(world.getCell(0).peek() instanceof Lion);
    assertTrue(world.getCell(8).peek() instanceof Lion);
    assertTrue(world.getCell(9).peek() instanceof Lion);
    assertTrue(world.getCell(17).peek() instanceof Lion);
    assertTrue(world.getCell(23).peek() instanceof Lion);
    assertTrue(world.getCell(20).peek() instanceof Lion);
    assertTrue(world.getCell(5).peek() instanceof Wizard1);
    a.cast(null, null, null);
    a.cast(world, null, null);
    a.cast(null, w, null);
  }

  public void test2() {
    final Repulsion a = new Repulsion();
    assertEquals(Castable.CAST_SINGLE, a.getCastFlags());
    final World world = new World(5, 5);
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    w.setOwner(3);
    world.getCell(12).push(w);
    world.getCell(6).push(new Lion());
    world.getCell(7).push(new Lion());
    world.getCell(8).push(new Lion());
    world.getCell(11).push(new Lion());
    world.getCell(13).push(new Lion());
    world.getCell(16).push(new Lion());
    world.getCell(17).push(new Lion());
    world.getCell(18).push(new Lion());
    a.cast(world, w, world.getCell(12));
    assertNull(world.getCell(6).peek());
    assertNull(world.getCell(7).peek());
    assertNull(world.getCell(8).peek());
    assertNull(world.getCell(11).peek());
    assertNull(world.getCell(13).peek());
    assertNull(world.getCell(16).peek());
    assertNull(world.getCell(17).peek());
    assertNull(world.getCell(18).peek());
    assertNull(world.getCell(21).peek());
    assertNull(world.getCell(15).peek());
    assertTrue(world.getCell(0).peek() instanceof Lion);
    assertTrue(world.getCell(2).peek() instanceof Lion);
    assertTrue(world.getCell(4).peek() instanceof Lion);
    assertTrue(world.getCell(10).peek() instanceof Lion);
    assertTrue(world.getCell(14).peek() instanceof Lion);
    assertTrue(world.getCell(20).peek() instanceof Lion);
    assertTrue(world.getCell(24).peek() instanceof Lion);
    assertTrue(world.getCell(12).peek() instanceof Wizard1);
    a.cast(null, null, null);
    a.cast(world, null, null);
    a.cast(null, w, null);
  }

  public void testVertical() {
    final Repulsion r = new Repulsion();
    final World w = new World(1, 10);
    final Orc o0 = new Orc();
    final Orc o1 = new Orc();
    final Orc o2 = new Orc();
    final Orc o3 = new Orc();
    final Orc o4 = new Orc();
    w.getCell(0).push(o0);
    w.getCell(3).push(o1);
    w.getCell(5).push(o2);
    w.getCell(7).push(o3);
    w.getCell(9).push(o4);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    w.getCell(2).push(wiz);
    r.cast(w, wiz, w.getCell(2));
    assertNull(w.actor(1));
    assertNull(w.actor(3));
    assertNull(w.actor(4));
    assertNull(w.actor(5));
    assertEquals(o0, w.actor(0));
    assertEquals(o1, w.actor(6));
    assertEquals(o2, w.actor(7));
    assertEquals(o3, w.actor(8));
    assertEquals(o4, w.actor(9));
    assertEquals(wiz, w.actor(2));
  }

  public void testBug314() {
    // Bug with wizard under generator (i.e. Dead Revenge), used to expose wizard
    final World world = new World(1, 5);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    world.getCell(0).push(wiz);
    final Wizard2 wiz2 = new Wizard2();
    wiz2.setState(State.ACTIVE);
    wiz2.setOwner(2);
    world.getCell(1).push(wiz2);
    final Generator g = new Generator();
    g.setOwner(2);
    world.getCell(1).push(g);
    new Repulsion().cast(world, wiz, world.getCell(0));
    assertNull(world.actor(1));
    assertNull(world.actor(2));
    assertNull(world.actor(3));
    assertEquals(g, world.getCell(4).pop());
    assertEquals(wiz2, world.actor(4));
  }
}
