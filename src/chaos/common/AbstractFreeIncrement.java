package chaos.common;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.wizard.Wizard;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.ShieldGrantedEvent;

/**
 * Abstract superclass for freely castable increment spell.
 *
 * @author Sean A. Irvine
 */
public abstract class AbstractFreeIncrement extends FreeCastable {

  /**
   * The increment to be applied.
   *
   * @return increment
   */
  public abstract int increment();

  /**
   * The attribute to be incremented.
   *
   * @return attribute
   */
  public abstract Attribute attribute();

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (caster instanceof Wizard) {
      final Attribute at = attribute();
      final int oldValue = caster.get(at);
      caster.increment(at, increment());
      if (caster.get(at) == oldValue) {
        // Value was not changed, used recovery instead
        final Attribute attr = at.recovery();
        if (attr != null) {
          if (casterCell != null) {
            casterCell.notify(new ShieldGrantedEvent(casterCell, caster, attr));
          }
          caster.increment(attr, 1);
        }
      } else if (casterCell != null) {
        casterCell.notify(new ShieldGrantedEvent(casterCell, caster, at));
      }
      if (casterCell != null) {
        casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.REDRAW_CELL));
      }
    }
  }
}
