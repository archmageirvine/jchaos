package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.common.Promotion;
import chaos.common.UndeadMonster;
import chaos.common.Virtuous;

/**
 * Skeleton.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Skeleton extends UndeadMonster implements Promotion, Humanoid, NoDeadImage, Virtuous {
  {
    setDefault(Attribute.LIFE, 5);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.AGILITY, 33);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x585EFEFA7C3C3C1CL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Imp.class;
  }

  @Override
  public Class<? extends Monster> promotion() {
    return SkeletonWarrior.class;
  }

  @Override
  public int promotionCount() {
    return 2;
  }
}
