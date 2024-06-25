package chaos.common.growth;

import chaos.board.Cell;
import chaos.common.Attribute;
import chaos.common.MaterialGrowth;

/**
 * Gooey blob.
 * @author Sean A. Irvine
 */
public class GooeyBlob extends MaterialGrowth {
  {
    setDefault(Attribute.LIFE, 6);
    setDefault(Attribute.LIFE_RECOVERY, 3);
    setDefault(Attribute.MAGICAL_RESISTANCE, 29);
  }

  @Override
  public int getCastRange() {
    return 7;
  }

  @Override
  public long getLosMask() {
    return 0x3C3CFFFFFFFF3F3AL;
  }

  @Override
  public int growthRate() {
    return 70;
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
