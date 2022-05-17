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
import chaos.common.wizard.Wizard;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Clone;
import chaos.util.Random;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * Simulacrum.
 *
 * @author Sean A. Irvine
 */
public class Simulacrum extends Castable implements TargetFilter {

  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_INANIMATE | CAST_LOS | CAST_GROWTH | CAST_NOWIZARDCELL;
  }
  @Override
  public int getCastRange() {
    return 8;
  }
  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor a = cell.peek();
      if (a != null && a.getState() != State.DEAD && !(a instanceof Wizard)) {
        Cell choice = null;
        int count = 0;
        for (final Cell c : world.getCells(cell.getCellNumber(), 1, 1, false)) {
          final Actor t = c.peek();
          if ((t == null || t.getState() == State.DEAD) && Random.nextInt(++count) == 0) {
            choice = c;
          }
        }
        if (choice != null) {
          // There is a suitable cell, form the clone, and insert.
          final Actor clone = Clone.clone(a);
          clone.set(Attribute.LIFE, Math.min(6, clone.get(Attribute.LIFE)));
          final int cc = choice.getCellNumber();
          choice.notify(new WeaponEffectEvent(cell.getCellNumber(), cc, WeaponEffectType.MONSTER_CAST_EVENT, clone));
          choice.notify(new CellEffectEvent(cc, CellEffectType.MONSTER_CAST_EVENT, clone));
          choice.push(clone);
        } else {
          // There was no spare cell for the replicant
          cell.notify(new CellEffectEvent(cell, CellEffectType.SPELL_FAILED));
        }
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    final Team teams = world.getTeamInformation();
    final int t = teams.getTeam(caster);
    CastUtils.keepHighestScoring(CastUtils.dropWizardsOrConveyedWizards(CastUtils.preferAnimates(CastUtils.keepFriends(targets, t, teams))));
  }
}
