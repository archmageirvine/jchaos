package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.ListCaster;
import chaos.common.Monster;
import chaos.common.Singleton;
import chaos.common.free.Arborist;
import chaos.common.inanimate.AppleWood;
import chaos.common.inanimate.Conifer;
import chaos.common.inanimate.DarkWood;
import chaos.common.inanimate.Elm;
import chaos.common.inanimate.Fir;
import chaos.common.inanimate.MagicWood;
import chaos.common.inanimate.ShadowWood;
import chaos.common.spell.Protection;

/**
 * Hebe.
 * @author Sean A. Irvine
 */
public class Hebe extends ListCaster implements Humanoid, Singleton {
  {
    setDefault(Attribute.LIFE, 18);
    setDefault(Attribute.LIFE_RECOVERY, 7);
    setDefault(Attribute.MAGICAL_RESISTANCE, 32);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 7);
    setDefault(Attribute.COMBAT, 1);
    setDefault(Attribute.AGILITY, 10);
    setDefault(Attribute.INTELLIGENCE, 37);
    setDefault(Attribute.INTELLIGENCE_RECOVERY, 1);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x001C3C3C3C3C383CL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Agathion.class;
  }

  /** Ceres. */
  @SuppressWarnings("unchecked")
  public Hebe() {
    super(AppleWood.class, Elm.class, Protection.class, Fir.class, Arborist.class, Conifer.class, MagicWood.class, ShadowWood.class, DarkWood.class);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }
}
