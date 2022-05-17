package chaos.common.inanimate;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Animateable;
import chaos.common.Attribute;
import chaos.common.Blocker;
import chaos.common.Bonus;
import chaos.common.Caster;
import chaos.common.Inanimate;
import chaos.common.Multiplicity;
import chaos.common.Realm;
import chaos.common.Wall;
import chaos.common.monster.StoneGiant;
import chaos.util.CastUtils;

/**
 * Rock.
 *
 * @author Sean A. Irvine
 */
public class Rock extends Actor implements Inanimate, Bonus, Animateable, Wall, Multiplicity, Blocker {
  {
    setDefault(Attribute.LIFE, 63);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setRealm(Realm.MATERIAL);
  }
  @Override
  public int getCastRange() {
    return 7;
  }
  @Override
  public long getLosMask() {
    return 0x1C3C7FFFFFFD1C1CL;
  }
  @Override
  public int getBonus() {
    return 1;
  }
  @Override
  public int getCastFlags() {
    return CAST_GROWTH | CAST_EMPTY | CAST_LOS | CAST_DEAD;
  }
  @Override
  public void cast(final World world, final Caster caster, final Cell c, final Cell casterCell) {
    CastUtils.castStone(this, caster, c, casterCell);
  }
  @Override
  public int getDefaultWeight() {
    return 0;
  }
  @Override
  public Actor getAnimatedForm() {
    final Actor a = new StoneGiant();
    a.setOwner(getOwner());
    return a;
  }
  @Override
  public int getMultiplicity() {
    return 3;
  }
}
