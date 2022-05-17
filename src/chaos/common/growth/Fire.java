package chaos.common.growth;

import chaos.board.Cell;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.MaterialGrowth;
import chaos.common.PowerUps;
import chaos.common.Promotion;

/**
 * Fire.
 *
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Fire extends MaterialGrowth implements Promotion {
  {
    setDefault(Attribute.LIFE, 10);
    setDefault(Attribute.MAGICAL_RESISTANCE, 3);
  }

  @Override
  public int getCastRange() {
    return 6;
  }

  @Override
  public int growthRate() {
    return 50;
  }

  @Override
  public int getDefaultWeight() {
    return -5;
  }

  @Override
  public int getGrowthType() {
    return GROW_BY_COMBAT;
  }

  @Override
  public boolean canGrowOver(final Cell cell) {
    // Only exception is fire shield
    return cell.peek() == null || !cell.peek().is(PowerUps.FIRE_SHIELD);
  }

  @Override
  public Class<? extends Actor> promotion() {
    return Balefire.class;
  }

  @Override
  public int promotionCount() {
    return 1000;
  }
}
