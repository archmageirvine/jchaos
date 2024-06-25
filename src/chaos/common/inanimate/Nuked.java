package chaos.common.inanimate;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Blocker;
import chaos.common.Caster;
import chaos.common.IgnoreShot;
import chaos.common.Inanimate;
import chaos.common.Killer;
import chaos.common.Multiplicity;
import chaos.common.Realm;

/**
 * The nuked cell.
 * @author Sean A. Irvine
 */
public class Nuked extends Actor implements Multiplicity, Inanimate, IgnoreShot, Blocker, Killer {
  {
    setDefault(Attribute.LIFE, 0);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setRealm(Realm.MATERIAL);
  }

  @Override
  public int getCastRange() {
    return 15;
  }

  @Override
  public long getLosMask() {
    return 0x0L;
  }

  @Override
  public int getCastFlags() {
    return 0;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell c, final Cell casterCell) {
    // In theory this does not get cast as a spell, so no casting is needed.
  }

  @Override
  public int getMultiplicity() {
    return 8;
  }

  @Override
  public int getDefaultWeight() {
    return -7;
  }
}
