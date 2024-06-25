package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.Polycaster;
import chaos.common.free.Countermand;
import chaos.common.free.Curse;
import chaos.common.free.Drain;
import chaos.common.free.Impair;
import chaos.common.spell.Clumsy;
import chaos.common.spell.CursedSword;
import chaos.common.spell.Stop;

/**
 * Dao.
 * @author Sean A. Irvine
 */
public class Dao extends Polycaster implements Humanoid {
  {
    setDefault(Attribute.LIFE, 17);
    setDefault(Attribute.LIFE_RECOVERY, 3);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.COMBAT, 1);
    setDefault(Attribute.AGILITY, 55);
    setDefault(Attribute.INTELLIGENCE, 60);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x667E3C3C38387C00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Orc.class;
  }

  /** Dao. */
  @SuppressWarnings("unchecked")
  public Dao() {
    super(4, Curse.class,
      Drain.class,
      Clumsy.class,
      Stop.class,
      CursedSword.class,
      Impair.class,
      Countermand.class
    );
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }

}
