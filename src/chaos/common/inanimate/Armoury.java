package chaos.common.inanimate;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Animateable;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.Inanimate;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.Realm;
import chaos.common.monster.GrayElf;
import chaos.util.CastUtils;

/**
 * Armoury.
 * @author Sean A. Irvine
 */
public class Armoury extends Monster implements Inanimate, Animateable {

  @Override
  public int getCastRange() {
    return 5;
  }

  @Override
  public long getLosMask() {
    return ~0;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell c, final Cell casterCell) {
    CastUtils.castStone(this, caster, c, casterCell);
  }

  @Override
  public Actor getAnimatedForm() {
    final Actor a = new GrayElf();
    a.setOwner(getOwner());
    return a;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return null;
  }

  {
    setRealm(Realm.CORE);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.COMBAT);
    setDefault(Attribute.LIFE, 63);
    setDefault(Attribute.SPECIAL_COMBAT, -1);
    set(PowerUps.TALISMAN, 6);
  }
}
