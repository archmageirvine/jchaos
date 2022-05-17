package chaos.common.monster;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.CoreMonster;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * Gollop.
 * @author Sean A. Irvine
 */
public class Gollop extends CoreMonster implements NoDeadImage {
  {
    setDefault(Attribute.LIFE, 1);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x0;
  }

  @Override
  public int getCastRange() {
    return 1;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell c, final Cell casterCell) {
    if (c != null) {
      if (caster != null) {
        setOwner(caster.getOwner());
      }
      if (casterCell != null) {
        c.notify(new WeaponEffectEvent(casterCell, c, WeaponEffectType.TREE_CAST_EVENT, this));
      }
      c.notify(new CellEffectEvent(c, CellEffectType.MONSTER_CAST_EVENT, this));
      c.push(this);
      c.notify(new CellEffectEvent(c, CellEffectType.REDRAW_CELL, this));
    }
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return null;
  }

  @Override
  public int getDefaultWeight() {
    return 60;
  }

  private static final int MAX_USELESS_GAIN = 10;

  /**
   * Test if it is a good idea for specified actor to merge with this Gollop.
   * @param a actor to consider
   * @return true iff it is desirable to merge
   */
  public boolean shouldMerge(final Actor a) {
    if (a instanceof Gollop) {
      return false;
    }
    int uselessGain = 0;
    for (final Attribute attribute : Attribute.values()) {
      final int total = get(attribute) + a.get(attribute);
      if (total > attribute.max()) {
        uselessGain += total - attribute.max();
      }
    }
    return uselessGain < MAX_USELESS_GAIN;
  }
}
