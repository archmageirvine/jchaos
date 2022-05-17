package chaos.common.mythos;

import chaos.common.Attribute;
import chaos.common.Monster;
import chaos.common.MythosMonster;
import chaos.common.monster.Robot;

/**
 * Cylinder.
 * @author Sean A. Irvine
 */
public class Cylinder extends MythosMonster {
  {
    setDefault(Attribute.LIFE, 60);
    setDefault(Attribute.LIFE_RECOVERY, 20);
    setDefault(Attribute.MAGICAL_RESISTANCE, 57);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 12);
    setDefault(Attribute.AGILITY, 50);
    setDefault(Attribute.INTELLIGENCE, 2);
    setDefault(Attribute.MOVEMENT, 4);
    setDefault(Attribute.COMBAT, 14);
  }

  @Override
  public long getLosMask() {
    return 0x387E7E7E7E7E7C38L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Robot.class;
  }
}
