package chaos.common.spell;

import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.EventListener;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class PoisonDaggerTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new PoisonDagger();
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
    final Castable x = new PoisonDagger();
    assertEquals(Castable.CAST_LIVING, x.getCastFlags());
    assertEquals(11, x.getCastRange());
    final Wizard1 w = new Wizard1();
    final World world = new World(1, 1);
    final EventListener listen = e -> {
      if (e instanceof CellEffectEvent) {
        final CellEffectEvent ce = (CellEffectEvent) e;
        if (ce.getEventType() == CellEffectType.REDRAW_CELL) {
          assertFalse(getRedraw());
          setRedraw();
        } else if (ce.getEventType() == CellEffectType.POWERUP) {
          assertFalse(getCast());
          setCast();
        }
      }
    };
    world.getCell(0).push(w);
    world.register(listen);
    w.set(Attribute.INTELLIGENCE, 42);
    x.cast(world, w, world.getCell(0), world.getCell(0));
    assertEquals(5, w.get(Attribute.COMBAT));
    assertEquals(Attribute.LIFE_RECOVERY, w.getCombatApply());
    assertTrue(getRedraw());
    assertTrue(getCast());
    world.deregister(listen);
    x.cast(world, w, world.getCell(0), null);
    x.cast(world, w, null, world.getCell(0));
    x.cast(world, null, world.getCell(0), world.getCell(0));
    x.cast(null, w, world.getCell(0), world.getCell(0));
  }

}
