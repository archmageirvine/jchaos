package chaos.common.inanimate;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Animateable;
import chaos.common.Attribute;
import chaos.common.Blocker;
import chaos.common.Caster;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.Multiplicity;
import chaos.common.NoDeadImage;
import chaos.common.Tree;
import chaos.common.monster.Gorilla;
import chaos.util.CastUtils;

/**
 * Shadow wood.
 * @author Sean A. Irvine
 * @author Julian Gollop
 */
public class ShadowWood extends MaterialMonster implements Multiplicity, Tree, NoDeadImage, Animateable, Blocker {
  {
    setDefault(Attribute.LIFE, 20);
    setDefault(Attribute.COMBAT, 2);
    setDefault(Attribute.INTELLIGENCE, 60);
    setDefault(Attribute.LIFE_RECOVERY, 2);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
  }

  @Override
  public long getLosMask() {
    return 0x345EFE7F3A181C36L;
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

  @Override
  public Actor getAnimatedForm() {
    final Actor a = new Gorilla();
    a.setOwner(getOwner());
    return a;
  }
}
