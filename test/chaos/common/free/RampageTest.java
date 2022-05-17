package chaos.common.free;

import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.monster.Horse;
import chaos.common.monster.Skeleton;
import chaos.common.monster.StoneGiant;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class RampageTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Rampage();
  }

  public void test1() {
    final Rampage a = new Rampage();
    final World world = new World(3, 3);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    w.setState(State.ACTIVE);
    a.cast(null, null, null);
    a.cast(world, null, null);
    a.cast(null, w, null);
    a.cast(world, w, null);
    world.getCell(4).push(w);
    final Horse h0 = new Horse();
    h0.setOwner(1);
    h0.set(Attribute.LIFE, 6);
    world.getCell(0).push(h0);
    final Horse h1 = new Horse();
    h1.setOwner(3);
    world.getCell(1).push(h1);
    final Horse hd = new Horse();
    hd.setState(State.DEAD);
    world.getCell(2).push(hd);
    world.getCell(3).push(new Skeleton());
    final Skeleton sk = new Skeleton();
    sk.setOwner(3);
    world.getCell(5).push(sk);
    world.getCell(8).push(new StoneGiant());
    a.cast(world, w, world.getCell(4));
    assertTrue(world.actor(0) instanceof Horse);
    assertEquals(State.DEAD, world.actor(0).getState());
    assertEquals(h1, world.actor(1));
    assertEquals(h1.getDefault(Attribute.LIFE), h1.get(Attribute.LIFE));
    assertTrue(world.actor(2) instanceof Horse);
    assertEquals(State.DEAD, world.actor(2).getState());
    assertNull(world.actor(3));
    assertEquals(sk, world.actor(5));
    assertEquals(sk.getDefault(Attribute.LIFE), sk.get(Attribute.LIFE));
    assertEquals(world.actor(8).getDefault(Attribute.LIFE) - 6, world.actor(8).get(Attribute.LIFE));
    assertTrue(w.get(Attribute.LIFE) >= w.getDefault(Attribute.LIFE));
  }

}
