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
 *
 * @author Sean A. Irvine
 */
public class SleepTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new Sleep();
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
    final Sleep x = new Sleep();
    assertEquals(Castable.CAST_LIVING | Castable.CAST_LOS | Castable.CAST_GROWTH | Castable.CAST_NOASLEEP | Castable.CAST_NOWIZARDCELL, x.getCastFlags());
    assertEquals(15, x.getCastRange());
    final Wizard1 w = new Wizard1();
    final Lion l = new Lion();
    w.setState(State.ACTIVE);
    l.setOwner(2);
    final World world = new World(1, 1);
    final EventListener listen = e -> {
      if (e instanceof CellEffectEvent) {
        final CellEffectEvent ce = (CellEffectEvent) e;
        if (ce.getEventType() == CellEffectType.REDRAW_CELL) {
          assertFalse(getRedraw());
          setRedraw();
        } else if (ce.getEventType() == CellEffectType.WHITE_CIRCLE_EXPLODE) {
          assertFalse(getCast());
          setCast();
        }
      }
    };
    world.getCell(0).push(l);
    world.register(listen);
    x.cast(world, w, world.getCell(0), world.getCell(0));
    assertEquals(State.ASLEEP, l.getState());
    assertTrue(getRedraw());
    assertTrue(getCast());
    assertEquals(0, l.getOwner());
    world.deregister(listen);
    x.cast(world, w, world.getCell(0), null);
    x.cast(world, w, null, world.getCell(0));
    x.cast(world, null, world.getCell(0), world.getCell(0));
    x.cast(null, w, world.getCell(0), world.getCell(0));
  }

}
