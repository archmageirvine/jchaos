package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.DemonicMonster;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.NoDeadImage;

/**
 * Higher devil.
 *
 * @author Sean A. Irvine
 * @author Ingo Holewczuk
 */
public class HigherDevil extends DemonicMonster implements Humanoid, Bonus, NoDeadImage {
  {
    setDefault(Attribute.LIFE, 59);
    setDefault(Attribute.LIFE_RECOVERY, 7);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.COMBAT, 15);
    setDefault(Attribute.INTELLIGENCE, 85);
    setDefault(Attribute.AGILITY, 54);
    setDefault(Attribute.MOVEMENT, 5);
  }
  @Override
  public long getLosMask() {
    return 0xBFDBFFFFFFFF7E18L;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Vampire.class;
  }
  @Override
  public int getBonus() {
    return 4;
  }
}
