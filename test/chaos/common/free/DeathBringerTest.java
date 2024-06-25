package chaos.common.free;

import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.monster.Horse;
import chaos.common.monster.StoneGiant;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.EventListener;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class DeathBringerTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new DeathBringer();
  }

  private boolean mRedraw = false;

  private void setRedraw() {
    mRedraw = true;
  }

  private boolean getRedraw() {
    return mRedraw;
  }

  private boolean mAbduct = false;

  private void setAbduct() {
    mAbduct = true;
  }

  private boolean getAbduct() {
    return mAbduct;
  }

  public void test1() {
    final DeathBringer a = new DeathBringer();
    final World world = new World(1, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    a.cast(null, null, null);
    a.cast(world, null, null);
    a.cast(null, w, null);
    a.cast(world, w, null);
    final EventListener listen = e -> {
      if (e instanceof CellEffectEvent) {
        final CellEffectEvent ce = (CellEffectEvent) e;
        if (ce.getEventType() == CellEffectType.REDRAW_CELL) {
          assertFalse(getRedraw());
          setRedraw();
        } else if (ce.getEventType() == CellEffectType.DEATH) {
          assertFalse(getAbduct());
          setAbduct();
        } else if (ce.getEventType() == CellEffectType.SPELL_FAILED) {
          fail("bad event");
        }
      }
    };
    world.getCell(0).register(listen);
    final StoneGiant l = new StoneGiant();
    l.setOwner(1);
    world.getCell(0).push(l);
    mRedraw = false;
    mAbduct = false;
    a.cast(world, w, null);
    assertEquals(State.DEAD, l.getState());
    assertTrue(getRedraw());
    assertTrue(getAbduct());
    assertEquals(2, w.getBonusCount());
    assertEquals(1, w.getBonusSelect());
    assertEquals(l.getDefault(Attribute.LIFE), w.getScore());
  }

  public void test3() {
    final DeathBringer a = new DeathBringer();
    final World world = new World(1, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    final EventListener listen = e -> {
      if (e instanceof CellEffectEvent) {
        final CellEffectEvent ce = (CellEffectEvent) e;
        if (ce.getEventType() == CellEffectType.SPELL_FAILED) {
          assertFalse(getAbduct());
          setAbduct();
        }
      }
    };
    world.getCell(0).register(listen);
    world.getCell(0).push(w);
    mAbduct = false;
    a.cast(world, w, world.getCell(0));
    assertTrue(getAbduct());
  }

  public void testDead() {
    final DeathBringer a = new DeathBringer();
    final World world = new World(1, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    final StoneGiant l = new StoneGiant();
    l.setOwner(1);
    l.setState(State.DEAD);
    world.getCell(0).push(l);
    a.cast(world, w, null);
    assertEquals(l, world.actor(0));
    assertEquals(0, w.getBonusCount());
    assertEquals(0, w.getBonusSelect());
    assertEquals(0, w.getScore());
  }

  public void testMounted() {
    final DeathBringer a = new DeathBringer();
    final World world = new World(1, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    final Horse l = new Horse();
    l.setOwner(1);
    l.setMount(w);
    world.getCell(0).push(l);
    a.cast(world, w, null);
    assertEquals(w, world.actor(0));
  }

  public void testWizard() {
    final DeathBringer a = new DeathBringer();
    final World world = new World(1, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    final Wizard1 l = new Wizard1();
    l.setOwner(1);
    world.getCell(0).push(l);
    a.cast(world, w, null);
    assertEquals(l, world.actor(0));
  }

}
