package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Monster;
import chaos.common.Polycaster;
import chaos.common.PowerUps;
import chaos.common.inanimate.Web;

/**
 * Acromantula.
 * @author Sean A. Irvine
 * @author Gregory B. Irvine
 */
public class Acromantula extends Polycaster implements Bonus {

  /** Acromantula. */
  @SuppressWarnings("unchecked")
  public Acromantula() {
    super(3, Web.class,
      Web.class,
      GiantSpider.class);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
    setDefault(Attribute.LIFE, 40);
    setDefault(Attribute.LIFE_RECOVERY, 5);
    setDefault(Attribute.MAGICAL_RESISTANCE, 80);
    setDefault(Attribute.INTELLIGENCE, 50);
    setDefault(Attribute.COMBAT, 12);
    setDefault(Attribute.AGILITY, 100);
    setDefault(Attribute.MOVEMENT, 3);
    set(PowerUps.INVULNERABLE, 4);
    set(PowerUps.WAND, 4);
  }

  @Override
  public long getLosMask() {
    return 0x3CFF7FFFFFFFFF3CL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Spider.class;
  }

  @Override
  public int getBonus() {
    return 1;
  }
}
