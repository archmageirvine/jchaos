package chaos.common.inanimate;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.Multiplicity;
import chaos.common.PowerUps;
import chaos.common.Realm;
import chaos.util.CastUtils;

/**
 * Dancing Daisy.
 * @author Sean A. Irvine
 */
public class DancingDaisy extends MaterialMonster implements Multiplicity {
  {
    setDefault(Attribute.LIFE, 1);
    setDefault(Attribute.SPECIAL_COMBAT, -1);
    setRealm(Realm.MATERIAL);
    setSpecialCombatApply(Attribute.INTELLIGENCE);
    set(PowerUps.TALISMAN, 3);
  }

  @Override
  public int getCastRange() {
    return 12;
  }

  @Override
  public long getLosMask() {
    return 0x003C3C3C3C181800L;
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
  public int getMultiplicity() {
    return 4;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return null;
  }
}
