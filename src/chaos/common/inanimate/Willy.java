package chaos.common.inanimate;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Animateable;
import chaos.common.Attribute;
import chaos.common.Blocker;
import chaos.common.Caster;
import chaos.common.Inanimate;
import chaos.common.Realm;
import chaos.common.monster.Faun;
import chaos.util.CastUtils;

/**
 * Willy.
 *
 * @author Sean A. Irvine
 */
public class Willy extends Actor implements Inanimate, Animateable, Blocker {
  {
    setDefault(Attribute.LIFE, 63);
    setDefault(Attribute.LIFE_RECOVERY, 7);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 7);
    setRealm(Realm.MATERIAL);
  }
  @Override
  public int getCastRange() {
    return 7;
  }
  @Override
  public long getLosMask() {
    return 0x383838387C7E3E36L;
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
  public Actor getAnimatedForm() {
    final Actor a = new Faun();
    a.setOwner(getOwner());
    return a;
  }

}
