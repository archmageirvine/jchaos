package chaos.common.spell;

import java.util.Collection;
import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.free.MyEventListener;
import chaos.common.inanimate.MagicWood;
import chaos.common.inanimate.ShadowWood;
import chaos.common.monster.Horse;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.Event;
import chaos.util.EventListener;
import chaos.util.PolycellEffectEvent;
import junit.framework.Assert;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class InvertTest extends AbstractCastableTest {


  @Override
  public Castable getCastable() {
    return new Invert();
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
        Assert.assertTrue(s.contains(mWorld.getCell(2)));
        Assert.assertTrue(s.contains(mWorld.getCell(4)));
      }
    }
  }

  public void test1() {
    final Invert a = new Invert();
    assertEquals(Castable.CAST_LIVING | Castable.CAST_INANIMATE | Castable.CAST_GROWTH | Castable.CAST_LOS, a.getCastFlags());
    assertEquals(4, a.getCastRange());
    final World world = new World(9, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    a.cast(null, null, null, null);
    a.cast(world, null, null, null);
    a.cast(null, w, null, null);
    final EventListener listen = new MyEventListener();
    world.register(listen);
    a.cast(world, w, world.getCell(1), world.getCell(1));
    assertEquals(0, w.getScore());
    world.deregister(listen);
    world.getCell(0).push(new MagicWood());
    world.getCell(1).push(new ShadowWood());
    final Lion h = new Lion();
    h.set(Attribute.LIFE, 1);
    h.setOwner(3);
    world.getCell(2).push(h);
    final Horse dh2 = new Horse();
    dh2.setState(State.DEAD);
    dh2.setOwner(3);
    world.getCell(3).push(dh2);
    final MagicWood mw = new MagicWood();
    mw.setOwner(3);
    world.getCell(4).push(mw);
    final Horse dh = new Horse();
    dh.setState(State.DEAD);
    world.getCell(6).push(dh);
    world.getCell(5).push(w);
    final EventListener listen2 = new MyListener(world);
    world.register(listen2);
    a.cast(world, w, world.getCell(5), world.getCell(5));
    assertEquals(22, world.actor(0).get(Attribute.LIFE));
    assertEquals(20, world.actor(1).get(Attribute.LIFE));
    assertEquals(63, world.actor(2).get(Attribute.LIFE));
    assertEquals(8, world.actor(3).get(Attribute.LIFE));
    assertEquals(42, world.actor(4).get(Attribute.LIFE));
    assertEquals(19, world.actor(5).get(Attribute.LIFE));
    assertEquals(8, world.actor(6).get(Attribute.LIFE));
  }

  public void testFilter() {
    final World w = new World(2, 1);
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(1);
    final Invert x = new Invert();
    final HashSet<Cell> t = new HashSet<>();
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    final Cell cn = new Cell(23);
    t.add(cn);
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    final Cell c = new Cell(0);
    c.push(wiz);
    t.add(c);
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    t.add(c);
    wiz.set(Attribute.LIFE, 11);
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    t.add(c);
    wiz.set(Attribute.LIFE, 10);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertTrue(t.contains(c));
    final Cell c2 = new Cell(1);
    final Lion l2 = new Lion();
    l2.setOwner(1);
    c2.push(l2);
    t.add(c2);
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    t.add(c2);
    t.add(c);
    wiz.set(Attribute.LIFE, 1);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertTrue(t.contains(c));
    l2.setOwner(2);
    t.add(c2);
    t.add(c);
    wiz.set(Attribute.LIFE, 1);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertTrue(t.contains(c));
  }
}
