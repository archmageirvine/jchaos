package chaos.common.inanimate;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Animateable;
import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Caster;
import chaos.common.Inanimate;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.common.monster.MindFlayer;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Random;

/**
 * Roper.
 *
 * @author Sean A. Irvine
 */
public class Roper extends MaterialMonster implements Bonus, Animateable, NoDeadImage, Inanimate {

  private static final int ROPER_MOVE = 8;

  {
    setDefault(Attribute.LIFE, 55);
    setDefault(Attribute.SPECIAL_COMBAT, 1);
    setDefault(Attribute.LIFE_RECOVERY, 6);
    setDefault(Attribute.MAGICAL_RESISTANCE, 43);
    setSpecialCombatApply(Attribute.LIFE);
  }
  @Override
  public long getLosMask() {
    return 0x187E7EFF7F7F7F00L;
  }
  @Override
  public int getCastRange() {
    return 9;
  }
  @Override
  public int getCastFlags() {
    return CAST_GROWTH | CAST_EMPTY | CAST_LOS | CAST_DEAD;
  }
  @Override
  public int getBonus() {
    return 2;
  }
  @Override
  public void cast(final World world, final Caster caster, final Cell c, final Cell casterCell) {
    CastUtils.castStone(this, caster, c, casterCell);
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return null;
  }
  @Override
  public int getDefaultWeight() {
    return -1;
  }
  @Override
  public Actor getAnimatedForm() {
    final Actor a = new MindFlayer();
    a.setOwner(getOwner());
    return a;
  }
  @Override
  public boolean update(final World world, final Cell cell) {
    if (world != null && cell != null && isMoved() && Random.nextInt(ROPER_MOVE) == 0) {
      // Make sure roper only moves once per update cycle
      setMoved(false);
      final int newCell = Random.nextInt(world.size());
      if (world.actor(newCell) == null) {
        // can move into vacant cell
        cell.notify(new CellEffectEvent(cell, CellEffectType.WARP_OUT, this));
        final Actor a = cell.pop(); // i.e. this roper
        final Cell nc = world.getCell(newCell);
        nc.push(a);
        nc.notify(new CellEffectEvent(nc, CellEffectType.WARP_IN, this));
        nc.notify(new CellEffectEvent(nc, CellEffectType.REDRAW_CELL));
        cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
      }
    }
    return super.update(world, cell);
  }
}
