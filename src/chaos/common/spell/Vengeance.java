package chaos.common.spell;

import java.util.HashSet;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Drainer;
import chaos.util.CellEffectType;

/**
 * Vengeance.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Vengeance extends Drainer {

  @Override
  public int getCastFlags() {
    return CAST_LIVING;
  }

  @Override
  public int getCastRange() {
    return MAX_CAST_RANGE;
  }

  @Override
  public Set<Cell> getAffectedCells(final World world, final Cell cell) {
    final Set<Cell> affected = new HashSet<>();
    affected.add(cell);
    return affected;
  }

  @Override
  public CellEffectType getEffectType() {
    return CellEffectType.SPUNGER;
  }

  @Override
  public int getDamage() {
    return 42;
  }
}
