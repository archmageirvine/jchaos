package chaos.common.free;

import java.util.Collection;

import junit.framework.Assert;
import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.inanimate.ShadowWood;
import chaos.common.monster.GiantBeetle;
import chaos.common.monster.Horse;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.Event;
import chaos.util.EventListener;
import chaos.util.PolycellEffectEvent;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class MassResurrectTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new MassResurrect();
  }

  private static class MyListener implements EventListener {
    private final World mWorld;
    MyListener(final World world) {
      mWorld = world;
    }
    @Override
    public void update(final Event e) {
      if (e instanceof CellEffectEvent) {
        Assert.fail();
      } else if (e instanceof PolycellEffectEvent) {
        final PolycellEffectEvent p = (PolycellEffectEvent) e;
        final Collection<Cell> s = p.getCells();
        Assert.assertEquals(2, s.size());
        Assert.assertTrue(s.contains(mWorld.getCell(3)));
        Assert.assertTrue(s.contains(mWorld.getCell(4)));
      }
    }
  }

  public void test1() {
    final MassResurrect a = new MassResurrect();
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
    world.getCell(0).push(new Lion());
    world.getCell(1).push(new ShadowWood());
    final Horse h = new Horse();
    world.getCell(2).push(h);
    final Horse dh2 = new Horse();
    dh2.setState(State.DEAD);
    world.getCell(3).push(dh2);
    final Horse dh = new Horse();
    dh.setState(State.DEAD);
    world.getCell(4).push(dh);
    world.getCell(5).push(w);
    final EventListener listen2 = new MyListener(world);
    world.register(listen2);
    a.cast(world, w, world.getCell(5));
    assertTrue(world.actor(3) instanceof GiantBeetle);
    assertTrue(world.actor(4) instanceof GiantBeetle);
    assertEquals(w, world.actor(5));
    assertTrue(world.actor(0) instanceof Lion);
  }
}
