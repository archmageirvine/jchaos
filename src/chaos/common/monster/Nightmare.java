package chaos.common.monster;

import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.MaterialMonsterMount;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.common.Promotion;
import chaos.common.Realm;

/**
 * Nightmare.
 * @author Sean A. Irvine
 */
public class Nightmare extends MaterialMonsterMount implements NoDeadImage, Promotion {
  {
    setDefault(Attribute.LIFE, 16);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.INTELLIGENCE, 70);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.COMBAT, 1);
    setDefault(Attribute.AGILITY, 70);
    setDefault(Attribute.MOVEMENT, 4);
    setRealm(Realm.ETHERIC);
  }

  @Override
  public long getLosMask() {
    return 0x0000E0FF7E7E2400L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Skeleton.class;
  }

  @Override
  public Class<? extends Actor> promotion() {
    return Shadowmare.class;
  }

  @Override
  public int promotionCount() {
    return 3;
  }
}
