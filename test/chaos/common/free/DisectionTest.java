package chaos.common.free;

import java.util.Collection;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.inanimate.MagicWood;
import chaos.common.monster.AirElemental;
import chaos.common.monster.Horse;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Event;
import chaos.util.EventListener;
import chaos.util.PolycellEffectEvent;
import junit.framework.Assert;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class DisectionTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Disection();
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
        if (p.getEventType() != CellEffectType.REDRAW_CELL) {
          final Collection<Cell> s = p.getCells();
          Assert.assertEquals(6, s.size());
          Assert.assertTrue(s.contains(mWorld.getCell(0)));
          Assert.assertTrue(s.contains(mWorld.getCell(1)));
          Assert.assertTrue(s.contains(mWorld.getCell(2)));
          Assert.assertTrue(s.contains(mWorld.getCell(3)));
          Assert.assertTrue(s.contains(mWorld.getCell(5)));
          Assert.assertTrue(s.contains(mWorld.getCell(7)));
        }
      }
    }
  }

  public void test1() {
    final Disection a = new Disection();
    assertEquals(Castable.CAST_SINGLE, a.getCastFlags());
    final World world = new World(8, 1);
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    w.setOwner(3);
    world.getCell(5).push(w);
    world.getCell(0).push(new MagicWood());
    world.getCell(1).push(new AirElemental());
    final Horse h = new Horse();
    world.getCell(2).push(h);
    final Horse dh2 = new Horse();
    dh2.setState(State.DEAD);
    world.getCell(3).push(dh2);
    final MagicWood mw = new MagicWood();
    mw.setOwner(3);
    world.getCell(3).push(mw);
    final Horse dh = new Horse();
    dh.setState(State.DEAD);
    world.getCell(4).push(dh);
    final Lion l = new Lion();
    world.getCell(7).push(l);
    final EventListener listen2 = new MyListener(world);
    world.register(listen2);
    a.cast(world, w, world.getCell(5));
    assertEquals(15, world.actor(7).get(Attribute.LIFE));
    assertEquals(10, world.actor(5).get(Attribute.LIFE));
    world.deregister(listen2);
    a.cast(null, null, null);
    a.cast(world, null, null);
    a.cast(null, w, null);
  }
}
