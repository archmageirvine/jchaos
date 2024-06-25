package chaos.common.free;

import java.util.Collection;

import chaos.Chaos;
import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Realm;
import chaos.common.State;
import chaos.common.inanimate.MagicWood;
import chaos.common.monster.AirElemental;
import chaos.common.monster.GoblinBomb;
import chaos.common.monster.Horse;
import chaos.common.monster.Lion;
import chaos.common.monster.Skeleton;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Event;
import chaos.util.EventListener;
import chaos.util.PolycellEffectEvent;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class DevastationTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Devastation();
  }

  private static class MyListener implements EventListener {
    private boolean mC = false;
    private final World mWorld;

    MyListener(final World world) {
      mWorld = world;
    }

    @Override
    public void update(final Event e) {
      if (e instanceof CellEffectEvent) {
        fail();
      } else if (e instanceof PolycellEffectEvent) {
        final PolycellEffectEvent p = (PolycellEffectEvent) e;
        if (p.getEventType() != CellEffectType.REDRAW_CELL) {
          final Collection<Cell> s = p.getCells();
          if (!mC) {
            assertEquals(6, s.size());
            assertTrue(s.contains(mWorld.getCell(0)));
            assertTrue(s.contains(mWorld.getCell(1)));
            assertTrue(s.contains(mWorld.getCell(2)));
            assertTrue(s.contains(mWorld.getCell(3)));
            assertTrue(s.contains(mWorld.getCell(4)));
            assertTrue(s.contains(mWorld.getCell(7)));
            mC = true;
          } else {
            assertEquals(2, s.size());
            assertTrue(s.contains(mWorld.getCell(1)));
            assertTrue(s.contains(mWorld.getCell(2)));
          }
        }
      }
    }
  }

  public void test1() {
    final Devastation a = new Devastation();
    assertEquals(Castable.CAST_SINGLE, a.getCastFlags());
    final World world = new World(8, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    a.cast(null, null, null);
    a.cast(world, null, null);
    a.cast(null, w, null);
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
    world.getCell(5).push(w);
    // this is complicated push, to check dual ram of devastation
    final Lion l = new Lion();
    world.getCell(7).push(l);
    world.getCell(7).push(l);
    final EventListener listen2 = new MyListener(world);
    world.register(listen2);
    a.cast(world, w, world.getCell(5));
    assertEquals(180, w.getScore());
    assertEquals(w, world.actor(5));
    assertNull(world.actor(0));
    assertNull(world.actor(1));
    assertNull(world.actor(2));
    assertNull(world.actor(3));
    assertNull(world.actor(4));
    assertNull(world.actor(6));
  }

  public void testBug163() {
    final Devastation a = new Devastation();
    final World world = Chaos.getChaos().getWorld();
    for (int k = 0; k < 3; ++k) {
      final Skeleton s = new Skeleton();
      s.set(Attribute.LIFE, 1);
      world.getCell(k).push(s);
    }
    final GoblinBomb g = new GoblinBomb();
    g.setRealm(Realm.ETHERIC);
    world.getCell(world.width() + 1).pop();
    world.getCell(world.width() + 1).push(g);
    a.cast(world, null, null);
  }

}
