package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.Polycaster;
import chaos.common.Rider;
import chaos.common.free.Bless;
import chaos.common.free.Command;
import chaos.common.free.Haste;
import chaos.common.free.Vitality;
import chaos.common.spell.Protection;

/**
 * Agathion.
 *
 * @author Sean A. Irvine
 */
@SuppressWarnings("unchecked")
public class Agathion extends Polycaster implements Humanoid, Bonus, Rider {

  /**
   * Agathion.
   */
  public Agathion() {
    super(2, Bless.class,
      Bless.class,
      Vitality.class,
      Haste.class,
      Command.class,
      Protection.class);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }

  {
    setDefault(Attribute.LIFE, 18);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setDefault(Attribute.COMBAT, 1);
    setDefault(Attribute.AGILITY, 34);
    setDefault(Attribute.INTELLIGENCE, 90);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x00181B3F3E183C00L;
  }

  @Override
  public int getBonus() {
    return 1;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Druid.class;
  }
}
