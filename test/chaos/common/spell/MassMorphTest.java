package chaos.common.spell;

import chaos.board.Cell;
import chaos.board.WizardManager;
import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.wizard.Wizard;
import chaos.common.free.MyEventListener;
import chaos.common.growth.OrangeJelly;
import chaos.common.inanimate.MagicWood;
import chaos.common.inanimate.ShadowWood;
import chaos.common.inanimate.WaspNest;
import chaos.common.monster.Horse;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.Event;
import chaos.util.EventListener;
import chaos.util.PolycellEffectEvent;
import junit.framework.Assert;

import java.util.Collection;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class MassMorphTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new MassMorph();
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
    final Castable a = getCastable();
    assertEquals(Castable.CAST_LIVING | Castable.CAST_INANIMATE | Castable.CAST_GROWTH, a.getCastFlags());
    assertEquals(Castable.MAX_CAST_RANGE, a.getCastRange());
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
    a.cast(world, w, world.getCell(5), world.getCell(5));
    assertTrue(world.actor(0) instanceof MagicWood);
    assertTrue(world.actor(1) instanceof ShadowWood);
    assertTrue(world.actor(2) instanceof MagicWood);
    assertTrue(world.actor(3) instanceof Horse);
    assertTrue(world.actor(4) instanceof MagicWood);
    assertTrue(world.actor(5) instanceof Wizard);
    assertTrue(world.actor(6) instanceof Horse);
    assertEquals(State.ACTIVE, world.actor(4).getState());
    assertEquals(3, world.actor(4).getOwner());
    assertNull(world.actor(8));
    world.deregister(listen2);
  }

  public void testDeadOwner() {
    final Castable a = getCastable();
    final World world = new World(1, 1);
    final WizardManager wm = world.getWizardManager();
    wm.getWizard(2).setState(State.ACTIVE);
    wm.getWizard(3).setState(State.DEAD);
    final WaspNest h = new WaspNest();
    h.setOwner(3);
    world.getCell(0).push(h);
    a.cast(world, wm.getWizard(2), world.getCell(0), world.getCell(0));
    assertTrue(world.actor(0) instanceof MagicWood);
    assertEquals(State.ACTIVE, world.actor(0).getState());
    assertEquals(2, world.actor(0).getOwner());
  }

  public void testDeadNoOwner() {
    final Castable a = getCastable();
    final World world = new World(1, 1);
    final WizardManager wm = world.getWizardManager();
    wm.getWizard(2).setState(State.ACTIVE);
    final WaspNest h = new WaspNest();
    h.setOwner(Actor.OWNER_NONE);
    world.getCell(0).push(h);
    a.cast(world, wm.getWizard(2), world.getCell(0), world.getCell(0));
    assertTrue(world.actor(0) instanceof MagicWood);
    assertEquals(State.ACTIVE, world.actor(0).getState());
    assertEquals(2, world.actor(0).getOwner());
  }

  public void testLiveOwner() {
    final Castable a = getCastable();
    final World world = new World(1, 1);
    final WizardManager wm = world.getWizardManager();
    wm.getWizard(2).setState(State.ACTIVE);
    wm.getWizard(3).setState(State.ACTIVE);
    final WaspNest h = new WaspNest();
    h.setOwner(3);
    world.getCell(0).push(h);
    a.cast(world, wm.getWizard(2), world.getCell(0), world.getCell(0));
    assertTrue(world.actor(0) instanceof MagicWood);
    assertEquals(State.ACTIVE, world.actor(0).getState());
    assertEquals(3, world.actor(0).getOwner());
  }

  public void testBug309() {
    final Castable a = getCastable();
    final World world = new World(1, 1);
    final WizardManager wm = world.getWizardManager();
    wm.getWizard(2).setState(State.ACTIVE);
    final Lion l = new Lion();
    world.getCell(0).push(l);
    final OrangeJelly j = new OrangeJelly();
    world.getCell(0).push(j);
    a.cast(world, wm.getWizard(2), world.getCell(0), null);
    assertTrue(world.actor(0) instanceof MagicWood);
    world.getCell(0).pop();
    assertNull(world.actor(0));
  }

  public void testDeadActor() {
    final Castable a = getCastable();
    final World world = new World(1, 1);
    final WizardManager wm = world.getWizardManager();
    wm.getWizard(2).setState(State.ACTIVE);
    wm.getWizard(3).setState(State.ACTIVE);
    final WaspNest h = new WaspNest();
    h.setState(State.DEAD);
    h.setOwner(3);
    world.getCell(0).push(h);
    a.cast(world, wm.getWizard(2), world.getCell(0), world.getCell(0));
    assertEquals(h, world.actor(0));
    assertEquals(State.DEAD, world.actor(0).getState());
    assertEquals(3, world.actor(0).getOwner());
  }

  public void testAsleepActor() {
    final Castable a = getCastable();
    final World world = new World(1, 1);
    final WizardManager wm = world.getWizardManager();
    wm.getWizard(2).setState(State.ACTIVE);
    wm.getWizard(3).setState(State.ACTIVE);
    final WaspNest h = new WaspNest();
    h.setState(State.ASLEEP);
    h.setOwner(3);
    world.getCell(0).push(h);
    a.cast(world, wm.getWizard(2), world.getCell(0), world.getCell(0));
    assertEquals(h, world.actor(0));
    assertEquals(State.ASLEEP, world.actor(0).getState());
    assertEquals(3, world.actor(0).getOwner());
  }

}
