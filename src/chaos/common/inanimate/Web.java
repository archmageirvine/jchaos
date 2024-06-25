package chaos.common.inanimate;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Animateable;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.Inanimate;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.Multiplicity;
import chaos.common.Realm;
import chaos.common.monster.Spider;
import chaos.util.CastUtils;

/**
 * Web.
 * @author Sean A. Irvine
 */
public class Web extends MaterialMonster implements Multiplicity, Inanimate, Animateable {
  {
    setDefault(Attribute.LIFE, 12);
    setDefault(Attribute.SPECIAL_COMBAT, 3);
    setRealm(Realm.MATERIAL);
    setSpecialCombatApply(Attribute.AGILITY);
  }

  @Override
  public int getCastRange() {
    return 8;
  }

  @Override
  public long getLosMask() {
    return 0x0L;
  }

  @Override
  public int getCastFlags() {
    return CAST_GROWTH | CAST_EMPTY | CAST_LOS;
  }

  @Override
  public Actor getAnimatedForm() {
    final Actor a = new Spider();
    a.setOwner(getOwner());
    return a;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell c, final Cell casterCell) {
    CastUtils.castStone(this, caster, c, casterCell);
  }

  @Override
  public int getMultiplicity() {
    return 6;
  }

  @Override
  public int getDefaultWeight() {
    return 0;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return null;
  }
}
