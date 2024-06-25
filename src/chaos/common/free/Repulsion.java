package chaos.common.free;

import java.util.ArrayList;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Caster;
import chaos.common.FreeCastable;

/**
 * Repulsion.
 * @author Sean A. Irvine
 */
public class Repulsion extends FreeCastable {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  /**
   * Perform a repulsion with the given delta values.  At least one of <code>dx</code>
   * <code>dy</code> should be nonzero and both of them should have absolute value at
   * most 1.
   * @param world the world
   * @param c origin
   * @param dx x delta
   * @param dy y delta
   */
  private static void repel(final World world, final Cell c, final int dx, final int dy) {
    final int cn = c.getCellNumber();
    final int w = world.width();
    int x = cn % w;
    int y = cn / w;
    final int ox = x;
    final int oy = y;
    final ArrayList<ArrayList<Actor>> retain = new ArrayList<>();
    x += dx;
    y += dy;
    Cell t;
    while ((t = world.getCell(x, y)) != null) {
      final ArrayList<Actor> cellActorList = new ArrayList<>();
      while (t.peek() != null) {
        cellActorList.add(t.pop());
      }
      if (!cellActorList.isEmpty()) {
        retain.add(cellActorList);
      }
      x += dx;
      y += dy;
    }
    x -= dx;
    y -= dy;
    int s = retain.size();
    while (s > 0) {
      final Cell cell = world.getCell(x, y);
      //cell.pop(); // is this pop needed??
      final ArrayList<Actor> toPush = retain.get(--s);
      for (int k = toPush.size() - 1; k >= 0; --k) {
        cell.push(toPush.get(k));
      }
      x -= dx;
      y -= dy;
    }
    while (x != ox || y != oy) {
      world.getCell(x, y).pop();
      x -= dx;
      y -= dy;
    }
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null && casterCell != null) {
      repel(world, casterCell, 1, 0);
      repel(world, casterCell, -1, -1);
      repel(world, casterCell, -1, 1);
      repel(world, casterCell, 0, -1);
      repel(world, casterCell, 1, 1);
      repel(world, casterCell, -1, 0);
      repel(world, casterCell, 1, -1);
      repel(world, casterCell, 0, 1);
    }
  }
}
