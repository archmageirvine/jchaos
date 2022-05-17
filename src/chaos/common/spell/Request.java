package chaos.common.spell;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.FrequencyTable;
import chaos.common.Monster;

/**
 * Request.
 *
 * @author Sean A. Irvine
 */
public class Request extends Castable {

  @Override
  public int getCastFlags() {
    return CAST_EMPTY | CAST_DEAD;
  }
  @Override
  public int getCastRange() {
    return 1;
  }
  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    // This spell simply proxies the activity to the actual randomly selected
    // creature.
    Actor a;
    while (!((a = FrequencyTable.DEFAULT.getUniformRandomMonster()) instanceof Monster)) {
      // do nothing
    }
    a.cast(world, caster, cell, casterCell);
  }
}
