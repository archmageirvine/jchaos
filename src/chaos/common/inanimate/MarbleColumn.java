package chaos.common.inanimate;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Animateable;
import chaos.common.Attribute;
import chaos.common.Blocker;
import chaos.common.Caster;
import chaos.common.Inanimate;
import chaos.common.Multiplicity;
import chaos.common.Realm;
import chaos.common.Wall;
import chaos.common.monster.Derro;
import chaos.util.CastUtils;

/**
 * Marble column.
 * @author Sean A. Irvine
 */
public class MarbleColumn extends Actor implements Inanimate, Animateable, Wall, Multiplicity, Blocker {
  {
    setDefault(Attribute.LIFE, 36);
    setDefault(Attribute.MAGICAL_RESISTANCE, 16);
    setRealm(Realm.MATERIAL);
  }

  @Override
  public int getCastRange() {
    return 5;
  }

  @Override
  public long getLosMask() {
    return 0x3C1818181818183CL;
  }

  @Override
  public int getCastFlags() {
    return CAST_GROWTH | CAST_EMPTY | CAST_LOS | CAST_DEAD;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell c, final Cell casterCell) {
    CastUtils.castStone(this, caster, c, casterCell);
  }

  @Override
  public int getDefaultWeight() {
    return 0;
  }

  @Override
  public Actor getAnimatedForm() {
    final Actor a = new Derro();
    a.setOwner(getOwner());
    return a;
  }

  @Override
  public int getMultiplicity() {
    return 4;
  }
}
