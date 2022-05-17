package chaos.common.spell;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractDecrement;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.ShieldDestroyedEvent;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * Bow breaker.
 *
 * @author Sean A. Irvine
 */
public class BowBreaker extends AbstractDecrement {

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_LOS;
  }
  @Override
  public int getCastRange() {
    return 9;
  }
  @Override
  public int decrement() {
    return Attribute.RANGE.max();
  }

  @Override
  public Attribute attribute() {
    return Attribute.RANGE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor a = cell.peek();
      if (a != null) {
        if (casterCell != null) {
          cell.notify(new WeaponEffectEvent(casterCell, cell, WeaponEffectType.BALL, caster, Attribute.RANGE));
        }
        cell.notify(new ShieldDestroyedEvent(cell, caster, Attribute.RANGE));
        a.decrement(Attribute.RANGE, decrement());
        a.decrement(Attribute.RANGED_COMBAT, decrement());
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      }
    }
  }
}
