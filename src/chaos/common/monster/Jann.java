package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.Polycaster;
import chaos.common.free.Boil;
import chaos.common.free.Quench;
import chaos.common.growth.GooeyBlob;
import chaos.common.growth.GreenOoze;
import chaos.common.growth.OrangeJelly;

/**
 * Jann.
 * @author Sean A. Irvine
 */
public class Jann extends Polycaster implements Humanoid {
  {
    setDefault(Attribute.LIFE, 12);
    setDefault(Attribute.MAGICAL_RESISTANCE, 75);
    setDefault(Attribute.COMBAT, 1);
    setDefault(Attribute.AGILITY, 45);
    setDefault(Attribute.INTELLIGENCE, 60);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x00081E7E7E3C7C00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Agathion.class;
  }

  /** Jann. */
  @SuppressWarnings("unchecked")
  public Jann() {
    super(3, Quench.class,
      Boil.class,
      OrangeJelly.class,
      GreenOoze.class,
      GooeyBlob.class
    );
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }

}
