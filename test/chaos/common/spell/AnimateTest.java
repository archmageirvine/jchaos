package chaos.common.spell;

import java.util.Collection;
import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.free.MyEventListener;
import chaos.common.inanimate.MagicWood;
import chaos.common.inanimate.ShadowWood;
import chaos.common.inanimate.WaspNest;
import chaos.common.monster.HigherDevil;
import chaos.common.monster.Horse;
import chaos.common.monster.Lion;
import chaos.common.monster.WoodElf;
import chaos.common.wizard.Wizard;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.Event;
import chaos.util.EventListener;
import chaos.util.PolycellEffectEvent;
import junit.framework.Assert;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class AnimateTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new Animate();
  }

  private boolean mRedraw = false;

  private void setRedraw() {
    mRedraw = true;
  }

  private boolean getRedraw() {
    return mRedraw;
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
    final Castable aa = getCastable();
    assertEquals(Castable.CAST_LIVING | Castable.CAST_INANIMATE | Castable.CAST_GROWTH, aa.getCastFlags());
    assertEquals(Castable.MAX_CAST_RANGE, aa.getCastRange());
    final World world = new World(9, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    aa.cast(null, null, null, null);
    aa.cast(world, null, null, null);
    aa.cast(null, w, null, null);
    final EventListener listen = new MyEventListener();
    world.register(listen);
    aa.cast(world, w, world.getCell(1), world.getCell(1));
    assertEquals(0, w.getScore());
    world.deregister(listen);
    world.getCell(0).push(new MagicWood());
    world.getCell(1).push(new ShadowWood());
    final WaspNest h = new WaspNest();
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
    aa.cast(world, w, world.getCell(5), world.getCell(5));
    assertTrue(world.actor(0) instanceof MagicWood);
    assertTrue(world.actor(1) instanceof ShadowWood);
    assertTrue(world.actor(2) instanceof HigherDevil);
    assertTrue(world.actor(3) instanceof Horse);
    assertTrue(world.actor(4) instanceof WoodElf);
    assertTrue(world.actor(5) instanceof Wizard);
    assertTrue(world.actor(6) instanceof Horse);
    assertEquals(State.ACTIVE, world.actor(4).getState());
    assertEquals(3, world.actor(4).getOwner());
    assertNull(world.actor(8));
    world.deregister(listen2);
    final EventListener listen3 = e -> {
      if (e instanceof PolycellEffectEvent) {
        fail();
      } else if (e instanceof CellEffectEvent) {
        assertEquals(5, ((CellEffectEvent) e).getCellNumber());
        setRedraw();
      }
    };
    world.register(listen3);
    aa.cast(world, w, world.getCell(5), world.getCell(5));
    assertTrue(world.actor(0) instanceof MagicWood);
    assertTrue(world.actor(1) instanceof ShadowWood);
    assertTrue(world.actor(2) instanceof HigherDevil);
    assertTrue(world.actor(3) instanceof Horse);
    assertTrue(world.actor(4) instanceof WoodElf);
    assertTrue(world.actor(5) instanceof Wizard);
    assertTrue(world.actor(6) instanceof Horse);
    assertEquals(State.ACTIVE, world.actor(4).getState());
    assertEquals(3, world.actor(4).getOwner());
    assertNull(world.actor(8));
    assertTrue(getRedraw());
    world.deregister(listen3);
    aa.cast(world, w, world.getCell(0), world.getCell(5));
    assertTrue(world.actor(0) instanceof WoodElf);
    assertTrue(world.actor(2) instanceof HigherDevil);
    assertTrue(world.actor(3) instanceof Horse);
    assertTrue(world.actor(4) instanceof WoodElf);
    assertTrue(world.actor(5) instanceof Wizard);
    assertTrue(world.actor(6) instanceof Horse);
    assertEquals(State.ACTIVE, world.actor(0).getState());
    assertEquals(-1, world.actor(0).getOwner());
    assertNull(world.actor(8));
  }

  public void testFilter() {
    final World w = new World(2, 1);
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(1);
    final Animate x = new Animate();
    final HashSet<Cell> t = new HashSet<>();
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    final Cell cn = new Cell(23);
    t.add(cn);
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    final Cell c = new Cell(0);
    final Lion l = new Lion();
    l.setOwner(1);
    c.push(l);
    t.add(c);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertTrue(t.contains(c));
    final Cell c2 = new Cell(1);
    final Lion l2 = new Lion();
    l2.setOwner(1);
    c2.push(l2);
    t.add(c2);
    x.filter(t, wiz, w);
    assertEquals(2, t.size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
  }
}
