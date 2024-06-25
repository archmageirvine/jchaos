package chaos.board;

import chaos.common.Actor;
import chaos.common.AttacksUndead;
import chaos.common.Attribute;
import chaos.common.Conveyance;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Realm;
import chaos.common.State;
import chaos.common.wizard.Wizard;

/**
 * Encapsulates the rules for ranged combat.
 * @author Sean A. Irvine
 */
final class ShootingRules {

  private ShootingRules() {
  }

  static boolean isShootable(final Wizard player, final Actor a) {
    return a instanceof Monster
      && a.getState() == State.ACTIVE
      && a.getOwner() == player.getOwner()
      && a.get(Attribute.RANGE) > 0
      && ((Monster) a).getShotsMade() < a.get(Attribute.SHOTS);
  }

  static boolean isShootableConveyance(final Wizard player, final Actor a) {
    return a instanceof Conveyance && isShootable(player, ((Conveyance) a).getMount());
  }

  static boolean isShootable(final int source, final int target, final Monster m, final World world, final LineOfSight los) {
    // compute (range+0.5)^2 without resorting to floating point
    final int rangeSquared = m.get(Attribute.RANGE) * m.get(Attribute.RANGE) + m.get(Attribute.RANGE);
    if (rangeSquared < world.getSquaredDistance(source, target)) {
      return false;
    }
    if (!m.is(PowerUps.ARCHERY) && !los.isLOS(source, target)) {
      return false;
    }
    // Special check for confidence
    final Wizard w = world.getWizardManager().getWizard(m.getOwner());
    if (w != null && w.is(PowerUps.CONFIDENCE)) {
      return true;
    }
    final Actor at = world.actor(target);
    return at == null || m instanceof AttacksUndead && at.getRealm() == Realm.ETHERIC || Realm.realmCheck(m, at);
  }

}
