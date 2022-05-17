package chaos.common.beam;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * General implementation of all spells that affect cells in a straight beam
 * from the wizard to the edge of the world.
 *
 * @author Sean A. Irvine
 */
public abstract class Beam extends Castable {

  @Override
  public int getCastFlags() {
    return Castable.CAST_ANY;
  }

  @Override
  public int getCastRange() {
    return 1;
  }

  /**
   * Perform this beam's effect on a given cell.  This function is called once
   * for each cell in the path of the beam. Implementations can assume that the
   * passed cell is non-null.
   *
   * @param cell cell effected
   * @param caster the caster
   * @param casterCell cell containing the caster
   */
  public abstract void beamEffect(final Cell cell, final Caster caster, final Cell casterCell);

  /**
   * Get the attribute affected by this beam.
   *
   * @return an attribute
   */
  protected abstract Attribute getAttribute();

  private void produceEvent(final World world, final Caster caster, final int sourceCell, final int deltax, final int deltay) {
    // Determine the final cell
    final int width = world.width();
    int cx = sourceCell % width;
    int cy = sourceCell / width;
    Cell oldCell = null;
    Cell beamCell;
    while ((beamCell = world.getCell(cx, cy)) != null) {
      oldCell = beamCell;
      cx += deltax;
      cy += deltay;
    }
    // oldCell should now contain the last valid cell of the world hit by beam
    if (oldCell != null) {
      world.notify(new WeaponEffectEvent(sourceCell, oldCell.getCellNumber(), WeaponEffectType.PLASMA, caster, getAttribute()));
    }
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    // For safety carefully evaluate the arguments to ensure the beam cast is
    // valid.  Invalid beams are silently ignored.  To be valid cell and
    // casterCell must be adjacent and some of the parameters must be non-null.
    if (cell != null && casterCell != null && world != null) {
      final int c = cell.getCellNumber();
      final int d = casterCell.getCellNumber();
      // Checks cells are actually different.
      if (c != d) {
        final int width = world.width();
        final int dx = d % width;
        final int dy = d / width;
        int cx = c % width;
        int cy = c / width;
        final int deltax = cx - dx;
        final int deltay = cy - dy;
        // Check adjacency constraint.
        if (Math.abs(deltax) <= 1 && Math.abs(deltay) <= 1) {
          // If we get here, then the cell selection was valid.  It remains to
          // perform the effect on each cell in turn, while the cell exists.
          produceEvent(world, caster, d, deltax, deltay);
          Cell beamCell;
          while ((beamCell = world.getCell(cx, cy)) != null) {
            beamEffect(beamCell, caster, casterCell);
            cx += deltax;
            cy += deltay;
          }
        }
      }
    }
  }
}
