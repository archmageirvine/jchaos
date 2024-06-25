package chaos.common.inanimate;

import chaos.common.AbstractWall;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Multiplicity;
import chaos.common.monster.MudMan;

/**
 * Weak wall.
 * @author Sean A. Irvine
 */
public class WeakWall extends AbstractWall implements Bonus, Multiplicity {

  {
    setDefault(Attribute.LIFE, 63);
  }

  @Override
  public int getCastFlags() {
    return CAST_GROWTH | CAST_EMPTY;
  }

  @Override
  public int getCastRange() {
    return MAX_CAST_RANGE;
  }

  @Override
  public int getBonus() {
    return 1;
  }

  @Override
  public Actor getAnimatedForm() {
    final Actor a = new MudMan();
    a.setOwner(getOwner());
    return a;
  }

  @Override
  public int getMultiplicity() {
    return 6;
  }
}
