package chaos.common.monster;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.common.State;
import chaos.common.UndeadMonster;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Random;

/**
 * Haunt.
 * @author Sean A. Irvine
 */
public class Haunt extends UndeadMonster implements NoDeadImage {

  /** Probability of changing sides. */
  private static final int CHANGE_SIDES_FACTOR = 170;

  {
    setDefault(Attribute.LIFE, 10);
    setDefault(Attribute.LIFE_RECOVERY, 6);
    setDefault(Attribute.INTELLIGENCE, 40);
    setDefault(Attribute.MAGICAL_RESISTANCE, 14);
    setDefault(Attribute.COMBAT, 3);
    setDefault(Attribute.AGILITY, 40);
    setDefault(Attribute.MOVEMENT, 2);
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Spectre.class;
  }

  @Override
  public boolean update(final World world, final Cell cell) {
    if (super.update(world, cell)) {
      return true;
    }
    // haunts can change sides
    if (world != null && getState() == State.ACTIVE && Random.nextInt(CHANGE_SIDES_FACTOR) == 0) {
      // +/- 1 ensure we don't assign to the "nobody" player
      setOwner(1 + Random.nextInt(world.getWizardManager().getMaximumPlayerNumber() - 1));
      if (cell != null) {
        cell.notify(new CellEffectEvent(cell, CellEffectType.OWNER_CHANGE));
      }
    }
    return false;
  }

}

