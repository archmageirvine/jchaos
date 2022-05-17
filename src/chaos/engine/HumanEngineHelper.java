package chaos.engine;

import java.util.Set;

import chaos.board.MoveMaster;
import chaos.board.Reachable;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.beam.Beam;
import chaos.common.wizard.Wizard;
import chaos.util.NameUtils;
import irvine.math.r.DoubleUtils;

/**
 * Helper routines for the human engine.
 *
 * @author Sean A. Irvine
 */
public final class HumanEngineHelper {

  private HumanEngineHelper() { }

  /**
   * Mark the actor as moved (if possible).
   * @param a actor to mark
   */
  static void setMoved(final Actor a) {
    if (a instanceof Monster) {
      a.setMoved(true);
    }
  }

  /**
   * Mark the actor as having shot.
   * @param a actor to mark
   */
  static void setShot(final Actor a) {
    if (a instanceof Monster) {
      ((Monster) a).incrementShotsMade();
    }
  }

  static int getCastRange(final Castable c, final Caster caster) {
    int range = c.getCastRange();
    if (caster instanceof Wizard && !(c instanceof FreeCastable) && !(c instanceof Beam)) {
      final Wizard wiz = (Wizard) caster;
      range += wiz.get(PowerUps.WAND);
      if (wiz.is(PowerUps.DEPTH) && !(c instanceof Actor)) {
        range = Castable.MAX_CAST_RANGE;
      }
    }
    return range;
  }

  static String describeMove(final World w, final MoveMaster mm, final Wizard wizard, final int cell) {
    final Monster m = (Monster) w.actor(cell);
    final StringBuilder sb = new StringBuilder();
    sb.append(NameUtils.getTextName(m)).append(": ");
    if (mm.isEngaged(wizard, cell)) {
      sb.append("engaged");
    } else {
      final double range = Math.sqrt(mm.getSquaredMovementPoints(wizard, cell));
      sb.append("movement points = ").append(DoubleUtils.NF2.format(range));
      if (m.is(PowerUps.FLYING)) {
        sb.append(" (flying)");
      }
    }
    return sb.toString();
  }

  static Set<Integer> possiblePlacesToMove(final World world, final Reachable reachable, final MoveMaster mm, final Wizard wizard, final int cell) {
    // todo temp replacement, better of get rid of this method all together
    // this should be same as old behaviour except showing full reachable for walkers
    final HumanMoveHelper hmh = new HumanMoveHelper(world);
    hmh.setTargets(world.actor(cell), cell, wizard, mm);
    return hmh.getPossiblePlacesToMove();
//    final Monster m = (Monster) world.actor(cell);
//    final Set<Integer> targets = new HashSet<>();
//    if (!mm.isEngaged(wizard, cell)) {
//      final int sqMovement = mm.getSquaredMovementPoints(wizard, cell);
//      final int range = m.is(PowerUps.FLYING) ? (int) Math.sqrt(sqMovement) : 1;
//      final int owner = wizard.getOwner();
//      final boolean isWizardMoving = world.actor(cell) instanceof Wizard;
//      for (final Cell c : m.is(PowerUps.FLYING) ? reachable.getReachableFlying(cell, Math.sqrt(sqMovement)) : world.getCells(cell, 1, range, false)) {
//        final int cn = c.getCellNumber();
//        if (world.getSquaredDistance(cell, cn) <= sqMovement) {
//          final Actor a = c.peek();
//          if (a == null || a.getState() == State.DEAD || mm.isAttackable(cell, cn)
//            || (isWizardMoving && mm.isMountable(wizard, cn))
//            || (a instanceof Gollop && owner == a.getOwner())) {
//            targets.add(cn);
//          }
//        }
//      }
//    } else {
//      for (final Cell c : world.getCells(cell, 1, 1, false)) {
//        final int cn = c.getCellNumber();
//        if (mm.isAttackable(cell, cn)) {
//          targets.add(cn);
//        }
//      }
//    }
//    return targets;
  }

}
