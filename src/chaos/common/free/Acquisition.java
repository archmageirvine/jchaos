package chaos.common.free;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.State;
import chaos.common.wizard.Wizard;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Acquisition.
 *
 * @author Sean A. Irvine
 */
public class Acquisition extends FreeCastable {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  private static final Attribute[] PREFERRED_ATTRIBUTES = {
    Attribute.SPECIAL_COMBAT,
    Attribute.RANGED_COMBAT,
    Attribute.LIFE_RECOVERY,
    Attribute.COMBAT,
    Attribute.MOVEMENT,
    Attribute.LIFE,
    Attribute.MAGICAL_RESISTANCE,
    Attribute.INTELLIGENCE,
    Attribute.AGILITY,
  };

  private static final int THIEF_CONSTANT = 2;

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null && caster != null) {
      for (final Wizard w : world.getWizardManager().getWizards()) {
        if (w != null && w != caster && w.getState() == State.ACTIVE) {
          // Found a living wizard
          for (final Attribute a : PREFERRED_ATTRIBUTES) {
            if (w.get(a) > THIEF_CONSTANT) {
              // Found an attribute to steal
              w.decrement(a, THIEF_CONSTANT);
              caster.increment(a, THIEF_CONSTANT);
              if (a == Attribute.RANGED_COMBAT && w.get(Attribute.RANGE) > THIEF_CONSTANT) {
                w.decrement(Attribute.RANGE, THIEF_CONSTANT);
                caster.increment(Attribute.RANGE, THIEF_CONSTANT);
              }
              break; // steal at most one attribute
            }
          }
        }
      }
    }
    // Always do the effect on the caster
    if (casterCell != null) {
      casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.ACQUISITION, caster));
      casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.REDRAW_CELL));
    }
  }
}


