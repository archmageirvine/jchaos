package chaos.common.spell;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.Multiplicity;
import chaos.common.State;
import chaos.common.TargetFilter;
import chaos.common.wizard.Wizard;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Bury.
 *
 * @author Sean A. Irvine
 */
public class Bury extends Castable implements Multiplicity, TargetFilter {
  @Override
  public int getCastFlags() {
    return CAST_DEAD;
  }
  @Override
  public int getCastRange() {
    return MAX_CAST_RANGE;
  }
  @Override
  public int getMultiplicity() {
    return 8;
  }
  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor a = cell.peek();
      if (a != null && a.getState() == State.DEAD) {
        // In theory every cast will satisfy these constraints, but it doesn't
        // hurt to make sure
        cell.notify(new CellEffectEvent(cell, CellEffectType.CORPSE_EXPLODE));
        cell.reinstate();
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
        if (caster instanceof Wizard) {
          ((Wizard) caster).addScore(3);
        }
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    CastUtils.keepHighestScoring(CastUtils.preferAnimates(targets));
  }
}
