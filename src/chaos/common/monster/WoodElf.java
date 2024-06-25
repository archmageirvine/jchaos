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
 * Wood elf.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class WoodElf extends MaterialMonster implements Promotion, Humanoid, BowShooter, Rider {
  {
    setDefault(Attribute.LIFE, 6);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 7);
    setDefault(Attribute.INTELLIGENCE, 73);
    setDefault(Attribute.COMBAT, 1);
    setDefault(Attribute.AGILITY, 33);
    setDefault(Attribute.MOVEMENT, 1);
    setDefault(Attribute.RANGED_COMBAT, 3);
    setDefault(Attribute.RANGE, 4);
    set(PowerUps.ARCHERY, 1);
  }

  @Override
  public long getLosMask() {
    return 0x00387C3838181800L;
  }

  @Override
  public Class<? extends Monster> promotion() {
    return DreadElf.class;
  }

  @Override
  public int promotionCount() {
    return 5;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return StoneGolem.class;
  }
}
