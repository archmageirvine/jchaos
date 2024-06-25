package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.BowShooter;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Rider;
import chaos.common.Unicaster;
import chaos.common.inanimate.DarkWood;

/**
 * Dark elf.
 * @author Sean A. Irvine
 */
public class DarkElf extends Unicaster implements Humanoid, BowShooter, Rider {
  {
    mDelay = 30;
    mCastClass = DarkWood.class;
    setDefault(Attribute.LIFE, 38);
    setDefault(Attribute.LIFE_RECOVERY, 12);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 24);
    setDefault(Attribute.INTELLIGENCE, 77);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.AGILITY, 80);
    setDefault(Attribute.MOVEMENT, 1);
    setDefault(Attribute.RANGED_COMBAT, 6);
    setDefault(Attribute.RANGE, 12);
    set(PowerUps.ARCHERY, 1);
    set(PowerUps.REFLECT, 1);
    set(PowerUps.ATTACK_ANY_REALM, 1);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }

  @Override
  public long getLosMask() {
    return 0x00387C3838181800L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return GrayElf.class;
  }
}
