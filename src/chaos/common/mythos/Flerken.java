package chaos.common.mythos;

import chaos.common.Attribute;
import chaos.common.Cat;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.Realm;
import chaos.common.monster.Tiger;

/**
 * Flerken.
 * @author Sean A. Irvine
 * @author Cameron T. Irvine
 */
public class Flerken extends MaterialMonster implements Cat {

  {
    setDefault(Attribute.LIFE, 50);
    setDefault(Attribute.LIFE_RECOVERY, 5);
    setDefault(Attribute.MAGICAL_RESISTANCE, 20);
    setDefault(Attribute.INTELLIGENCE, 15);
    setDefault(Attribute.COMBAT, 10);
    setDefault(Attribute.RANGED_COMBAT, 11);
    setDefault(Attribute.RANGED_COMBAT_RECOVERY, 2);
    setDefault(Attribute.RANGE, 5);
    setDefault(Attribute.RANGE_RECOVERY, 1);
    setDefault(Attribute.AGILITY, 15);
    setDefault(Attribute.MOVEMENT, 5);
    setRealm(Realm.MYTHOS);
  }

  @Override
  public long getLosMask() {
    return 0x0000000407DF7F00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Tiger.class;
  }
}
