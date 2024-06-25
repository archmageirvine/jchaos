package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.WakeOnFire;

/**
 * Troll.  Graphics for a troll were defined in Spectrum Chaos, but the
 * creature never appeared in the standard game.
 * @author Sean A. Irvine
 */
public class Troll extends MaterialMonster implements Humanoid, WakeOnFire {
  {
    setDefault(Attribute.LIFE, 11);
    setDefault(Attribute.LIFE_RECOVERY, 5);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.AGILITY, 13);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x0078FC3E3E383800L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Gorilla.class;
  }
}
