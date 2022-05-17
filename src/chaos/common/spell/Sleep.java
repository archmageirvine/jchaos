package chaos.common.spell;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.State;
import chaos.common.TargetFilter;
import chaos.common.wizard.Wizard;
import chaos.util.AudioEvent;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * Sleep. The name of this spell appeared in Spectrum Chaos.
 * @author Sean A. Irvine
 */
public class Sleep extends Castable implements TargetFilter {
  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_LOS | CAST_GROWTH | CAST_NOASLEEP | CAST_NOWIZARDCELL;
  }

  @Override
  public int getCastRange() {
    return 15;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor a = cell.peek();
      if (!(a instanceof Wizard)) {
        if (casterCell != null) {
          cell.notify(new WeaponEffectEvent(casterCell, cell, WeaponEffectType.DEMOTION));
        }
        cell.notify(new AudioEvent(cell, a, "sleep", caster));
        cell.notify(new CellEffectEvent(cell, CellEffectType.WHITE_CIRCLE_EXPLODE));
        a.setOwner(0);
        a.setState(State.ASLEEP);
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.keepHighestScoring(CastUtils.dropWizardsOrConveyedWizards(CastUtils.preferAnimates(CastUtils.keepAwake(CastUtils.keepEnemies(targets, t, teams)))));
  }
}
