package chaos.common.inanimate;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Animateable;
import chaos.common.Attribute;
import chaos.common.Blocker;
import chaos.common.Caster;
import chaos.common.IgnoreShot;
import chaos.common.Inanimate;
import chaos.common.Killer;
import chaos.common.Multiplicity;
import chaos.common.Realm;
import chaos.common.monster.GiantBeetle;
import chaos.util.CastUtils;

/**
 * Pit.
 * @author Sean A. Irvine
 */
public class Pit extends Actor implements Multiplicity, Inanimate, Animateable, IgnoreShot, Blocker, Killer {
  {
    setDefault(Attribute.LIFE, 63);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setRealm(Realm.MATERIAL);
  }

  @Override
  public int getCastRange() {
    return MAX_CAST_RANGE;
  }

  @Override
  public long getLosMask() {
    return 0x0L;
  }

  @Override
  public int getCastFlags() {
    return CAST_GROWTH | CAST_EMPTY | CAST_DEAD;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell c, final Cell casterCell) {
    CastUtils.castStone(this, caster, c, casterCell);
  }

  @Override
  public int getMultiplicity() {
    return 8;
  }

  @Override
  public int getDefaultWeight() {
    return -7;
  }

  @Override
  public Actor getAnimatedForm() {
    final Actor a = new GiantBeetle();
    a.setOwner(getOwner());
    return a;
  }
}
