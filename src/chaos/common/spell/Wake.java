package chaos.common.spell;

import java.util.Iterator;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.State;
import chaos.common.TargetFilter;
import chaos.common.wizard.Wizard;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * Wake.
 * @author Sean A. Irvine
 */
public class Wake extends Castable implements TargetFilter {
  @Override
  public int getCastFlags() {
    return CAST_LIVING | CAST_LOS | CAST_GROWTH | CAST_INANIMATE | CAST_NOWIZARDCELL;
  }

  @Override
  public int getCastRange() {
    return 15;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor a = cell.peek();
      if (a != null && !(a instanceof Wizard) && a.getState() == State.ASLEEP) {
        if (casterCell != null) {
          cell.notify(new WeaponEffectEvent(casterCell, cell, WeaponEffectType.PROMOTION));
        }
        cell.notify(new CellEffectEvent(cell, CellEffectType.WHITE_CIRCLE_EXPLODE));
        a.setOwner(caster.getOwner());
        a.setState(State.ACTIVE);
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      }
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    for (final Iterator<Cell> it = targets.iterator(); it.hasNext(); ) {
      final Cell c = it.next();
      final Actor a = c.peek();
      if (a == null || a.getState() != State.ASLEEP) {
        it.remove();
      }
    }
    CastUtils.keepHighestScoring(targets);
  }
}
