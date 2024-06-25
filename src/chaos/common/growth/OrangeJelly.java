package chaos.common.growth;

import chaos.board.Cell;
import chaos.common.Attribute;
import chaos.common.MaterialGrowth;

/**
 * Orange jelly.
 * @author Sean A. Irvine
 */
public class OrangeJelly extends MaterialGrowth {
  {
    setDefault(Attribute.LIFE, 4);
  }

  @Override
  public int getCastRange() {
    return 3;
  }

  @Override
  public long getLosMask() {
    return 0x0024747E7E7E7E7EL;
  }

  @Override
  public int growthRate() {
    return 100;
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
