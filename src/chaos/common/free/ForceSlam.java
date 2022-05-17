package chaos.common.free;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.State;
import chaos.util.AudioEvent;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Random;
import chaos.util.RingCellEffectEvent;
import chaos.util.Sleep;

/**
 * Force slam.
 * @author Sean A. Irvine
 */
public class ForceSlam extends FreeCastable {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null && casterCell != null) {
      final Team team = world.getTeamInformation();
      final int t = team.getTeam(caster);
      final int centre = casterCell.getCellNumber();
      final HashSet<Actor> wasMoved = new HashSet<>();
      final ArrayList<Actor> content = new ArrayList<>();
      do {
        wasMoved.clear();
        content.clear();
        // Do one ring at a time heading outwards from the caster, keep going until
        // there are no more possible cells.
        int radius = 0;
        Set<Cell> ring;
        casterCell.notify(new AudioEvent(centre, caster, "power"));
        while (!(ring = world.getCells(centre, ++radius, radius, false)).isEmpty()) {
          casterCell.notify(new RingCellEffectEvent(centre, radius - 1, new Color(64 + Random.nextInt(192), 0, 0)));
          // Within the ring, test if we can move its creations outwards
          for (final Cell c : ring) {
            if (c.peek() != null && !wasMoved.contains(c.peek()) && (team.getTeam(c.peek()) != t || c.peek().getState() != State.ACTIVE)) {
              final int cc = c.getCellNumber();
              final int sqDistance = world.getSquaredDistance(centre, cc);
              Cell choice = null;
              int choiceCount = 0;
              for (final Cell adj : world.getCells(cc, 1, 1, false)) {
                if (adj.peek() == null && world.getSquaredDistance(centre, adj.getCellNumber()) > sqDistance && Random.nextInt(++choiceCount) == 0) {
                  choice = adj;
                }
              }
              if (choice != null) {
                content.clear();
                while (c.peek() != null) {
                  content.add(c.pop());
                }
                for (int k = content.size() - 1; k >= 0; --k) {
                  choice.push(content.get(k));
                }
                wasMoved.add(choice.peek());
                c.notify(new CellEffectEvent(c, CellEffectType.REDRAW_CELL));
                choice.notify(new CellEffectEvent(choice, CellEffectType.REDRAW_CELL));
              }
            }
          }
        }
        Sleep.sleep(20);
      } while (!wasMoved.isEmpty());
    }
  }
}
