package chaos.common.monster;

import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.Promotion;

/**
 * Spider.
 * @author Sean A. Irvine
 * @author Ingo Holewczuk
 */
public class Spider extends MaterialMonster implements Promotion {
  {
    setDefault(Attribute.LIFE, 6);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.INTELLIGENCE, 7);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.AGILITY, 100);
    setDefault(Attribute.MOVEMENT, 3);
  }

  @Override
  public long getLosMask() {
    return 0x00007E7E7E7E0000L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return GiantBeetle.class;
  }

  @Override
  public Class<? extends Actor> promotion() {
    return Acromantula.class;
  }

  @Override
  public int promotionCount() {
    return 2;
  }
}
