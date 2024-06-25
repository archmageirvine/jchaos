package chaos.common.spell;

import chaos.common.Castable;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.EventListener;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class FireballTest extends LightningTest {

  @Override
  public Castable getCastable() {
    return new Fireball();
  }

  @Override
  protected int getDamage() {
    return 60;
  }

  @Override
  public void testFlags() {
    final Castable x = getCastable();
    assertEquals(Castable.CAST_LIVING | Castable.CAST_DEAD | Castable.CAST_INANIMATE | Castable.CAST_LOS | Castable.CAST_GROWTH, x.getCastFlags());
    assertEquals(8, x.getCastRange());
  }

  @Override
  protected EventListener getListener() {
    return e -> {
      if (e instanceof CellEffectEvent && ((CellEffectEvent) e).getEventType() == CellEffectType.REDRAW_CELL) {
        assertFalse(getRedraw());
        setRedraw();
      } else if (e instanceof WeaponEffectEvent && ((WeaponEffectEvent) e).getEventType() == WeaponEffectType.FIREBALL) {
        assertFalse(getCast());
        setCast();
      }
    };
  }
}
