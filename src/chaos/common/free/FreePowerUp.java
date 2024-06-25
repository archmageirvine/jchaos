package chaos.common.free;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.PowerUp;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.PowerUpEvent;

/**
 * Convenience class for cast a free wizard power up.
 * @author Sean A. Irvine
 */
public abstract class FreePowerUp extends FreeCastable implements PowerUp {

  @Override
  public int getCastFlags() {
    return Castable.CAST_SINGLE;
  }

  /**
   * Defines the count used with this power up.  This is normally 1 but may
   * be overridden by certain spells, such as Double.
   * @return power up count
   */
  protected int getPowerUpCount() {
    return 1;
  }

  /**
   * Should this power up add to any existing count of this power up.
   * @return true to add
   */
  protected boolean cumulative() {
    return false;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (caster != null && world != null) {
      final int current = cumulative() ? caster.get(getPowerUpType()) : 0;
      caster.set(getPowerUpType(), current + getPowerUpCount());
      if (casterCell != null) {
        casterCell.notify(new PowerUpEvent(casterCell, caster, getPowerUpType()));
        casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.REDRAW_CELL));
      }
    }
  }
}
