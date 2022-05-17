package chaos.common.inanimate;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Animateable;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.Conveyance;
import chaos.common.Meditation;
import chaos.common.Multiplicity;
import chaos.common.Realm;
import chaos.common.Tree;
import chaos.common.monster.WoodElf;
import chaos.util.CastUtils;

/**
 * Magic wood.
 *
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class MagicWood extends Actor implements Meditation, Multiplicity, Tree, Animateable {
  {
    setDefault(Attribute.LIFE, 22);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setRealm(Realm.MATERIAL);
  }
  @Override
  public long getLosMask() {
    return 0x7CFEFE7E3C183C7CL;
  }
  @Override
  public int getCastRange() {
    return 8;
  }
  @Override
  public int getCastFlags() {
    return CAST_GROWTH | CAST_EMPTY | CAST_LOS;
  }
  @Override
  public int getMultiplicity() {
    return 8;
  }
  @Override
  public void cast(final World world, final Caster caster, final Cell c, final Cell casterCell) {
    CastUtils.castTree(this, caster, c, casterCell);
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
    return 2;
  }
  @Override
  public boolean freeCollapse() {
    return false;
  }
  @Override
  public int collapseFactor() {
    return 3;
  }
  @Override
  public Actor getAnimatedForm() {
    final Actor a = new WoodElf();
    a.setOwner(getOwner());
    return a;
  }
}
