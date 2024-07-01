package chaos.common.monster;

import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.ListCaster;
import chaos.common.Monster;
import chaos.common.Singleton;
import chaos.common.dragon.RedDragon;
import chaos.common.mythos.MrStrong;
import chaos.common.mythos.SpaceRaider;
import chaos.common.mythos.SwissKnife;

/**
 * Pallas.
 * @author Sean A. Irvine
 */
public class Juno extends ListCaster implements Humanoid, Singleton {
  {
    setDefault(Attribute.LIFE, 27);
    setDefault(Attribute.LIFE_RECOVERY, 7);
    setDefault(Attribute.MAGICAL_RESISTANCE, 27);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 7);
    setDefault(Attribute.COMBAT, 1);
    setDefault(Attribute.AGILITY, 24);
    setDefault(Attribute.INTELLIGENCE, 48);
    setDefault(Attribute.INTELLIGENCE_RECOVERY, 7);
    setDefault(Attribute.MOVEMENT, 1);
    setDefault(Attribute.MOVEMENT_RECOVERY, 1);
  }

  @Override
  public long getLosMask() {
    return 0x00383C3C3C3C1C3CL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Agathion.class;
  }

  /** Ceres. */
  @SuppressWarnings("unchecked")
  public Juno() {
    super(Ogre.class, RedDragon.class, SpaceRaider.class, OgreMage.class, MrStrong.class, Vampire.class, Necromancer.class, SwissKnife.class, OgreWarrior.class);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }
}
