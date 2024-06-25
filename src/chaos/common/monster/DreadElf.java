package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.BowShooter;
import chaos.common.Humanoid;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Rider;

/**
 * Dread elf.
 * @author Sean A. Irvine
 */
public class DreadElf extends MaterialMonster implements Bonus, Humanoid, BowShooter, Rider {
  {
    setDefault(Attribute.LIFE, 39);
    setDefault(Attribute.LIFE_RECOVERY, 4);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 21);
    setDefault(Attribute.INTELLIGENCE, 93);
    setDefault(Attribute.COMBAT, 6);
    setDefault(Attribute.AGILITY, 53);
    setDefault(Attribute.MOVEMENT, 2);
    setDefault(Attribute.RANGED_COMBAT, 9);
    setDefault(Attribute.RANGE, 6);
    set(PowerUps.ARCHERY, 1);
  }

  @Override
  public int getBonus() {
    return 1;
  }

  @Override
  public long getLosMask() {
    return 0x003C3E1C1C181800L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return WoodElf.class;
  }
}
