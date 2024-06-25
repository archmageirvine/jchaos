package chaos.common.monster;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.common.UndeadMonster;

/**
 * Solar.
 * @author Sean A. Irvine
 */
public class Solar extends UndeadMonster implements Humanoid, NoDeadImage {
  {
    setDefault(Attribute.LIFE, 23);
    setDefault(Attribute.LIFE_RECOVERY, 2);
    setDefault(Attribute.MAGICAL_RESISTANCE, 86);
    setDefault(Attribute.INTELLIGENCE, 40);
    setDefault(Attribute.COMBAT, 5);
    setDefault(Attribute.AGILITY, 33);
    setDefault(Attribute.MOVEMENT, 1);
  }

  @Override
  public long getLosMask() {
    return 0x387C7C7C7C786800L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Ghoul.class;
  }

  /**
   * Solar has non-standard movement.  It always moves itself, so the
   * update makes sure ordinary players cannot move it.
   * @param world the world containing the actor, may be null
   * @param cell the cell containing the actor. may be null
   * @return update success
   */
  @Override
  public boolean update(final World world, final Cell cell) {
    final boolean r = super.update(world, cell);
    setMoved(true);
    return r;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell) {
    super.cast(world, caster, cell, casterCell);
    setMoved(true);
  }
}
