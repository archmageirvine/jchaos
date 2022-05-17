package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.DemonicMonster;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.NoDeadImage;

/**
 * Lesser devil.
 *
 * @author Sean A. Irvine
 * @author Ingo Holewczuk
 */
public class LesserDevil extends DemonicMonster implements Humanoid, Bonus, NoDeadImage {
  {
    setDefault(Attribute.LIFE, 56);
    setDefault(Attribute.LIFE_RECOVERY, 6);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.COMBAT, 12);
    setDefault(Attribute.INTELLIGENCE, 75);
    setDefault(Attribute.AGILITY, 54);
    setDefault(Attribute.MOVEMENT, 3);
  }
  @Override
  public long getLosMask() {
    return 0x3CFFFFFFFFFE7E18L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Zombie.class;
  }
  @Override
  public int getBonus() {
    return 3;
  }
}
