package chaos.common.free;

import java.util.ArrayList;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.PowerUps;
import chaos.common.Realm;
import chaos.common.State;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Random;
import chaos.util.RankingTable;
import chaos.util.Restore;

/**
 * Life giver.
 *
 * @author Sean A. Irvine
 * @author Gregory B. Irvine
 */
public class LifeGiver extends FreeCastable {

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null && caster != null) {
      final ArrayList<Integer> dead = new ArrayList<>();
      for (int k = 0; k < world.size(); ++k) {
        final Actor a = world.actor(k);
        if (a != null && a.getState() == State.DEAD) {
          dead.add(k);
        }
      }
      if (dead.isEmpty()) {
        // Failed to find target.  In this case, there is no target cell with which
        // we can identify the attempt; therefore we resort to a SPELL_FAILED event
        // with the notification going to the caster's cell.
        if (casterCell != null) {
          casterCell.notify(new CellEffectEvent(casterCell, CellEffectType.SPELL_FAILED));
        }
      } else {
        int best = Random.nextInt(dead.size());
        for (int i = 0; i < caster.get(PowerUps.LEVEL); ++i) {
          final int p = Random.nextInt(dead.size());
          if (RankingTable.getRanking(world.actor(dead.get(p))) > RankingTable.getRanking(world.actor(best))) {
            best = p;
          }
        }
        if (best != -1) { // Should actually always be >= 0
          final int cellNumber = dead.get(best);
          final Cell cell = world.getCell(cellNumber);
          cell.notify(new CellEffectEvent(cell, CellEffectType.RAISE_DEAD, caster));
          final Actor a = world.actor(cellNumber);
          a.setOwner(caster.getOwner());
          a.setRealm(Realm.ETHERIC);
          Restore.restore(a);
          cell.notify(new CellEffectEvent(cell, CellEffectType.REDRAW_CELL));
        }
      }
    }
  }
}
