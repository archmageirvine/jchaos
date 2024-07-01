package chaos.common.mythos;

import chaos.common.Attribute;
import chaos.common.Monster;
import chaos.common.MythosMonster;

/**
 * Space Raider.
 * @author Sean A. Irvine
 * @author Psion Software
 */
public class SpaceRaider extends MythosMonster {
  {
    setDefault(Attribute.LIFE, 4);
    setDefault(Attribute.MAGICAL_RESISTANCE, 4);
    setDefault(Attribute.AGILITY, 4);
    setDefault(Attribute.INTELLIGENCE, 4);
    setDefault(Attribute.MOVEMENT, 4);
    setDefault(Attribute.COMBAT, 4);
  }

  @Override
  public long getLosMask() {
    return 0x007CFEFEFE7CFE00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return null;
  }
}
