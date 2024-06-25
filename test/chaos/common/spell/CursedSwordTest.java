package chaos.common.spell;

import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.monster.Hippocrates;
import chaos.common.monster.Vampire;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.EventListener;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class CursedSwordTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new CursedSword();
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
    final Castable x = new CursedSword();
    assertEquals(Castable.CAST_LIVING, x.getCastFlags());
    assertEquals(Castable.MAX_CAST_RANGE, x.getCastRange());
    final Wizard1 w = new Wizard1();
    final World world = new World(1, 1);
    final EventListener listen = e -> {
      if (e instanceof CellEffectEvent) {
        final CellEffectEvent ce = (CellEffectEvent) e;
        if (ce.getEventType() == CellEffectType.REDRAW_CELL) {
          assertFalse(getRedraw());
          setRedraw();
        } else if (ce.getEventType() == CellEffectType.SHIELD_DESTROYED) {
          assertFalse(getCast());
          setCast();
        }
      }
    };
    world.getCell(0).push(w);
    w.set(Attribute.COMBAT, 12);
    world.register(listen);
    x.cast(world, w, world.getCell(0), world.getCell(0));
    assertEquals(2, w.get(Attribute.COMBAT));
    assertTrue(getRedraw());
    assertTrue(getCast());
    world.deregister(listen);
    x.cast(world, w, world.getCell(0), world.getCell(0));
    assertEquals(0, w.get(Attribute.COMBAT));
    final Hippocrates h = new Hippocrates();
    h.set(Attribute.COMBAT, -11);
    world.getCell(0).push(h);
    x.cast(world, w, world.getCell(0), null);
    assertEquals(-1, h.get(Attribute.COMBAT));
    final Vampire v = new Vampire();
    v.set(Attribute.COMBAT, -6);
    world.getCell(0).push(v);
    x.cast(world, w, world.getCell(0), null);
    assertEquals(-6, v.get(Attribute.COMBAT));
    x.cast(world, w, null, world.getCell(0));
    x.cast(world, null, world.getCell(0), world.getCell(0));
    x.cast(null, w, world.getCell(0), world.getCell(0));
  }
}
