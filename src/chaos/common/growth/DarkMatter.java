package chaos.common.growth;

import chaos.board.Cell;
import chaos.common.Attribute;
import chaos.common.MaterialGrowth;
import chaos.common.Realm;
import chaos.common.State;

/**
 * Dark Matter.
 * @author Sean A. Irvine
 */
public class DarkMatter extends MaterialGrowth {

  {
    setDefault(Attribute.LIFE, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 3);
    setRealm(Realm.HYPERASTRAL);
  }

  @Override
  public int getCastRange() {
    return 12;
  }

  @Override
  public int growthRate() {
    return 60;
  }

  @Override
  public int getDefaultWeight() {
    return 0;
  }

  @Override
  public int getGrowthType() {
    return GROW_BY_COMBAT;
  }

  @Override
  protected int getGrowthCombat() {
    return 0; // actually can only grow into empty cells
  }

  @Override
  public boolean canGrowOver(final Cell cell) {
    return cell.peek() == null || cell.peek().getState() == State.DEAD;
  }
}
