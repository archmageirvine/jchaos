package chaos.common.mythos;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.MythosMonster;
import chaos.common.monster.Bandit;

/**
 * Sabre Man.
 * @author Sean A. Irvine
 * @author Chris Stamper
 * @author Tim Stamper
 */
public class SabreMan extends MythosMonster implements Humanoid {
  {
    setDefault(Attribute.LIFE, 12);
    setDefault(Attribute.MAGICAL_RESISTANCE, 12);
    setDefault(Attribute.AGILITY, 50);
    setDefault(Attribute.INTELLIGENCE, 15);
    setDefault(Attribute.MOVEMENT, 2);
    setDefault(Attribute.COMBAT, 8);
    setDefault(Attribute.COMBAT_RECOVERY, 2);
  }

  @Override
  public long getLosMask() {
    return 0x00003E3E7E3E3E1EL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Bandit.class;
  }
}
