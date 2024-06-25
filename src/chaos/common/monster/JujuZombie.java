package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.common.UndeadMonster;

/**
 * Juju zombie.
 * @author Sean A. Irvine
 */
public class JujuZombie extends UndeadMonster implements Humanoid, NoDeadImage {
  {
    setDefault(Attribute.LIFE, 6);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 29);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.AGILITY, 27);
    setDefault(Attribute.MOVEMENT, 1);
    setCombatApply(Attribute.MAGICAL_RESISTANCE);
  }

  @Override
  public long getLosMask() {
    return 0x00181E181C3C3C00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Zombie.class;
  }
}
