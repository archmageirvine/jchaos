package chaos.common.spell;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.Realm;
import chaos.common.TargetFilter;
import chaos.common.wizard.Wizard;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Restore;

/**
 * Raise dead.
 *
 * @author Sean A. Irvine
 */
public class RaiseDead extends Castable implements TargetFilter {
  @Override
  public int getCastFlags() {
    return CAST_DEAD | CAST_LOS;
  }
  @Override
  public int getCastRange() {
    return 8;
  }
  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    if (cell != null) {
      final Actor raise = cell.peek();
      if (raise != null) {
        cell.notify(new CellEffectEvent(cell, CellEffectType.RAISE_DEAD, caster));
        if (caster != null) {
          raise.setOwner(caster.getOwner());
        }
        raise.setRealm(Realm.ETHERIC);
        Restore.restore(raise);
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      }
    }
    if (caster instanceof Wizard) {
      ((Wizard) caster).addScore(20);
    }
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    CastUtils.keepHighestScoring(CastUtils.preferAnimates(targets));
  }
}
