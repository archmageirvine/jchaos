package chaos.common.free;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractFreeIncrement;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.PowerUps;
import chaos.common.wizard.Wizard;

/**
 * Magic bow.
 *
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class MagicBow extends AbstractFreeIncrement {

  @Override
  public int increment() {
    return 5;
  }

  @Override
  public Attribute attribute() {
    return Attribute.RANGE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (caster instanceof Wizard) {
      final Wizard w = (Wizard) caster;
      w.set(PowerUps.BOW, 1);
      w.set(PowerUps.ATTACK_ANY_REALM, 1);
      w.setRangedCombatApply(Attribute.LIFE);
      w.increment(Attribute.RANGED_COMBAT, 5);
      w.increment(Attribute.SHOTS, 1);
    }
    super.cast(world, caster, casterCell);
  }
}
