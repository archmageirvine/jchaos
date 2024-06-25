package chaos.common.growth;

import chaos.board.Cell;
import chaos.common.Attribute;
import chaos.common.MaterialGrowth;
import chaos.common.PowerUps;

/**
 * Flood.
 * @author Sean A. Irvine
 */
public class Flood extends MaterialGrowth {
  {
    setDefault(Attribute.LIFE, 8);
    setDefault(Attribute.MAGICAL_RESISTANCE, 1);
  }

  @Override
  public int getCastRange() {
    return 5;
  }

  @Override
  public int growthRate() {
    return 40;
  }

  @Override
  public int getDefaultWeight() {
    return 15;
  }

  @Override
  public int getGrowthType() {
    return GROW_BY_COMBAT;
  }

  @Override
  public boolean canGrowOver(final Cell cell) {
    // Only exception is flood shield
    return cell.peek() == null || !cell.peek().is(PowerUps.FLOOD_SHIELD);
  }
}
