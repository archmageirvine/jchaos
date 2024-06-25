package chaos.common.free;

import chaos.board.World;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.inanimate.PowerWallHorizontal;
import chaos.common.inanimate.WeakWall;
import chaos.common.monster.Horse;
import chaos.common.wizard.Wizard1;
import chaos.util.EventListener;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class DestroyWallTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new DestroyWall();
  }

  public void test1() {
    final DestroyWall a = new DestroyWall();
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
    world.getCell(0).push(new WeakWall());
    world.getCell(1).push(new PowerWallHorizontal());
    final Horse h = new Horse();
    world.getCell(2).push(h);
    final Horse dh2 = new Horse();
    dh2.setState(State.DEAD);
    world.getCell(3).push(dh2);
    final WeakWall mw = new WeakWall();
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
