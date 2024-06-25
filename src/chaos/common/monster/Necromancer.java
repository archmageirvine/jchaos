package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.Polycaster;
import chaos.common.Rider;
import chaos.common.spell.Kill;
import chaos.common.spell.RaiseDead;

/**
 * Necromancer.
 * @author Sean A. Irvine
 */
public class Necromancer extends Polycaster implements Humanoid, Rider {
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
    return 0x001FFFFF7C1C7C00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Azer.class;
  }

  /** Necromancer. */
  @SuppressWarnings("unchecked")
  public Necromancer() {
    super(3, Kill.class, RaiseDead.class);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }
}
