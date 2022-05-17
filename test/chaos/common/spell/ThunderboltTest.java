package chaos.common.spell;

import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Monster;
import chaos.common.monster.Horse;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.EventListener;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class ThunderboltTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new Thunderbolt();
  }

  private boolean mCast = false;

  protected void setCast() {
    mCast = true;
  }

  protected boolean getCast() {
    return mCast;
  }

  private boolean mRedraw = false;

  protected void setRedraw() {
    mRedraw = true;
  }

  protected boolean getRedraw() {
    return mRedraw;
  }

  protected EventListener getListener() {
    return e -> {
      if (e instanceof CellEffectEvent && ((CellEffectEvent) e).getEventType() == CellEffectType.REDRAW_CELL) {
        assertFalse(getRedraw());
        setRedraw();
      } else if (e instanceof WeaponEffectEvent && ((WeaponEffectEvent) e).getEventType() == WeaponEffectType.THUNDERBOLT) {
        assertFalse(getCast());
        setCast();
      }
    };
  }

  public void testFlags() {
    final Castable x = getCastable();
    assertEquals(Castable.CAST_LIVING  | Castable.CAST_INANIMATE | Castable.CAST_GROWTH, x.getCastFlags());
    assertEquals(12, x.getCastRange());
  }

  public void testCast() {
    final Castable x = getCastable();
    final Wizard1 w = new Wizard1();
    final World world = new World(2, 1);
    final EventListener listen = getListener();
    world.getCell(0).push(w);
    world.register(listen);
    final Monster h = new Horse();
    world.getCell(1).push(h);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(1, h.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(1, h.get(Attribute.LIFE));
    assertEquals(1, h.get(Attribute.AGILITY));
    assertEquals(0, h.get(Attribute.RANGED_COMBAT));
    assertTrue(getRedraw());
    assertTrue(getCast());
    assertEquals(h, world.actor(1));
    world.deregister(listen);
    x.cast(world, w, world.getCell(0), null);
    x.cast(world, w, null, world.getCell(0));
    x.cast(world, null, world.getCell(0), world.getCell(0));
    x.cast(null, w, world.getCell(0), world.getCell(0));
  }
}
