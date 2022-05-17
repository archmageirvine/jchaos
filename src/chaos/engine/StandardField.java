package chaos.engine;

import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Killer;
import chaos.common.State;
import chaos.common.monster.Gollop;
import chaos.common.wizard.Wizard;

/**
 * Desirability of a cell from an ordinary movement point of view. This
 * is not suitable for use with wizards.
 *
 * @author Sean A. Irvine
 */
public class StandardField implements ScalarField {

  private final World mWorld;
  private final int mOwner;
  private final int mTeam;

  StandardField(final World world, final Wizard wizard) {
    mWorld = world;
    mOwner = wizard.getOwner();
    mTeam = mWorld.getTeamInformation().getTeam(mOwner);
  }

  @Override
  public double weight(final int cell) {
    final Actor a = mWorld.actor(cell);
    if (a == null) {
      return 0; // baseline, empty cell
    }
    final State state = a.getState();
    if (state == State.DEAD || state == State.ASLEEP) {
      return 0;
    }
    if (a instanceof Killer) {
      return -0.001;
    }
    final int owner = a.getOwner();
    if (owner == mOwner) {
      if (a instanceof Gollop) {
        return 1;
      }
      return -0.0001; // Don't hang together
    }
    final int team = mWorld.getTeamInformation().getTeam(owner);
    if (team == mTeam) {
      return -0.001; // Skip away from own team
    }
    // Division gets the weight down to the scale approx [0,1] care about.
    final double lethal = Weight.lethality(a);
    if (Double.isNaN(lethal)) {
      System.out.println("NaN for " + a.getName() + " " + a.getState() + " " + a.getOwner());
      return 0;
    }
    return lethal / 150.0;
  }
}
