package chaos.common.mythos;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.MythosMonster;
import chaos.common.NoDeadImage;
import chaos.common.PowerUps;

/**
 * Horace.
 * @author Sean A. Irvine
 */
public class Horace extends MythosMonster implements Humanoid, NoDeadImage {
  {
    setDefault(Attribute.LIFE, 5);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 1);
    setDefault(Attribute.AGILITY, 0);
    setDefault(Attribute.INTELLIGENCE, 4);
    setDefault(Attribute.MOVEMENT, 4);
    setDefault(Attribute.COMBAT, 1);
    set(PowerUps.HORROR, 2);
  }

  @Override
  public long getLosMask() {
    return 0x7EFFFFFF7E7E7EFFL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return SpaceRaider.class;
  }

  @Override
  public int getCastRange() {
    return 1;
  }
}
