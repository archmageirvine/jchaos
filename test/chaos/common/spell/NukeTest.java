package chaos.common.spell;

import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.inanimate.Nuked;
import chaos.common.monster.Horse;
import chaos.common.monster.StoneGiant;
import chaos.common.wizard.Wizard1;
import chaos.common.wizard.Wizard2;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.EventListener;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class NukeTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new Nuke();
  }

  private boolean mCast = false;

  private void setCast() {
    mCast = true;
  }

  private boolean getCast() {
    return mCast;
  }

  private boolean mRedraw = false;

  private void setRedraw() {
    mRedraw = true;
  }

  private boolean getRedraw() {
    return mRedraw;
  }

  public void testCast() {
    final Castable x = new Nuke();
    assertEquals(Castable.CAST_LIVING | Castable.CAST_GROWTH | Castable.CAST_INANIMATE | Castable.CAST_NOEXPOSEDWIZARD, x.getCastFlags());
    assertEquals(Castable.MAX_CAST_RANGE, x.getCastRange());
    final Wizard1 w = new Wizard1();
    final World world = new World(3, 1);
    final EventListener listen = e -> {
      if (e instanceof CellEffectEvent) {
        final CellEffectEvent ce = (CellEffectEvent) e;
        if (ce.getEventType() == CellEffectType.REDRAW_CELL) {
          assertFalse(getRedraw());
          setRedraw();
        } else if (ce.getEventType() == CellEffectType.BOMB) {
          assertFalse(getCast());
          setCast();
        }
      }
    };
    w.setState(State.ACTIVE);
    world.getCell(0).push(w);
    world.register(listen);
    final Horse h = new Horse();
    world.getCell(1).push(h);
    final Horse h2 = new Horse();
    h2.setState(State.DEAD);
    world.getCell(2).push(h2);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertTrue(world.actor(1) instanceof Nuked);
    assertTrue(getRedraw());
    assertTrue(getCast());
    mCast = false;
    mRedraw = false;
    assertEquals(8, w.getScore());
    assertEquals(w.getDefault(Attribute.LIFE) - 10, w.get(Attribute.LIFE));
    assertEquals(w, world.actor(0));
    assertNull(world.actor(2));
    w.setState(State.DEAD);
    final StoneGiant sg = new StoneGiant();
    world.getCell(1).push(sg);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertTrue(world.actor(1) instanceof Nuked);
    assertTrue(getRedraw());
    assertTrue(getCast());
    mCast = false;
    mRedraw = false;
    assertEquals(56, w.getScore());
    assertEquals(1, w.getBonusSelect());
    // Cast nuke on wizard mounted in horse
    final Wizard2 ww = new Wizard2();
    ww.setState(State.ACTIVE);
    h.setMount(ww);
    h.setState(State.ACTIVE);
    h.set(Attribute.LIFE, h.getDefault(Attribute.LIFE));
    world.getCell(1).pop();
    world.getCell(1).pop();
    world.getCell(1).push(h);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(h, world.actor(1));
    assertEquals(State.ACTIVE, h.getState());
    assertEquals(h.getDefault(Attribute.LIFE), h.get(Attribute.LIFE));
    assertFalse(getRedraw());
    assertFalse(getCast());
    world.deregister(listen);
    x.cast(world, w, world.getCell(0), null);
    x.cast(world, w, null, world.getCell(0));
    x.cast(world, null, world.getCell(0), world.getCell(0));
    x.cast(null, w, world.getCell(0), world.getCell(0));
  }
}
