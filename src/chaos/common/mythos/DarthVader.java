package chaos.common.mythos;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.MythosMonster;
import chaos.common.Singleton;
import chaos.common.monster.Ghost;

/**
 * Darth Vader
 *
 * @author Sean A. Irvine
 */
public class DarthVader extends MythosMonster implements Humanoid, Singleton {
  {
    setDefault(Attribute.LIFE, 45);
    setDefault(Attribute.MAGICAL_RESISTANCE, 75);
    setDefault(Attribute.AGILITY, 33);
    setDefault(Attribute.INTELLIGENCE, 30);
    setDefault(Attribute.MOVEMENT, 1);
    setDefault(Attribute.COMBAT, 13);
    setDefault(Attribute.SPECIAL_COMBAT, 4);
  }
  @Override
  public long getLosMask() {
    return 0x0C4E3E7EFEFF7F1BL;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return Ghost.class;
  }
}
