package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.Polycaster;
import chaos.common.free.Biohazard;
import chaos.common.free.Bless;
import chaos.common.free.Command;
import chaos.common.free.Level;
import chaos.common.free.Still;
import chaos.common.free.Venom;
import chaos.common.growth.VioletFungi;
import chaos.common.inanimate.Pentagram;
import chaos.common.spell.Armour;
import chaos.common.spell.Fireball;
import chaos.common.spell.Freeze;
import chaos.common.spell.RangeBoost;
import chaos.common.spell.Speed;
import chaos.common.spell.Wake;

/**
 * Marid.
 * @author Sean A. Irvine
 */
public class Marid extends Polycaster implements Humanoid {
  {
    setDefault(Attribute.LIFE, 29);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 60);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.AGILITY, 5);
    setDefault(Attribute.INTELLIGENCE, 90);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x00183E785C3C3C00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Necromancer.class;
  }

  /** Marid. */
  @SuppressWarnings("unchecked")
  public Marid() {
    super(3, Imp.class,
      Bless.class,
      VioletFungi.class,
      Command.class,
      Still.class,
      Level.class,
      Biohazard.class,
      Freeze.class,
      Wake.class,
      RangeBoost.class,
      Fireball.class,
      Speed.class,
      Venom.class,
      Pentagram.class,
      Armour.class);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }

}
