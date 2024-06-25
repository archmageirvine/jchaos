package chaos.common.free;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.Tree;
import chaos.common.wizard.Wizard;
import chaos.util.AudioEvent;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.PolycellEffectEvent;

/**
 * Acid rain.
 *
 * @author Sean A. Irvine
 */
public class AcidRain extends FreeCastable {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null) {
      // Compute the set of cells to be affected, which consists of every cell
      // containing a tree.
      final HashSet<Cell> trees = new HashSet<>();
      for (int i = 0; i < world.size(); ++i) {
        final Cell c = world.getCell(i);
        if (c.peek() instanceof Tree) {
          trees.add(c);
        }
      }
      if (!trees.isEmpty()) {
        // At least one tree was found.  Call reinstate on each tree, to make sure
        // it is destroyed in accordance to the rules of the game.
        world.notify(new PolycellEffectEvent(trees, CellEffectType.GREEN_CIRCLE_EXPLODE));
        world.notify(new AudioEvent(casterCell, caster, "acid_rain")); // Use caster for proxy
        for (final Cell c : trees) {
          c.reinstate();
        }
        world.notify(new PolycellEffectEvent(trees, CellEffectType.REDRAW_CELL));
        // Award points only if at least one tree was destroyed.
        if (caster instanceof Wizard) {
          ((Wizard) caster).addScore(trees.size() << 2);
        }
      } else if (casterCell != null) {
        // Try to indicate spell failure in the case of no trees.
        casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.SPELL_FAILED));
      }
    }
  }
}
