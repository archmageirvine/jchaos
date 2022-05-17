package chaos.common.growth;

import chaos.board.Cell;
import chaos.common.Attribute;
import chaos.common.MaterialGrowth;

/**
 * Green ooze.
 *
 * @author Sean A. Irvine
 */
public class GreenOoze extends MaterialGrowth {
  {
    setDefault(Attribute.LIFE, 8);
  }
  @Override
  public int getCastRange() {
    return 8;
  }
  @Override
  public int growthRate() {
    return 80;
  }
  @Override
  public int getDefaultWeight() {
    return 15;
  }
  @Override
  public int getGrowthType() {
    return GROW_OVER;
  }
  @Override
  public boolean canGrowOver(final Cell cell) {
    return true;
  }
}
