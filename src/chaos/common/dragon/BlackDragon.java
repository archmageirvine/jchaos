package chaos.common.dragon;

import chaos.board.Cell;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Conveyance;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Realm;
import chaos.common.Rideable;
import chaos.common.Unicaster;
import chaos.common.spell.Lightning;

/**
 * The black dragon.
 * @author Sean A. Irvine
 */
public class BlackDragon extends Unicaster implements Bonus, Rideable {
  {
    setDefault(Attribute.LIFE, 61);
    setDefault(Attribute.LIFE_RECOVERY, 7);
    setDefault(Attribute.MAGICAL_RESISTANCE, 47);
    setDefault(Attribute.INTELLIGENCE, 99);
    setDefault(Attribute.COMBAT, 14);
    setDefault(Attribute.AGILITY, 10);
    setDefault(Attribute.MOVEMENT, 6);
    setDefault(Attribute.RANGED_COMBAT, 10);
    setDefault(Attribute.RANGE, 12);
    set(PowerUps.FLYING, 1);
    set(PowerUps.ATTACK_ANY_REALM, 1);
    setRealm(Realm.DRACONIC);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
    mDelay = 5;
    mCastClass = Lightning.class;
  }

  @Override
  public int getBonus() {
    return 6;
  }

  @Override
  public long getLosMask() {
    return 0x062E77FE9FBEF6FCL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return BlueDragon.class;
  }

  @Override
  public void doCasting(final Cell casterCell) {
    super.doCasting(casterCell);
    super.doCasting(casterCell);
  }

  private Actor mMount;

  @Override
  public Actor getMount() {
    return mMount;
  }

  @Override
  public void setMount(final Actor actor) {
    Conveyance.checkMount(this, actor);
    mMount = actor;
  }

}
