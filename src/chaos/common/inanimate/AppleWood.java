package chaos.common.inanimate;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Animateable;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.Conveyance;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.Multiplicity;
import chaos.common.NoDeadImage;
import chaos.common.Tree;
import chaos.common.monster.GrayElf;
import chaos.util.CastUtils;

/**
 * Apple wood.
 *
 * @author Sean A. Irvine
 */
public class AppleWood extends MaterialMonster implements Multiplicity, Tree, NoDeadImage, Animateable, Conveyance {
  {
    setDefault(Attribute.LIFE, 20);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.INTELLIGENCE, 90);
    setDefault(Attribute.LIFE_RECOVERY, 1);
    setDefault(Attribute.SPECIAL_COMBAT, -7);
    setSpecialCombatApply(Attribute.MAGICAL_RESISTANCE);
  }
  @Override
  public long getLosMask() {
    return 0x3EFFFFFFFF3E183CL;
  }
  @Override
  public int getCastRange() {
    return 7;
  }
  @Override
  public int getCastFlags() {
    return CAST_GROWTH | CAST_EMPTY | CAST_LOS;
  }
  @Override
  public Actor getAnimatedForm() {
    final Actor a = new GrayElf();
    a.setOwner(getOwner());
    return a;
  }
  @Override
  public void cast(final World world, final Caster caster, final Cell c, final Cell casterCell) {
    CastUtils.castTree(this, caster, c, casterCell);
  }
  @Override
  public int getMultiplicity() {
    return 2;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return null;
  }
  @Override
  public int getDefaultWeight() {
    return 1;
  }

  private Actor mMount;

  @Override
  public Actor getMount() {
    return mMount;
  }

  @Override
  public void setMount(final Actor actor) {
    Conveyance.checkMount(this, actor);
    mMount = actor;
  }
}
