package chaos.common.free;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractGenerator;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.State;
import chaos.common.wizard.Wizard;
import chaos.util.CellEffectType;
import chaos.util.PolycellEffectEvent;

/**
 * Devastation.
 * @author Sean A. Irvine
 */
public class Devastation extends FreeCastable {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null) {
      int score = 0;
      // Make two complete passes to make sure we properly kill elementals.
      for (int k = 0; k < 2; ++k) {
        // This spell applies to every cell except empty cells, wizards, and generators.
        final HashSet<Cell> affected = new HashSet<>();
        for (int i = 0; i < world.size(); ++i) {
          final Actor a = world.actor(i);
          if (a != null && !(a instanceof Wizard) && !(a instanceof AbstractGenerator)) {
            affected.add(world.getCell(i));
          }
        }
        if (k == 0) {
          world.notify(new PolycellEffectEvent(affected, CellEffectType.FADE_TO_RED));
        }
        for (final Cell c : affected) {
          final Actor a = c.peek();
          // Although we checked for null above, reinstate of GoblinBomb etc.
          // can cause other cells to become null. See Bug#163.
          if (a != null) {
            score += a.getState() == State.DEAD ? 3 : a.getDefault(Attribute.LIFE);
            c.reinstate();
          }
        }
        world.notify(new PolycellEffectEvent(affected, CellEffectType.REDRAW_CELL));
      }
      if (caster instanceof Wizard) {
        ((Wizard) caster).addScore(score);
      }
    }
  }
}
