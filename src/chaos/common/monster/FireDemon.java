package chaos.common.monster;

import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.DemonicMonster;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.common.Promotion;

/**
 * Fire demon.
 * @author Sean A. Irvine
 * @author Ingo Holewczuk
 */
public class FireDemon extends DemonicMonster implements Humanoid, Bonus, NoDeadImage, Promotion {
  {
    setDefault(Attribute.LIFE, 63);
    setDefault(Attribute.LIFE_RECOVERY, 7);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.COMBAT, 14);
    setDefault(Attribute.RANGED_COMBAT, 10);
    setDefault(Attribute.RANGE, 2);
    setDefault(Attribute.INTELLIGENCE, 100);
    setDefault(Attribute.AGILITY, 54);
    setDefault(Attribute.MOVEMENT, 3);
  }

  @Override
  public long getLosMask() {
    return 0xFEFFFFFFFFFFFFBFL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return HigherDevil.class;
  }

  @Override
  public int getBonus() {
    return 4;
  }

  @Override
  public Class<? extends Actor> promotion() {
    return Balrog.class;
  }

  @Override
  public int promotionCount() {
    return 10;
  }
}
