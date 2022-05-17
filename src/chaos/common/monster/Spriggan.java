package chaos.common.monster;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.State;
import chaos.util.Random;

/**
 * Spriggan.
 *
 * @author Sean A. Irvine
 */
public class Spriggan extends MaterialMonster implements Humanoid {
  {
    setDefault(Attribute.LIFE, 12);
    setDefault(Attribute.MAGICAL_RESISTANCE, 29);
    setDefault(Attribute.AGILITY, 40);
    setDefault(Attribute.INTELLIGENCE, 67);
    setDefault(Attribute.MOVEMENT, 1);
    setDefault(Attribute.COMBAT, 3);
  }
  @Override
  public long getLosMask() {
    return 0x061F3F7F7C3C3C00L;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return Goblin.class;
  }

  /** Initial probability of sleep. */
  private static final int SLEEP_FACTOR = 220;

  /** The smaller this number the more likely the spriggan is to fall asleep. */
  private int mSleep = SLEEP_FACTOR;

  /**
   * Differs from the standard update, in that it handles the spriggan going
   * to sleep.
   *
   * @param world the world
   * @param cell cell containing this actor
   * @return as defined in superclass
   */
  @Override
  public boolean update(final World world, final Cell cell) {
    if (super.update(world, cell)) {
      return true;
    } else {
      // active spriggans sometimes fall asleep, when mSleep reaches
      // one, sleeping is guaranteed
      if (getState() == State.ACTIVE && Random.nextInt(mSleep--) == 0) {
        setOwner(0);
        setState(State.ASLEEP);
        mSleep = SLEEP_FACTOR;
      }
      return false;
    }
  }

}
