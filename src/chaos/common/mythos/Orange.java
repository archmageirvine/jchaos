package chaos.common.mythos;

import chaos.common.Attribute;
import chaos.common.Monster;
import chaos.common.MythosMonster;
import chaos.common.PowerUps;

/**
 * Orange.
 *
 * @author Sean A. Irvine
 */
public class Orange extends MythosMonster {
  {
    setDefault(Attribute.LIFE, 20);
    setDefault(Attribute.LIFE_RECOVERY, 4);
    setDefault(Attribute.MAGICAL_RESISTANCE, 5);
    setDefault(Attribute.AGILITY, 89);
    setDefault(Attribute.INTELLIGENCE, 0);
    setDefault(Attribute.MOVEMENT, 12);
    setDefault(Attribute.COMBAT, 1);
    setDefault(Attribute.SPECIAL_COMBAT, 10);
    set(PowerUps.FLYING, 1);
  }
  @Override
  public long getLosMask() {
    return 0x3C7EFFFFFFFF7E3CL;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return null;
  }
}
