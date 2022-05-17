package chaos.common.monster;

import chaos.common.AttacksUndead;
import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.MaterialMonster;
import chaos.common.Monster;

/**
 * Hippocrates.
 *
 * @author Sean A. Irvine
 */
public class Hippocrates extends MaterialMonster implements Humanoid, AttacksUndead {
  {
    setDefault(Attribute.LIFE, 16);
    setDefault(Attribute.LIFE_RECOVERY, 2);
    setDefault(Attribute.MAGICAL_RESISTANCE, 57);
    setDefault(Attribute.INTELLIGENCE, 100);
    setDefault(Attribute.COMBAT, -3);
    setDefault(Attribute.SPECIAL_COMBAT, -2);
    setDefault(Attribute.AGILITY, 7);
    setDefault(Attribute.MOVEMENT, 1);
  }
  @Override
  public long getLosMask() {
    return 0xFCFC743F3F3F3E3EL;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Druid.class;
  }
}
