package chaos.common.monster;

import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.Promotion;
import chaos.common.UndeadMonster;
import chaos.common.Virtuous;

/**
 * Skeleton warrior.
 *
 * @author Sean A. Irvine
 */
public class SkeletonWarrior extends UndeadMonster implements Humanoid, Promotion, Virtuous {
  {
    setDefault(Attribute.LIFE, 15);
    setDefault(Attribute.INTELLIGENCE, 1);
    setDefault(Attribute.COMBAT, 6);
    setDefault(Attribute.AGILITY, 33);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x585EFEFA7C3C3C1CL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Skeleton.class;
  }

  @Override
  public Class<? extends Actor> promotion() {
    return SkeletonLord.class;
  }

  @Override
  public int promotionCount() {
    return 3;
  }
}
