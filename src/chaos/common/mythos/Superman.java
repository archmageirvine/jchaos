package chaos.common.mythos;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.MythosMonster;
import chaos.common.PowerUps;
import chaos.common.Singleton;

/**
 * Superman.
 * @author Sean A. Irvine
 */
public class Superman extends MythosMonster implements Humanoid, Singleton {
  {
    setDefault(Attribute.LIFE, 50);
    setDefault(Attribute.LIFE_RECOVERY, 4);
    setDefault(Attribute.MAGICAL_RESISTANCE, 25);
    setDefault(Attribute.AGILITY, 2);
    setDefault(Attribute.INTELLIGENCE, 30);
    setDefault(Attribute.MOVEMENT, 8);
    setDefault(Attribute.COMBAT, 14);
    set(PowerUps.FLYING, 1);
  }

  @Override
  public long getLosMask() {
    return 0x00687C7E3C3C1E16L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return null;
  }
}
