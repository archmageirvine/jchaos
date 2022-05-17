package chaos.common.mythos;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.MythosMonster;
import chaos.common.Singleton;

/**
 * Mr Strong.
 * @author Sean A. Irvine
 */
public class MrStrong extends MythosMonster implements Humanoid, Singleton {
  {
    setDefault(Attribute.LIFE, 50);
    setDefault(Attribute.MAGICAL_RESISTANCE, 5);
    setDefault(Attribute.AGILITY, 13);
    setDefault(Attribute.INTELLIGENCE, 10);
    setDefault(Attribute.MOVEMENT, 1);
    setDefault(Attribute.COMBAT, 13);
  }

  @Override
  public long getLosMask() {
    return 0x00387C7F7E7C3800L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return null;
  }
}
