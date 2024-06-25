package chaos.common.free;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.State;
import chaos.common.wizard.Wizard;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Random;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * Pyrotechnics.
 *
 * @author Sean A. Irvine
 * @author Stephen Smith
 */
public class Pyrotechnics extends FreeCastable {

  /** Damage inflicted by each firework. */
  private static final int DAMAGE = 4;
  /** Radius of cells affected. */
  private static final int RADIUS = 4;
  /** Number of fireworks. */
  private static final int COUNT = 15;
  /** Weapon effects for pyrotechnics. */
  private static final WeaponEffectType[] WEAPONS = {
    WeaponEffectType.LIGHTNING,
    WeaponEffectType.ICE_BEAM,
    WeaponEffectType.FIREBALL,
    WeaponEffectType.BALL,
    WeaponEffectType.SPINNER,
  };

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null && casterCell != null) {
      // Get all cells within the specified maximum radius.
      final Set<Cell> tt = world.getCells(casterCell.getCellNumber(), 1, RADIUS, false);
      final Cell[] targets = tt.toArray(new Cell[0]);
      if (targets.length > 0) {
        // At least one target cell exists -- should actually always be true.
        for (int k = 0; k < COUNT; ++k) {
          final Cell t = targets[Random.nextInt(targets.length)];
          t.notify(new WeaponEffectEvent(casterCell, t, WEAPONS[Random.nextInt(WEAPONS.length)], this));
          final Actor a = t.peek();
          if (a != null) {
            if (a.getState() == State.DEAD) {
              t.notify(new CellEffectEvent(t, CellEffectType.CORPSE_EXPLODE));
              t.reinstate();
              if (caster instanceof Wizard) {
                ((Wizard) caster).addScore(5);
              }
            } else {
              t.notify(new CellEffectEvent(t, CellEffectType.WHITE_CIRCLE_EXPLODE));
              if (DAMAGE < a.get(Attribute.LIFE)) {
                a.set(Attribute.LIFE, a.get(Attribute.LIFE) - DAMAGE);
              } else {
                CastUtils.handleScoreAndBonus(caster, a, casterCell);
                t.reinstate();
              }
            }
            t.notify(new CellEffectEvent(t, CellEffectType.REDRAW_CELL));
          }
        }
      }
    }
  }
}
