package chaos.common.spell;

import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.EventListener;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class BuryTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new Bury();
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
    final Bury x = new Bury();
    assertEquals(Castable.CAST_DEAD, x.getCastFlags());
    assertEquals(Castable.MAX_CAST_RANGE, x.getCastRange());
    assertEquals(8, x.getMultiplicity());
    final Wizard1 w = new Wizard1();
    final World world = new World(1, 1);
    final EventListener listen = e -> {
      if (e instanceof CellEffectEvent) {
        final CellEffectEvent ce = (CellEffectEvent) e;
        if (ce.getEventType() == CellEffectType.REDRAW_CELL) {
          assertFalse(getRedraw());
          setRedraw();
        } else if (ce.getEventType() == CellEffectType.CORPSE_EXPLODE) {
          assertFalse(getCast());
          setCast();
        }
      }
    };
    final Lion l = new Lion();
    l.setState(State.DEAD);
    world.getCell(0).push(l);
    world.register(listen);
    x.cast(world, w, world.getCell(0), world.getCell(0));
    assertNull(world.actor(0));
    assertTrue(getRedraw());
    assertTrue(getCast());
    assertEquals(3, w.getScore());
    world.deregister(listen);
    l.setState(State.ACTIVE);
    world.getCell(0).push(l);
    x.cast(world, w, world.getCell(0), world.getCell(0));
    assertEquals(l, world.actor(0));
    assertEquals(3, w.getScore());
    x.cast(world, w, world.getCell(0), null);
    x.cast(world, w, null, world.getCell(0));
    x.cast(world, null, world.getCell(0), world.getCell(0));
    x.cast(null, w, world.getCell(0), world.getCell(0));
  }
}
