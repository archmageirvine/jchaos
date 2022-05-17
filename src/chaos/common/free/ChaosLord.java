package chaos.common.free;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.PowerUps;
import chaos.common.wizard.Wizard;
import chaos.util.AudioEvent;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;

/**
 * Chaos Lord.
 *
 * @author Sean A. Irvine
 */
public class ChaosLord extends FreeCastable {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (caster != null) {
      if (casterCell != null) {
        casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.SHIELD_GRANTED));
        casterCell.notify(new AudioEvent(casterCell, caster, "chaos_lord"));
        casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.REDRAW_CELL));
      }
      caster.set(Attribute.LIFE, Attribute.LIFE.max());
      caster.set(Attribute.INTELLIGENCE, Attribute.INTELLIGENCE.max());
      caster.set(Attribute.MAGICAL_RESISTANCE, Attribute.MAGICAL_RESISTANCE.max());
      caster.set(Attribute.AGILITY, Attribute.AGILITY.max());
      caster.increment(Attribute.RANGE, 15);
      caster.set(Attribute.MOVEMENT, Attribute.MOVEMENT.max());
      caster.increment(Attribute.COMBAT, 15);
      caster.increment(Attribute.RANGED_COMBAT, 15);
      caster.increment(Attribute.LIFE_RECOVERY, 7);
      caster.increment(Attribute.MAGICAL_RESISTANCE_RECOVERY, 7);
      caster.increment(Attribute.INTELLIGENCE_RECOVERY, 7);
      caster.increment(Attribute.AGILITY_RECOVERY, 7);
      caster.set(Attribute.SHOTS, Attribute.SHOTS.max());
      caster.setCombatApply(Attribute.LIFE);
      caster.setRangedCombatApply(Attribute.LIFE);
      caster.set(PowerUps.FLYING, 1);
      if (caster instanceof Wizard) {
        final Wizard w = (Wizard) caster;
        w.set(PowerUps.BOW, 1);
        w.set(PowerUps.FLYING, 1);
        w.set(PowerUps.REFLECT, 1);
        w.set(PowerUps.FIRE_SHIELD, 1);
        w.set(PowerUps.FLOOD_SHIELD, 1);
        w.set(PowerUps.EARTHQUAKE_SHIELD, 1);
        w.set(PowerUps.ATTACK_ANY_REALM, 1);
        w.set(PowerUps.TRIPLE, 6);
      }
    }
  }
}
