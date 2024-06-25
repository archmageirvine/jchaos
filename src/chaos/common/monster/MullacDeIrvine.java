package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.Polycaster;
import chaos.common.growth.Earthquake;
import chaos.common.growth.Fire;
import chaos.common.growth.Flood;
import chaos.common.growth.GooeyBlob;
import chaos.common.growth.GreenOoze;
import chaos.common.growth.OrangeJelly;
import chaos.common.growth.VioletFungi;

/**
 * Mullac de Irvine.
 * @author Sean A. Irvine
 */
public class MullacDeIrvine extends Polycaster implements Humanoid {
  {
    setDefault(Attribute.LIFE, 16);
    setDefault(Attribute.MAGICAL_RESISTANCE, 55);
    setDefault(Attribute.COMBAT, 1);
    setDefault(Attribute.AGILITY, 5);
    setDefault(Attribute.INTELLIGENCE, 100);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x0098D8FC3C183800L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Jann.class;
  }

  /** Mullac de Irvine. */
  @SuppressWarnings("unchecked")
  public MullacDeIrvine() {
    super(3, Earthquake.class,
      Fire.class,
      Flood.class,
      GreenOoze.class,
      GooeyBlob.class,
      OrangeJelly.class,
      VioletFungi.class);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }

}
