package chaos.common.free;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractFreeIncrement;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.PowerUps;

/**
 * Talisman.
 *
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Talisman extends AbstractFreeIncrement {

  private Caster mCaster = null;

  @Override
  public int increment() {
    return mCaster == null || !mCaster.is(PowerUps.TALISMAN) ? 5 : 0;
  }

  @Override
  public Attribute attribute() {
    return Attribute.SPECIAL_COMBAT;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    mCaster = caster;
    super.cast(world, caster, casterCell);
    if (caster != null) {
      caster.increment(PowerUps.TALISMAN);
    }
  }
}
