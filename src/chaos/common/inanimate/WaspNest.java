package chaos.common.inanimate;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Animateable;
import chaos.common.Attribute;
import chaos.common.Blocker;
import chaos.common.Caster;
import chaos.common.Inanimate;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.common.PowerUps;
import chaos.common.monster.HigherDevil;
import chaos.util.CastUtils;

/**
 * Wasp nest.
 * @author Sean A. Irvine
 */
public class WaspNest extends MaterialMonster implements Inanimate, NoDeadImage, Animateable, Blocker {
  {
    setDefault(Attribute.LIFE, 60);
    setDefault(Attribute.LIFE_RECOVERY, 5);
    setDefault(Attribute.MAGICAL_RESISTANCE, 28);
    setDefault(Attribute.SPECIAL_COMBAT, 6);
    set(PowerUps.TALISMAN, 2);
  }

  @Override
  public long getLosMask() {
    return 0x3C7E7E7E7E7E7E7EL;
  }

  @Override
  public int getCastRange() {
    return 9;
  }

  @Override
  public int getCastFlags() {
    return CAST_GROWTH | CAST_EMPTY | CAST_LOS | CAST_DEAD;
  }

  @Override
  public Actor getAnimatedForm() {
    final Actor a = new HigherDevil();
    a.setOwner(getOwner());
    return a;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell c, final Cell casterCell) {
    CastUtils.castStone(this, caster, c, casterCell);
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
