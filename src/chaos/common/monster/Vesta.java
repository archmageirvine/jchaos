package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.ListCaster;
import chaos.common.Monster;
import chaos.common.Singleton;
import chaos.common.free.Assurance;
import chaos.common.free.Materialize;
import chaos.common.free.ParadigmShift;
import chaos.common.free.Virtue;
import chaos.common.spell.Exorcise;
import chaos.common.spell.Heal;

/**
 * Ceres.
 * @author Sean A. Irvine
 */
public class Vesta extends ListCaster implements Humanoid, Singleton {
  {
    setDefault(Attribute.LIFE, 17);
    setDefault(Attribute.LIFE_RECOVERY, 3);
    setDefault(Attribute.MAGICAL_RESISTANCE, 50);
    setDefault(Attribute.COMBAT, 1);
    setDefault(Attribute.AGILITY, 10);
    setDefault(Attribute.INTELLIGENCE, 20);
    setDefault(Attribute.INTELLIGENCE_RECOVERY, 20);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x1A1E1E3E3C3A387CL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Agathion.class;
  }

  /** Ceres. */
  @SuppressWarnings("unchecked")
  public Vesta() {
    super(Exorcise.class, Assurance.class, Materialize.class, Heal.class, ParadigmShift.class, Exorcise.class, Virtue.class, Assurance.class, Materialize.class);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }

}
