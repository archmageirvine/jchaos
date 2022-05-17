package chaos.common.free;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractFreeIncrement;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.PowerUps;

/**
 * Magic knife.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class MagicKnife extends AbstractFreeIncrement {

  @Override
  public int increment() {
    return 3;
  }

  @Override
  public Attribute attribute() {
    return Attribute.COMBAT;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (caster != null) {
      caster.set(PowerUps.SWORD, 1);
      caster.set(PowerUps.ATTACK_ANY_REALM, 1);
    }
    super.cast(world, caster, casterCell);
  }
}
