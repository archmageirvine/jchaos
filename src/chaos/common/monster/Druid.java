package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.Polycaster;
import chaos.common.inanimate.MagicWood;
import chaos.common.spell.Lightning;
import chaos.common.spell.MagicBolt;

/**
 * Druid.
 * @author Sean A. Irvine
 */
public class Druid extends Polycaster implements Humanoid {
  {
    setDefault(Attribute.LIFE, 16);
    setDefault(Attribute.MAGICAL_RESISTANCE, 57);
    setDefault(Attribute.COMBAT, 1);
    setDefault(Attribute.AGILITY, 7);
    setDefault(Attribute.INTELLIGENCE, 100);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x00189CFE7E3C3E00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Bandit.class;
  }

  /** Druid. */
  @SuppressWarnings("unchecked")
  public Druid() {
    super(12, Lightning.class,
      Lightning.class,
      MagicBolt.class,
      MagicWood.class);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }

}
