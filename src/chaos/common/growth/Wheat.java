package chaos.common.growth;

import java.util.HashSet;
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
import chaos.util.CellEffectType;
import chaos.util.PolycellEffectEvent;
import chaos.util.Random;

/**
 * Wheat.
 * @author Sean A. Irvine
 */
public class Wheat extends MaterialMonster implements UndyingGrowth {

  {
    setDefault(Attribute.LIFE, 1);
    setDefault(Attribute.SPECIAL_COMBAT, -1);
    setSpecialCombatApply(Attribute.LIFE);
  }

  @Override
  public int getCastRange() {
    return 0;
  }

  @Override
  public int getDefaultWeight() {
    return 2;
  }

  @Override
  public long getLosMask() {
    return 0x0;
  }

  @Override
  public int growthRate() {
    return 4;
  }

  @Override
  public int getGrowthType() {
    return GROW_OVER;
  }

  private boolean canGrowOver(final Cell cell) {
    // wheat only grows on empty or dead cells
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
    return Wheat.class;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    // cell is ignored here, this is effectively a free castable
    if (world != null && casterCell != null) {
      final HashSet<Cell> affected = new HashSet<>();
      for (final Cell c : world.getCells(casterCell.getCellNumber(), 2, 3, false)) {
        if (c.peek() == null || c.peek().getState() == State.DEAD) {
          affected.add(c);
        }
      }
      world.notify(new PolycellEffectEvent(affected, CellEffectType.MONSTER_CAST_EVENT, this));
      for (final Cell c : affected) {
        final Actor a = (Actor) FrequencyTable.instantiate(Wheat.class);
        a.setOwner(caster.getOwner());
        c.push(a);
      }
      world.notify(new PolycellEffectEvent(affected, CellEffectType.REDRAW_CELL, caster));
    }
  }
}
