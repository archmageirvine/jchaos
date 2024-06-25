package chaos.common;

import chaos.board.Cell;
import chaos.board.World;
import chaos.util.CastUtils;

/**
 * Abstract wall.  Implements common functionality of all walls.
 * @author Sean A. Irvine
 */
public abstract class AbstractWall extends Actor implements Inanimate, Animateable, Wall, Blocker {
  @Override
  public long getLosMask() {
    return ~0L;
  }

  @Override
  public int getCastFlags() {
    return Castable.CAST_GROWTH | Castable.CAST_EMPTY | Castable.CAST_LOS;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell c, final Cell casterCell) {
    CastUtils.castStone(this, caster, c, casterCell);
  }

  @Override
  public int getDefaultWeight() {
    return 0;
  }

  {
    setRealm(Realm.MATERIAL);
  }
}
