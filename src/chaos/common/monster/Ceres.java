package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.ListCaster;
import chaos.common.Monster;
import chaos.common.Singleton;
import chaos.common.free.Bless;
import chaos.common.growth.Wheat;

/**
 * Ceres.
 * @author Sean A. Irvine
 */
public class Ceres extends ListCaster implements Humanoid, Singleton {
  {
    setDefault(Attribute.LIFE, 15);
    setDefault(Attribute.LIFE_RECOVERY, 4);
    setDefault(Attribute.MAGICAL_RESISTANCE, 70);
    setDefault(Attribute.COMBAT, 1);
    setDefault(Attribute.AGILITY, 20);
    setDefault(Attribute.INTELLIGENCE, 80);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x5878387C3C5C1C3EL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Agathion.class;
  }

  /** Ceres. */
  @SuppressWarnings("unchecked")
  public Ceres() {
    super(Wheat.class, Bless.class, Eagle.class, Wheat.class);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }

}
