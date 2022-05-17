package chaos.common.growth;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.FrequencyTable;
import chaos.common.GrowthHelper;
import chaos.common.Monster;
import chaos.common.MythosMonster;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.UndyingGrowth;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.ChaosProperties;
import chaos.util.Random;

/**
 * Spiky.
 * @author Sean A. Irvine
 */
public class Spiky extends MythosMonster implements UndyingGrowth {

  private static final int DEATH_EXPECTED = ChaosProperties.properties().getIntProperty("chaos.spiky.expected", 50);

  {
    setDefault(Attribute.LIFE, 32);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.MAGICAL_RESISTANCE, 23);
    setDefault(Attribute.SPECIAL_COMBAT, 4);
    setSpecialCombatApply(Attribute.INTELLIGENCE);
  }

  @Override
  public int getCastRange() {
    return 9;
  }

  @Override
  public int getDefaultWeight() {
    return 2;
  }

  @Override
  public long getLosMask() {
    return 0x00185A7E3C7E7E3CL;
  }

  @Override
  public int growthRate() {
    return 16;
  }

  @Override
  public int getGrowthType() {
    return GROW_FOUR_WAY; // this stops them dying at random
  }

  private boolean canGrowOver(final Cell cell) {
    final Actor a = cell.peek();
    return a == null || a.getState() == State.DEAD;
  }

  @Override
  public void grow(final int cell, final World w) {
    // this code is subset of generic MaterialMonster grow() code.
    if (w.actor(cell) != this) {
      throw new RuntimeException("Requested cell does not contain this growth");
    }
    if (getState() != State.ACTIVE || is(PowerUps.NO_GROW)) {
      return;
    }
    // Choose a target randomly and fairly
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
    // prepare a new object of correct type and details and install it in the target cell
    final Actor sprout = (Actor) FrequencyTable.instantiate(sproutClass());
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
    return Random.nextInt(8) == 0 ? DarkMatter.class : Spiky.class;
  }
  
  @Override
  public boolean update(final World world, final Cell cell) {
    if (cell != null && Random.nextInt(DEATH_EXPECTED) == 0) {
      cell.notify(new CellEffectEvent(cell, CellEffectType.FIREBALL_EXPLODE));
      for (final Cell c : world.getCells(cell.getCellNumber(), 1, 1, false)) {
        final Actor a = c.peek();
        if (a != null && a.getState() != State.DEAD) {
          c.notify(new CellEffectEvent(c, CellEffectType.ORANGE_CIRCLE_EXPLODE));
          a.set(Attribute.INTELLIGENCE, 2);
          c.notify(new CellEffectEvent(c, CellEffectType.REDRAW_CELL));
        }
      }
      cell.pop();
      return true;
    }
    return super.update(world, cell);
  }
}
