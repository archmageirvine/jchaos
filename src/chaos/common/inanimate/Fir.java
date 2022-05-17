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
import chaos.common.Realm;
import chaos.common.Tree;
import chaos.common.monster.Ogre;
import chaos.util.CastUtils;

/**
 * Fir.
 *
 * @author Sean A. Irvine
 */
public class Fir extends MaterialMonster implements Tree, Animateable, Multiplicity, Blocker {
  {
    setDefault(Attribute.LIFE, 49);
    setDefault(Attribute.LIFE_RECOVERY, 7);
    setDefault(Attribute.MAGICAL_RESISTANCE, 42);
    setDefault(Attribute.SPECIAL_COMBAT, 1);
    setRealm(Realm.MATERIAL);
    setSpecialCombatApply(Attribute.MOVEMENT);
  }
  @Override
  public int getCastRange() {
    return 7;
  }
  @Override
  public long getLosMask() {
    return 0x18183C3C3C7E7E18L;
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
  public int getDefaultWeight() {
    return 0;
  }
  @Override
  public Actor getAnimatedForm() {
    final Actor a = new Ogre();
    a.setOwner(getOwner());
    return a;
  }
  @Override
  public int getMultiplicity() {
    return 4;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return null;
  }
}
