package chaos.common.growth;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.MaterialGrowth;
import chaos.common.PowerUps;
import chaos.common.Promotion;
import chaos.common.UndyingGrowth;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Random;

/**
 * Earthquake.
 * @author Sean A. Irvine
 */
public class Earthquake extends MaterialGrowth implements Promotion, UndyingGrowth {
  {
    setDefault(Attribute.LIFE, 45);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    set(PowerUps.EARTHQUAKE_SHIELD, 1);
  }

  @Override
  public int getCastRange() {
    return 10;
  }

  @Override
  public int growthRate() {
    return 30;
  }

  @Override
  public int getDefaultWeight() {
    return -5;
  }

  @Override
  public int getGrowthType() {
    return GROW_FOUR_WAY;
  }

  @Override
  public boolean canGrowOver(final Cell cell) {
    // Only exception is the earthquake shield
    return cell.peek() == null || !cell.peek().is(PowerUps.EARTHQUAKE_SHIELD);
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell c, final Cell casterCell) {
    if (c != null && caster != null) {
      setOwner(caster.getOwner());
      c.notify(new CellEffectEvent(c, CellEffectType.EARTHQUAKE, this));
      c.push(this);
      c.notify(new CellEffectEvent(c, CellEffectType.REDRAW_CELL));
    }
  }

  @Override
  public Class<? extends Actor> promotion() {
    return Magma.class;
  }

  @Override
  public int promotionCount() {
    return 5;
  }

  @Override
  public Class<? extends Actor> sproutClass() {
    return Random.nextInt(10) == 0 ? Magma.class : Earthquake.class;
  }
}
