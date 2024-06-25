package chaos.common.inanimate;

import chaos.common.AbstractWall;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Multiplicity;
import chaos.common.monster.BasaltGolem;

/**
 * Strong wall.
 * @author Sean A. Irvine
 */
public class StrongWall extends AbstractWall implements Bonus, Multiplicity {
  {
    setDefault(Attribute.LIFE, 63);
    setDefault(Attribute.LIFE_RECOVERY, 7);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
  }

  @Override
  public int getCastRange() {
    return 11;
  }

  @Override
  public int getBonus() {
    return 2;
  }

  @Override
  public Actor getAnimatedForm() {
    final Actor a = new BasaltGolem();
    a.setOwner(getOwner());
    return a;
  }

  @Override
  public int getMultiplicity() {
    return 4;
  }
}
