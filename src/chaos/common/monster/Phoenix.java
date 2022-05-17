package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.WakeOnFire;

/**
 * The achiyalabopa.
 *
 * @author Sean A. Irvine
 */
public class Phoenix extends MaterialMonster implements WakeOnFire {
  {
    setDefault(Attribute.LIFE, 18);
    setDefault(Attribute.LIFE_RECOVERY, 3);
    setDefault(Attribute.MAGICAL_RESISTANCE, 5);
    setDefault(Attribute.COMBAT, 6);
    setDefault(Attribute.INTELLIGENCE, 20);
    setDefault(Attribute.AGILITY, 9);
    setDefault(Attribute.MOVEMENT, 6);
    set(PowerUps.FLYING, 1);
  }
  @Override
  public int getCastRange() {
    return 3;
  }
  @Override
  public long getLosMask() {
    return 0x0000EEFFFFE70200L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Achiyalabopa.class;
  }

}
