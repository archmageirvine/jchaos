package chaos.common.inanimate;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Animateable;
import chaos.common.Attribute;
import chaos.common.Blocker;
import chaos.common.Caster;
import chaos.common.Monster;
import chaos.common.Multiplicity;
import chaos.common.NoDeadImage;
import chaos.common.Tree;
import chaos.common.UndeadMonster;
import chaos.common.monster.Baboon;
import chaos.util.CastUtils;

/**
 * Dark wood.
 *
 * @author Sean A. Irvine
 */
public class DarkWood extends UndeadMonster implements Multiplicity, Tree, NoDeadImage, Blocker, Animateable {

  {
    setDefault(Attribute.LIFE, 20);
    setDefault(Attribute.INTELLIGENCE, 60);
    setDefault(Attribute.LIFE_RECOVERY, 2);
    setDefault(Attribute.SPECIAL_COMBAT, 2);
  }

  @Override
  public long getLosMask() {
    return 0x30FFEEFE7E7E3C18L;
  }
  @Override
  public int getCastRange() {
    return 12;
  }
  @Override
  public int getCastFlags() {
    return CAST_GROWTH | CAST_EMPTY | CAST_LOS;
  }
  @Override
  public Actor getAnimatedForm() {
    final Actor a = new Baboon();
    a.setOwner(getOwner());
    return a;
  }
  @Override
  public void cast(final World world, final Caster caster, final Cell c, final Cell casterCell) {
    CastUtils.castTree(this, caster, c, casterCell);
  }
  @Override
  public int getMultiplicity() {
    return 6;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return null;
  }
  @Override
  public int getDefaultWeight() {
    return -1;
  }
}
