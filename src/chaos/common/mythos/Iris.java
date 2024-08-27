package chaos.common.mythos;

import chaos.common.Attribute;
import chaos.common.ListCaster;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.common.Realm;
import chaos.common.Singleton;
import chaos.common.free.Confidence;
import chaos.common.monster.Solar;
import chaos.common.spell.Archery;
import chaos.common.spell.Obscurity;

/**
 * Iris.
 * @author Sean A. Irvine
 */
public class Iris extends ListCaster implements NoDeadImage, Singleton {
  {
    setDefault(Attribute.LIFE, 8);
    setDefault(Attribute.LIFE_RECOVERY, 8);
    setDefault(Attribute.MAGICAL_RESISTANCE, 9);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 1);
    setDefault(Attribute.COMBAT, 0);
    setDefault(Attribute.AGILITY, 10);
    setDefault(Attribute.INTELLIGENCE, 0);
    setDefault(Attribute.MOVEMENT, 1);
    setRealm(Realm.MYTHOS);
  }

  @Override
  public long getLosMask() {
    return 0;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Solar.class;
  }

  /** Ceres. */
  @SuppressWarnings("unchecked")
  public Iris() {
    super(Archery.class, Obscurity.class, Archery.class, Obscurity.class, Archery.class, Obscurity.class, Confidence.class, Archery.class, Obscurity.class, Archery.class, Obscurity.class, Archery.class, Obscurity.class, Confidence.class, Archery.class, Obscurity.class, Archery.class, Obscurity.class, Archery.class, Obscurity.class, Confidence.class);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }
}
