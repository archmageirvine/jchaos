package chaos.common.spell;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.TargetFilter;
import chaos.common.wizard.Wizard;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Random;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * Subversion.
 *
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Subversion extends Castable implements TargetFilter {

  /**
   * The larger this number the more likely the abduction on a given creature
   * will succeed.  It should be larger than MAX_VALUE to ensure that even the
   * most intelligent creatures have some chance of succumbing.
   */
  private static final int SUBVERSION_FACTOR = Attribute.INTELLIGENCE.max() + 20;

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_LOS | CAST_GROWTH | CAST_NOWIZARDCELL;
  }

  @Override
  public int getCastRange() {
    return 11;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null && caster != null) {
      final Actor current = cell.peek();
      // Draw the weapon effect, irrespective of success/failure of the cast
      if (casterCell != null) {
        cell.notify(new WeaponEffectEvent(casterCell, cell, WeaponEffectType.BRAIN_BEAM_EVENT));
      }
      // Higher level wizards have move chance of successful subversion
      final int subversionFactor = SUBVERSION_FACTOR + 25 * caster.get(PowerUps.LEVEL);
      // In theory it is not possible to cast this spell on empty cells or
      // cells containing a wizard, but for robustness we check anyway.
      if (current instanceof Monster && !(current instanceof Wizard) && cell.getMount() == null && Random.nextInt(subversionFactor) > current.get(Attribute.INTELLIGENCE)) {
        // Subversion succeeds
        cell.notify(new CellEffectEvent(cell, CellEffectType.OWNER_CHANGE));
        current.setOwner(caster.getOwner());
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      } else {
        // Failed (for any of several possible reasons).
        cell.notify(new CellEffectEvent(cell, CellEffectType.SPELL_FAILED));
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.keepHighestSubversionScoring(CastUtils.dropWizardsOrConveyedWizards(CastUtils.keepEnemies(targets, t, teams)));
  }
}
