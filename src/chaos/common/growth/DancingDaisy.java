package chaos.common.growth;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.GrowthHelper;
import chaos.common.Inanimate;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.Multiplicity;
import chaos.common.PowerUps;
import chaos.common.Realm;
import chaos.common.State;
import chaos.common.UndyingGrowth;
import chaos.util.CastUtils;
import chaos.util.Random;

/**
 * Dancing Daisy.
 * @author Sean A. Irvine
 */
public class DancingDaisy extends MaterialMonster implements Inanimate, Multiplicity, UndyingGrowth {
  {
    setDefault(Attribute.LIFE, 1);
    setDefault(Attribute.SPECIAL_COMBAT, 1);
    setRealm(Realm.MATERIAL);
    setSpecialCombatApply(Attribute.INTELLIGENCE);
    set(PowerUps.TALISMAN, 3);
  }

  @Override
  public int getCastRange() {
    return 12;
  }

  @Override
  public long getLosMask() {
    return 0x003C3C3C3C181800L;
  }

  @Override
  public int getCastFlags() {
    return CAST_GROWTH | CAST_EMPTY | CAST_LOS;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell c, final Cell casterCell) {
    CastUtils.castTree(this, caster, c, casterCell);
  }

  @Override
  public int getDefaultWeight() {
    return 0;
  }

  @Override
  public int getMultiplicity() {
    return 4;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Thistle.class;
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    GrowthHelper.filter(targets, caster, world);
  }

  @Override
  public Class<? extends Actor> sproutClass() {
    return DancingDaisy.class;
  }

  @Override
  public int growthRate() {
    return 5;
  }

  @Override
  public int getGrowthType() {
    return GROW_OVER;
  }

  private boolean canGrowOver(final Cell cell) {
    // fungi only grows on empty or dead cells
    final Actor a = cell.peek();
    return a == null || a.getState() == State.DEAD;
  }

  @Override
  public void grow(final int cell, final World w) {
    // this code is subset of generic MaterialMonster grow() code.
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
    for (final Cell c : w.getCells(cell, 1, 2, false)) {
      if (Random.nextInt(m++) == 0) {
        tcell = c;
      }
    }
    if (tcell == null || !canGrowOver(tcell)) {
      return;
    }
    // prepare a new object of correct type and details and install it in the target cell
    final Actor sprout = new Thistle();
    sprout.setOwner(getOwner());
    sprout.setState(getState());
    sprout.setRealm(getRealm());
    tcell.push(sprout);
  }
}
