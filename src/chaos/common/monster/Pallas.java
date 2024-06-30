package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Humanoid;
import chaos.common.ListCaster;
import chaos.common.Monster;
import chaos.common.Singleton;
import chaos.common.free.Anger;
import chaos.common.free.Command;
import chaos.common.free.Sharpshooting;
import chaos.common.inanimate.BipedalGenerator;
import chaos.common.mythos.SabreMan;
import chaos.common.spell.Combat;
import chaos.common.spell.Quickshot;

/**
 * Pallas.
 * @author Sean A. Irvine
 */
public class Pallas extends ListCaster implements Humanoid, Singleton, Bonus {
  {
    setDefault(Attribute.LIFE, 32);
    setDefault(Attribute.LIFE_RECOVERY, 3);
    setDefault(Attribute.MAGICAL_RESISTANCE, 20);
    setDefault(Attribute.COMBAT, 3);
    setDefault(Attribute.AGILITY, 18);
    setDefault(Attribute.INTELLIGENCE, 40);
    setDefault(Attribute.MOVEMENT, 2);
  }

  @Override
  public long getLosMask() {
    return 0x183C3C3C3C3C3C3CL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Agathion.class;
  }

  /** Ceres. */
  @SuppressWarnings("unchecked")
  public Pallas() {
    super(BipedalGenerator.class, Command.class, Anger.class, SabreMan.class, Sharpshooting.class, Command.class, Anger.class, Sharpshooting.class, Quickshot.class, Combat.class, Command.class, Anger.class);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }

  @Override
  public int getBonus() {
    return 1;
  }
}
