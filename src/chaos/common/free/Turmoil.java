package chaos.common.free;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;
import irvine.math.Shuffle;

/**
 * Turmoil.  This was the only extra spell available by meditation in Spectrum
 * Chaos.  Also, the Spectrum implementation was buggy and would occasionally
 * crash.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class Turmoil extends FreeCastable {

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null) {
      // Compute a shuffle of the cells.
      final int[] shuffle = Shuffle.shuffle(world.size());
      // Now perform the operation shifting cell contents as appropriate,
      // at most two layers of cells are transfered
      for (int i = 0; i < shuffle.length; ++i) {
        if (i != shuffle[i]) {
          final Cell c1 = world.getCell(i);
          final Cell c2 = world.getCell(shuffle[i]);
          world.notify(new WeaponEffectEvent(c1, c2, WeaponEffectType.LINE));
          final Actor top1 = c1.pop();
          final Actor bot1 = c1.pop();
          final Actor top2 = c2.pop();
          final Actor bot2 = c2.pop();
          if (bot1 != null) {
            c2.push(bot1);
          }
          if (top1 != null) {
            c2.push(top1);
          }
          if (bot2 != null) {
            c1.push(bot2);
          }
          if (top2 != null) {
            c1.push(top2);
          }
          c1.notify(new CellEffectEvent(c1, CellEffectType.REDRAW_CELL));
          c2.notify(new CellEffectEvent(c2, CellEffectType.REDRAW_CELL));
        }
      }
    }
  }
}
