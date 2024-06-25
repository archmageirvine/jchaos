package chaos.common.dragon;

import chaos.Chaos;
import chaos.board.Cell;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Castable;
import chaos.common.Conveyance;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Promotion;
import chaos.common.Realm;
import chaos.common.Rideable;
import chaos.common.Unicaster;
import chaos.common.spell.Lightning;

/**
 * The blue dragon.
 * @author Sean A. Irvine
 */
public class BlueDragon extends Unicaster implements Bonus, Promotion, Rideable {
  {
    setDefault(Attribute.LIFE, 41);
    setDefault(Attribute.LIFE_RECOVERY, 2);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setDefault(Attribute.INTELLIGENCE, 87);
    setDefault(Attribute.COMBAT, 8);
    setDefault(Attribute.AGILITY, 27);
    setDefault(Attribute.MOVEMENT, 4);
    setDefault(Attribute.RANGED_COMBAT, 9);
    setDefault(Attribute.RANGE, 10);
    set(PowerUps.FLYING, 1);
    setRealm(Realm.DRACONIC);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
    mDelay = 6;
    mCastClass = Lightning.class;
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

  @Override
  public int getBonus() {
    return 2;
  }

  @Override
  public long getLosMask() {
    return 0x607EFF7FFD7F7F3FL;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return RedDragon.class;
  }

  @Override
  public Class<? extends Monster> promotion() {
    return BlackDragon.class;
  }

  @Override
  public int promotionCount() {
    return 5;
  }

  @Override
  public void doCasting(final Cell casterCell) {
    final Castable cast = getCastable();
    if (casterCell != null) {
      Chaos.getChaos().getAI().cast(this, cast, casterCell);
      if (cast != null) {
        increment(PowerUps.WAND);
      }
    }
  }
}
