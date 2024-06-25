package chaos.common;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.util.CellEffectType;
import chaos.util.PolycellEffectEvent;
import chaos.util.PolyshieldEvent;

/**
 * Abstract superclass for spells that increment an attribute of all creations
 * with the same team as the caster.
 * @author Sean A. Irvine
 */
public abstract class AbstractIncrement extends FreeCastable {

  /**
   * The increment to be applied.
   * @return increment
   */
  public abstract int increment();

  /**
   * The attribute to be incremented.
   * @return attribute
   */
  public abstract Attribute attribute();

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null && caster != null) {
      // Increment is to be applied to the caster and all exposed items owned by
      // the caster.  This extra check helps ensure the increment is applied to
      // the wizard when mounted etc.
      final HashSet<Cell> affected = new HashSet<>();
      if (casterCell != null) {
        affected.add(casterCell);
      }
      caster.increment(attribute(), increment());
      final Attribute at = attribute();
      final int inc = increment();
      final int p = caster.getOwner();
      final Team ti = world.getTeamInformation();
      final int team = ti.getTeam(p);
      for (int i = 0; i < world.size(); ++i) {
        final Actor a = world.actor(i);
        if (a != null && a != caster && a.getState() == State.ACTIVE && team == ti.getTeam(a)) {
          affected.add(world.getCell(i));
          a.increment(at, inc);
        }
      }
      world.notify(new PolyshieldEvent(affected, at, caster));
      world.notify(new PolycellEffectEvent(affected, CellEffectType.REDRAW_CELL, caster));
    }
  }
}
