package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonsterMount;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.common.Realm;

/**
 * Shadowmare.
 *
 * @author Sean A. Irvine
 * @author Gregory B. Irvine
 */
public class Shadowmare extends MaterialMonsterMount implements NoDeadImage {
  {
    setDefault(Attribute.LIFE, 45);
    setDefault(Attribute.LIFE_RECOVERY, 7);
    setDefault(Attribute.INTELLIGENCE, 60);
    setDefault(Attribute.INTELLIGENCE_RECOVERY, 3);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 20);
    setDefault(Attribute.COMBAT, 14);
    setDefault(Attribute.AGILITY, 45);
    setDefault(Attribute.MOVEMENT, 4);
    setDefault(Attribute.SPECIAL_COMBAT, 3);
    setSpecialCombatApply(Attribute.AGILITY);
    setRealm(Realm.ETHERIC);
  }

  @Override
  public long getLosMask() {
    return 0x0000E0FF7E7E2400L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Nightmare.class;
  }
}
