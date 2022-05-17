package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Unicaster;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.free.Rampage;

/**
 * Carnivorous ape.
 *
 * @author Sean A. Irvine
 */
public class CarnivorousApe extends Unicaster implements Humanoid {

  {
    setDefault(Attribute.LIFE, 39);
    setDefault(Attribute.LIFE_RECOVERY, 5);
    setDefault(Attribute.INTELLIGENCE, 28);
    setDefault(Attribute.MAGICAL_RESISTANCE, 53);
    setDefault(Attribute.COMBAT, 14);
    setDefault(Attribute.AGILITY, 30);
    setDefault(Attribute.MOVEMENT, 2);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
    set(PowerUps.REINCARNATE, 1);
    set(PowerUps.INVULNERABLE, 2);
    mDelay = 6;
    mCastClass = Rampage.class;
  }

  @Override
  public long getLosMask() {
    return 0x007E7E7E3C3C7E00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Gorilla.class;
  }
}
