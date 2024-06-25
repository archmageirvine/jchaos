package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.BowShooter;
import chaos.common.Humanoid;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Promotion;
import chaos.common.Rider;

/**
 * Gray elf.
 * @author Sean A. Irvine
 */
public class GrayElf extends MaterialMonster implements Humanoid, BowShooter, Promotion, Rider {
  {
    setDefault(Attribute.LIFE, 8);
    setDefault(Attribute.LIFE_RECOVERY, 3);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 14);
    setDefault(Attribute.INTELLIGENCE, 67);
    setDefault(Attribute.COMBAT, 1);
    setDefault(Attribute.AGILITY, 40);
    setDefault(Attribute.MOVEMENT, 1);
    setDefault(Attribute.RANGED_COMBAT, 4);
    setDefault(Attribute.RANGE, 6);
    set(PowerUps.ARCHERY, 1);
  }

  @Override
  public int getCastRange() {
    return 2;
  }

  @Override
  public long getLosMask() {
    return 0x00387C3838181800L;
  }

  @Override
  public Class<? extends Monster> promotion() {
    return DarkElf.class;
  }

  @Override
  public int promotionCount() {
    return 4;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return WoodElf.class;
  }
}
