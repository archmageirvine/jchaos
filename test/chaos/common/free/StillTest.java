package chaos.common.free;

import chaos.board.World;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.growth.Earthquake;
import chaos.common.monster.Horse;
import chaos.common.wizard.Wizard1;
import chaos.util.EventListener;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class StillTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Still();
  }

  public void test1() {
    final Still a = new Still();
    assertEquals(Castable.CAST_SINGLE, a.getCastFlags());
    final World world = new World(7, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    a.cast(null, null, null);
    a.cast(world, null, null);
    a.cast(null, w, null);
    final EventListener listen = new MyEventListener();
    world.register(listen);
    a.cast(world, w, world.getCell(1));
    assertEquals(0, w.getScore());
    world.deregister(listen);
    world.getCell(0).push(new Earthquake());
    world.getCell(1).push(new Earthquake());
    final Horse h = new Horse();
    world.getCell(2).push(h);
    final Horse dh2 = new Horse();
    dh2.setState(State.DEAD);
    world.getCell(3).push(dh2);
    final Earthquake mw = new Earthquake();
    mw.setOwner(3);
    world.getCell(3).push(mw);
    final Horse dh = new Horse();
    dh.setState(State.DEAD);
    world.getCell(4).push(dh);
    world.getCell(5).push(w);
    final EventListener listen2 = new YourListener(world);
    world.register(listen2);
    a.cast(world, w, world.getCell(5));
    assertEquals(12, w.getScore());
    assertEquals(h, world.actor(2));
    assertEquals(State.ACTIVE, h.getState());
    assertEquals(dh, world.actor(4));
    assertEquals(w, world.actor(5));
    assertNull(world.actor(0));
    assertNull(world.actor(1));
    assertEquals(dh2, world.actor(3));
  }

}
