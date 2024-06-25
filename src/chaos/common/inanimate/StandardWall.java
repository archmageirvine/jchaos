package chaos.common.inanimate;

import chaos.common.AbstractWall;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Multiplicity;
import chaos.common.monster.StoneGolem;

/**
 * Standard wall.
 * @author Sean A. Irvine
 */
public class StandardWall extends AbstractWall implements Bonus, Multiplicity {
  {
    setDefault(Attribute.LIFE, 63);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
  }

  @Override
  public int getCastRange() {
    return 6;
  }

  @Override
  public int getBonus() {
    return 1;
  }

  @Override
  public Actor getAnimatedForm() {
    final Actor a = new StoneGolem();
    a.setOwner(getOwner());
    return a;
  }

  @Override
  public int getMultiplicity() {
    return 6;
  }
}
