package chaos.common.free;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.Realm;
import chaos.util.CellEffectType;
import chaos.util.PolycellEffectEvent;

/**
 * Materialize.
 * @author Sean A. Irvine
 */
public class Materialize extends FreeCastable {

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null) {
      final HashSet<Cell> affected = new HashSet<>();
      for (int i = 0; i < world.size(); ++i) {
        final Actor a = world.actor(i);
        if (a != null && a.getRealm() != Realm.MATERIAL) {
          affected.add(world.getCell(i));
        }
      }
      world.notify(new PolycellEffectEvent(affected, CellEffectType.CHANGE_REALM, caster));
      for (final Cell c : affected) {
        c.peek().setRealm(Realm.MATERIAL);
      }
      world.notify(new PolycellEffectEvent(affected, CellEffectType.REDRAW_CELL));
    }
  }
}
