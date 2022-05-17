package chaos.common.inanimate;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Animateable;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.Inanimate;
import chaos.common.Realm;
import chaos.common.TargetFilter;
import chaos.common.monster.Marid;
import chaos.util.CastUtils;

/**
 * Mana battery.
 *
 * @author Sean A. Irvine
 */
public class ManaBattery extends Actor implements Inanimate, Animateable, TargetFilter {
  {
    setDefault(Attribute.LIFE, 50);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY, 25);
    setRealm(Realm.CORE);
  }
  @Override
  public int getCastRange() {
    return 12;
  }
  @Override
  public Actor getAnimatedForm() {
    final Actor a = new Marid();
    a.setOwner(getOwner());
    return a;
  }
  @Override
  public long getLosMask() {
    return 0x003C7E42FFFFFFFFL;
  }
  @Override
  public int getCastFlags() {
    return Castable.CAST_GROWTH | Castable.CAST_EMPTY | Castable.CAST_DEAD | Castable.CAST_LOS;
  }
  @Override
  public void cast(final World world, final Caster caster, final Cell c, final Cell casterCell) {
    CastUtils.castStone(this, caster, c, casterCell);
  }
  @Override
  public int getDefaultWeight() {
    return 50;
  }
  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    CastUtils.keepClosest(targets, caster, world);
  }
}
