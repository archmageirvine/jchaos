package chaos.common.free;

import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Growth;
import chaos.common.Monster;
import chaos.common.State;
import chaos.common.monster.CatLord;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class SummonsTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Summons();
  }

  public void test1() {
    final Summons a = new Summons();
    final World world = new World(3, 4);
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    w.setOwner(7);
    world.getCell(4).push(w);
    final Lion l = new Lion();
    l.setState(State.DEAD);
    world.getCell(1).push(l);
    final CatLord cl = new CatLord();
    cl.setOwner(1);
    world.getCell(world.size() - 1).push(cl); // should stop a new one
    assertFalse(Actor.OWNER_NONE == world.isCatLordAlive());
    a.cast(null, null, null);
    a.cast(world, null, null);
    a.cast(null, w, null);
    a.cast(world, w, world.getCell(4));
    final Actor[] act = new Actor[9];
    for (int i = 0; i < act.length; ++i) {
      final Actor ac = world.actor(i);
      assertTrue(ac.toString(), ac instanceof Monster);
      assertFalse(ac.toString(), ac instanceof Growth);
      assertFalse(ac.toString(), ac instanceof CatLord);
      assertEquals(7, ac.getOwner());
      assertEquals(State.ACTIVE, ac.getState());
      act[i] = ac;
    }
    a.cast(world, w, world.getCell(4));
    for (int i = 0; i < act.length; ++i) {
      assertEquals(act[i], world.actor(i));
    }
  }
}
