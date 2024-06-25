package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.common.UndeadMonster;

/**
 * Zombie.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Zombie extends UndeadMonster implements Humanoid, NoDeadImage {
  {
    setDefault(Attribute.LIFE, 6);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 29);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.AGILITY, 27);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x00187818383C3C00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Shadow.class;
  }
}
