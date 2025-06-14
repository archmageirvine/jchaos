package chaos.common.growth;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.FrequencyTable;
import chaos.common.GrowthHelper;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.UndyingGrowth;
import chaos.util.Random;

/**
 * Thistle.
 * @author Sean A. Irvine
 */
public class Thistle extends MaterialMonster implements UndyingGrowth {

  {
    setDefault(Attribute.LIFE, 1);
    setDefault(Attribute.SPECIAL_COMBAT, 1);
    setSpecialCombatApply(Attribute.LIFE);
  }

  @Override
  public int getCastRange() {
    return 18;
  }

  @Override
  public int getDefaultWeight() {
    return 2;
  }

  @Override
  public long getLosMask() {
    return 0x000019183C7C3C10L;
  }

  @Override
  public int growthRate() {
    return 1; // very slow to grow by itself
  }

  @Override
  public int getGrowthType() {
    return GROW_OVER;
  }

  private boolean canGrowOver(final Cell cell) {
    // Only grows on empty or dead cells
    final Actor a = cell.peek();
    return a == null || a.getState() == State.DEAD;
  }

  @Override
  public void grow(final int cell, final World w) {
    // this code is subset of generic MaterialMonster grow() code.
    // integrity check (should never happen)
    if (w.actor(cell) != this) {
      throw new RuntimeException("Requested cell does not contain this growth");
    }
    // only active growths can grow
    if (getState() != State.ACTIVE || is(PowerUps.NO_GROW)) {
      return;
    }
    // choose a target randomly and fairly
    int m = 1;
    Cell tcell = null;
    for (final Cell c : w.getCells(cell, 1, 1, false)) {
      if (Random.nextInt(m++) == 0) {
        tcell = c;
      }
    }
    if (tcell == null || !canGrowOver(tcell)) {
      return;
    }
    final Actor sprout = (Actor) FrequencyTable.instantiate(getClass());
    sprout.setOwner(getOwner());
    sprout.setState(getState());
    sprout.setRealm(getRealm());
    tcell.push(sprout);
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return null;
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    GrowthHelper.filter(targets, caster, world);
  }

  @Override
  public Class<? extends Actor> sproutClass() {
    return DancingDaisy.class;
  }
}
