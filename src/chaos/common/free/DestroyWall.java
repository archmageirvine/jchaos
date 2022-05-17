package chaos.common.free;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.Wall;
import chaos.common.wizard.Wizard;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.PolycellEffectEvent;

/**
 * Destroy wall.
 *
 * @author Sean A. Irvine
 */
public class DestroyWall extends FreeCastable {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null) {
      // Compute the set of cells to be affected, which consists of every cell
      // containing a wall.
      final HashSet<Cell> walls = new HashSet<>();
      for (int i = 0; i < world.size(); ++i) {
        final Cell c = world.getCell(i);
        if (c.peek() instanceof Wall) {
          walls.add(c);
        }
      }
      if (!walls.isEmpty()) {
        // At least one wall was found.  Call reinstate on each wall, to make sure
        // it is destroyed in accordance to the rules of the game.
        world.notify(new PolycellEffectEvent(walls, CellEffectType.GREEN_CIRCLE_EXPLODE));
        for (final Cell c : walls) {
          c.reinstate();
        }
        world.notify(new PolycellEffectEvent(walls, CellEffectType.REDRAW_CELL));
        // Award points only if at least one wall was destroyed.
        if (caster instanceof Wizard) {
          ((Wizard) caster).addScore(walls.size() << 2);
        }
      } else if (casterCell != null) {
        // Try to indicate spell failure in the case of no walls.
        casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.SPELL_FAILED));
      }
    }
  }
}
