package chaos.common.spell;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Drainer;
import chaos.util.CellEffectType;

/**
 * Consecrate.  Note, the word "consecrate" did appear in the Spectrum Chaos
 * code, but it was not actually implemented as a spell.
 *
 * @author Sean A. Irvine
 */
public class Consecrate extends Drainer {

  @Override
  public int getCastFlags() {
    return CAST_ANY;
  }
  @Override
  public int getCastRange() {
    return MAX_CAST_RANGE;
  }
  @Override
  public Set<Cell> getAffectedCells(final World world, final Cell cell) {
    final Set<Cell> affected = world.getCells(cell.getCellNumber(), 0, 1, false);
    affected.removeIf(c -> c.peek() == null);
    return affected;
  }
  @Override
  public CellEffectType getEffectType() {
    return CellEffectType.SPUNGER;
  }
  @Override
  public int getDamage() {
    return 12;
  }
}
