package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.ListCaster;
import chaos.common.Monster;
import chaos.common.Singleton;
import chaos.common.growth.DancingDaisy;
import chaos.common.growth.Thistle;

/**
 * Flora.
 * @author Sean A. Irvine
 */
public class Flora extends ListCaster implements Humanoid, Singleton {
  {
    setDefault(Attribute.LIFE, 17);
    setDefault(Attribute.LIFE_RECOVERY, 9);
    setDefault(Attribute.MAGICAL_RESISTANCE, 30);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 7);
    setDefault(Attribute.COMBAT, 1);
    setDefault(Attribute.AGILITY, 20);
    setDefault(Attribute.INTELLIGENCE, 39);
    setDefault(Attribute.INTELLIGENCE_RECOVERY, 1);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x00383C3C3C3C1C3CL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Agathion.class;
  }

  /** Ceres. */
  @SuppressWarnings("unchecked")
  public Flora() {
    super(Thistle.class, Thistle.class, Triffid.class, DancingDaisy.class, Thistle.class, Bat.class, Thistle.class, DancingDaisy.class, Triffid.class);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }
}
