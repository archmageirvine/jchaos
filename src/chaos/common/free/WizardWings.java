package chaos.common.free;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractFreeIncrement;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.PowerUps;

/**
 * Wizard wings.
 *
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class WizardWings extends AbstractFreeIncrement {

  @Override
  public int increment() {
    return 5;
  }

  @Override
  public Attribute attribute() {
    return Attribute.MOVEMENT;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (caster != null) {
      caster.set(PowerUps.FLYING, 1);
    }
    super.cast(world, caster, casterCell);
  }
}
