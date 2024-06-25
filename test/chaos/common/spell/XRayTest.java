package chaos.common.spell;

import chaos.common.Castable;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.EventListener;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class XRayTest extends LightningTest {

  @Override
  public Castable getCastable() {
    return new XRay();
  }

  @Override
  public void testFlags() {
    final Castable x = getCastable();
    assertEquals(Castable.CAST_LIVING | Castable.CAST_INANIMATE | Castable.CAST_LOS | Castable.CAST_GROWTH, x.getCastFlags());
    assertEquals(9, x.getCastRange());
  }

  @Override
  protected EventListener getListener() {
    return e -> {
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
  }
}
