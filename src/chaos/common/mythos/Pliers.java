package chaos.common.mythos;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.MythosMonster;

/**
 * Pliers.
 * @author Sean A. Irvine
 */
public class Pliers extends MythosMonster implements Humanoid {
  {
    setDefault(Attribute.LIFE, 20);
    setDefault(Attribute.MAGICAL_RESISTANCE, 10);
    setDefault(Attribute.AGILITY, 100);
    setDefault(Attribute.INTELLIGENCE, 10);
    setDefault(Attribute.MOVEMENT, 2);
    setDefault(Attribute.COMBAT, 14);
  }

  @Override
  public long getLosMask() {
    return 0x3C3C3C7E7EFFDBDBL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Orange.class;
  }
}
