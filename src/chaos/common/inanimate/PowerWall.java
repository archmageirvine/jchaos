package chaos.common.inanimate;

import chaos.common.AbstractWall;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.NoFlyOver;
import chaos.common.monster.ClayGolem;

/**
 * Power wall.
 *
 * @author Sean A. Irvine
 */
public class PowerWall extends AbstractWall implements Bonus, NoFlyOver {
  {
    setDefault(Attribute.LIFE, 63);
    setDefault(Attribute.LIFE_RECOVERY, 63);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 1);
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
    final Actor a = new ClayGolem();
    a.setOwner(getOwner());
    return a;
  }
}
