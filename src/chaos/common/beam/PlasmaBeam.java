package chaos.common.beam;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Caster;
import chaos.common.State;
import chaos.common.TargetFilter;
import chaos.common.wizard.Wizard;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Random;

/**
 * Plasma beam.
 * @author Sean A. Irvine
 */
public class PlasmaBeam extends Beam implements TargetFilter {

  /** Damage inflicted by this beam weapon. */
  private static final int DAMAGE = 45;

  @Override
  protected Attribute getAttribute() {
    return Attribute.MAGICAL_RESISTANCE;
  }

  @Override
  public void beamEffect(final Cell cell, final Caster caster, final Cell casterCell) {
    final Actor a = cell.peek();
    if (a != null) {
      if (a.getState() == State.DEAD) {
        // Target is a corpse, explode it and give a few points to the caster.
        cell.reinstate();
        if (caster instanceof Wizard) {
          ((Wizard) caster).addScore(5);
        }
      } else {
        final int mr = a.get(Attribute.MAGICAL_RESISTANCE) - DAMAGE;
        if (mr > 0) {
          // survived
          a.set(Attribute.MAGICAL_RESISTANCE, mr);
        } else {
          // no mr
          a.set(Attribute.MAGICAL_RESISTANCE, 0);
          if (-mr >= a.get(Attribute.LIFE)) {
            CastUtils.handleScoreAndBonus(caster, a, casterCell);
            cell.reinstate();
          } else {
            a.set(Attribute.LIFE, a.get(Attribute.LIFE) + mr);
          }
        }
      }
    }
    cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int owner = caster.getOwner();
    final int tm = teams.getTeam(owner);
    // This spell is unusual in that it completely eliminates the given
    // targets and reselects based on the following
    targets.clear();
    final Cell cc = world.getCell(caster);
    if (cc == null) {
      return;
    }
    final int casterCell = cc.getCellNumber();
    final int[] cxy = new int[2];
    world.getCellCoordinates(casterCell, cxy);
    final int[] xy = new int[2];
    Cell bestCell = null;
    int bestScore = Integer.MIN_VALUE;
    int count = 1;
    for (final Cell c : world.getCells(casterCell, 1, 1, false)) {
      world.getCellCoordinates(c.getCellNumber(), xy);
      int x = xy[0];
      int y = xy[1];
      final int dx = x - cxy[0];
      final int dy = y - cxy[1];
      Cell t = c;
      int score = 0;
      do {
        final Actor a = t.peek();
        if (a != null && a.getState() != State.DEAD) {
          final int ao = a.getOwner();
          if (ao == owner) {
            score -= 2;
          } else if (teams.getTeam(ao) == tm) {
            --score;
          } else if (a instanceof Bonus) {
            score += 7;
          } else {
            score += 4;
          }
        }
        x += dx;
        y += dy;
        t = world.getCell(x, y);
      } while (t != null);
      if (score > bestScore) {
        bestScore = score;
        bestCell = c;
        count = 1;
      } else if (score == bestScore && Random.nextInt(++count) == 0) {
        bestCell = c;
      }
    }
    if (bestCell != null) {
      targets.add(bestCell);
    }
  }

}
