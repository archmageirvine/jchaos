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
 * Demon.
 *
 * @author Sean A. Irvine
 * @author Ingo Holewczuk
 */
public class Demon extends DemonicMonster implements Humanoid, Bonus, NoDeadImage, Promotion {
  {
    setDefault(Attribute.LIFE, 62);
    setDefault(Attribute.LIFE_RECOVERY, 7);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.COMBAT, 15);
    setDefault(Attribute.INTELLIGENCE, 90);
    setDefault(Attribute.AGILITY, 54);
    setDefault(Attribute.MOVEMENT, 3);
  }
  @Override
  public long getLosMask() {
    return 0xFEFFFFFFFFFFFFBFL;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Spectre.class;
  }
  @Override
  public int getBonus() {
    return 3;
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
