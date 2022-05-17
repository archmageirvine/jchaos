package chaos.engine;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Conveyance;
import chaos.common.Growth;
import chaos.common.Meditation;
import chaos.common.State;
import chaos.common.wizard.Wizard;
import chaos.common.monster.GoblinBomb;

/**
 * Field for a wizard. This reflects the desirability of a cell from a
 * wizard point of view. It considers only the content of the cell and
 * not the surroundings.
 *
 * @author Sean A. Irvine
 */
public class WizardField implements ScalarField {

  private static final double SCALE = 1.0 / Attribute.LIFE.max();
  private static final double COMBAT_SCALE = Attribute.COMBAT.max() / 15.0;

  private final World mWorld;
  private final int mOwner;
  private final int mTeam;
  private final boolean mExposedWizard;

  WizardField(final World world, final Wizard wizard) {
    mWorld = world;
    mOwner = wizard.getOwner();
    mTeam = mWorld.getTeamInformation().getTeam(mOwner);
    final Cell wc = world.getCell(wizard);
    mExposedWizard = wc != null && wc.peek() == wizard;
  }

  private double simpleWeight(final Actor a) {
    final double w = (a.get(Attribute.LIFE)
                      + (a.get(Attribute.COMBAT)
                         + a.get(Attribute.RANGED_COMBAT)
                         + a.get(Attribute.SPECIAL_COMBAT)) * COMBAT_SCALE) * 0.25 * SCALE;
    if (a instanceof Conveyance) {
      final Actor m = ((Conveyance) a).getMount();
      if (m != null) {
        return 0.5 * (w + simpleWeight(m));
      }
    }
    // Things that can't move are usually less dangerous
    final int movement = a.get(Attribute.MOVEMENT);
    return movement == 0 ? 0.2 * w : w;
  }

  @Override
  public double weight(final int cell) {
    final Actor a = mWorld.actor(cell);
    if (a == null) {
      return 0; // baseline, empty cell
    }
    final State state = a.getState();
    if (state == State.DEAD || state == State.ASLEEP) {
      return -0.001; // dead and asleep, only very slightly worse than an empty cell
    }
    if (a instanceof Meditation && ((Meditation) a).getMount() == null) {
      // Unoccupied meditations are highly desirable
      return mExposedWizard ? 1 : 0.8;
    }
    final int owner = a.getOwner();
    if (a instanceof Growth) {
      final Growth g = (Growth) a;
      if (g.getGrowthType() != Growth.GROW_OVER || owner != mOwner) {
        return -0.99;
      }
    }
    if (a instanceof GoblinBomb) {
      return -0.4;
    }
    if (owner == mOwner) {
      // Attraction to own things
      if (mExposedWizard && a instanceof Conveyance) {
        return 1;
      }
      return 0.2 * simpleWeight(a);
    }
    final int team = mWorld.getTeamInformation().getTeam(owner);
    if (team == mTeam) {
      // Attraction to own team
      return 0.05 * simpleWeight(a);
    }
    return -simpleWeight(a);
  }
}
