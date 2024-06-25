package chaos.common.inanimate;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Animateable;
import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Caster;
import chaos.common.Conveyance;
import chaos.common.Inanimate;
import chaos.common.Meditation;
import chaos.common.Realm;
import chaos.common.TargetFilter;
import chaos.common.monster.Wraith;
import chaos.util.CastUtils;

/**
 * Shadow city.
 *
 * @author Sean A. Irvine
 */
public class ShadowCity extends Actor implements Meditation, Bonus, Inanimate, Animateable, TargetFilter {
  {
    setDefault(Attribute.LIFE, 63);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setRealm(Realm.ETHERIC);
  }
  @Override
  public long getLosMask() {
    return 0x4266E77EFFFFE7E7L;
  }
  @Override
  public int getCastRange() {
    return 4;
  }
  @Override
  public int getCastFlags() {
    return CAST_EMPTY | CAST_LOS;
  }
  @Override
  public int getBonus() {
    return 10;
  }
  @Override
  public void cast(final World world, final Caster caster, final Cell c, final Cell casterCell) {
    CastUtils.castMeditation(this, caster, c, casterCell);
  }
  private Actor mMount = null;
  @Override
  public Actor getMount() {
    return mMount;
  }
  @Override
  public void setMount(final Actor actor) {
    Conveyance.checkMount(this, actor);
    mMount = actor;
  }
  @Override
  public int getDefaultWeight() {
    return 1;
  }
  @Override
  public boolean freeCollapse() {
    return true;
  }
  @Override
  public int collapseFactor() {
    return 6;
  }
  @Override
  public Actor getAnimatedForm() {
    final Actor a = new Wraith();
    a.setOwner(getOwner());
    return a;
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    CastUtils.keepClosest(targets, caster, world);
  }
}
