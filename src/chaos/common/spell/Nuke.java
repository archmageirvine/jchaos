package chaos.common.spell;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.State;
import chaos.common.TargetFilter;
import chaos.common.inanimate.Nuked;
import chaos.common.wizard.Wizard;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Nuke.
 *
 * @author Sean A. Irvine
 */
public class Nuke extends Castable implements TargetFilter {

  private static final int SECONDARY_NUKE_DAMAGE = 10;

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_GROWTH | CAST_INANIMATE | CAST_NOEXPOSEDWIZARD;
  }
  @Override
  public int getCastRange() {
    return MAX_CAST_RANGE;
  }
  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null && caster != null) {
      if (cell.getMount() == null) {
        final Actor a = cell.peek();
        if (!(a instanceof Wizard)) {
          if (a != null) {
            CastUtils.handleScoreAndBonus(caster, a, casterCell);
            cell.notify(new CellEffectEvent(cell, CellEffectType.BOMB));
            if (world != null) {
              for (final Cell c : world.getCells(cell.getCellNumber(), 1, 1, false)) {
                final Actor aa = c.peek();
                if (aa != null) {
                  if (aa.getState() == State.DEAD || aa.decrement(Attribute.LIFE, SECONDARY_NUKE_DAMAGE)) {
                    c.reinstate();
                  }
                }
              }
            }
            // Nothing survives a nuke!
            // Reinstate still needed to handle case of Dead Revenge generator
            cell.reinstate();
            cell.pop();
            cell.pop();
          }
          final Nuked r = new Nuked();
          r.setOwner(caster.getOwner());
          cell.push(r);
          cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
        }
      } else {
        cell.notify(new CellEffectEvent(cell, CellEffectType.SPELL_FAILED));
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.keepHighestScoring(CastUtils.dropWizards(CastUtils.keepEnemies(targets, t, teams)));
  }
}
