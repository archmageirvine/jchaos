package chaos.board;

import java.io.Serializable;
import java.util.ArrayList;

import chaos.common.Actor;
import chaos.util.Random;

/**
 * Maintains team information.
 *
 * @author Sean A. Irvine
 */
public class Team implements Serializable {

  /**
   * This constant is added to indexes to the team map, it allows for the
   * negative values used by the independents.
   */
  private static final int DELTA = 2;

  /** Stores the mapping for player to team. */
  private final int[] mTeam;
  private final ArrayList<Integer> mHeraldicKey = new ArrayList<>();
  private int mNextTeamNumber;

  /**
   * Initialize a new team object.
   */
  public Team() {
    // initialize to the identity map
    mTeam = new int[WizardManager.WIZ_ARRAY_SIZE + DELTA];
    for (int i = 0; i < mTeam.length; ++i) {
      mTeam[i] = i;
      // Chances of two teams having same key is vanishingly small
      mHeraldicKey.add(Random.nextInt(Integer.MAX_VALUE));
    }
    mNextTeamNumber = mTeam.length - 1;
  }


  /**
   * Get the team number to which the specified player belongs.
   *
   * @param player player to get team number for
   * @return team player belongs to
   */
  public int getTeam(final int player) {
    return mTeam[player + DELTA];
  }

  /**
   * Get the team number to which the specified actor belongs.
   *
   * @param actor actor to get team for
   * @return team player belongs to
   */
  public int getTeam(final Actor actor) {
    return getTeam(actor.getOwner());
  }

  /**
   * Set <code>player1</code> to be on the same team as <code>player2</code>.
   * Other players allied to <code>player2</code> will remain allied to
   * <code>player2</code> and will also become allies of <code>player1</code>.
   * Former allies of <code>player1</code> are lost, unless they also happen
   * to already be allies of <code>player2</code>.
   *
   * @param player1 player to set team aspect of
   * @param player2 player to set team aspect to
   */
  public void setTeam(final int player1, final int player2) {
    mTeam[player1 + DELTA] = mTeam[player2 + DELTA];
  }

  /**
   * Method that should only be used for initial team assignments.
   *
   * @param player player to assign
   * @param team team number for player
   */
  public void explicitSetTeam(final int player, final int team) {
    mTeam[player + DELTA] = team;
  }

  /**
   * Ensure the given player is not allied with any other player.
   *
   * @param player player to separate
   */
  public void separate(final int player) {
    mTeam[player + DELTA] = ++mNextTeamNumber;
  }

  /**
   * Get a random value representing this team.
   *
   * @param team team to get team shield for
   * @return shield code
   */
  public int heraldicKey(final int team) {
    while (team >= mHeraldicKey.size()) {
      mHeraldicKey.add(Random.nextInt(Integer.MAX_VALUE));
    }
    return mHeraldicKey.get(team);
  }
}
