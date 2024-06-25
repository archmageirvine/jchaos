package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Mud man.
 * @author Sean A. Irvine
 */
public class MudMan extends MaterialMonster implements Humanoid {
  {
    setDefault(Attribute.LIFE, 32);
    setDefault(Attribute.MAGICAL_RESISTANCE, 14);
    setDefault(Attribute.AGILITY, 7);
    setDefault(Attribute.INTELLIGENCE, 13);
    setDefault(Attribute.MOVEMENT, 1);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.RANGE, 4);
    setDefault(Attribute.RANGED_COMBAT, 8);
  }

  @Override
  public long getLosMask() {
    return 0x003CFFFFFFFF7E7EL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return OgreMage.class;
  }
}
