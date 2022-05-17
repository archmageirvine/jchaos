package chaos.util;

import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Conveyance;
import chaos.common.State;
import chaos.common.wizard.Wizard;

import java.util.HashSet;

/**
 * Team utility functions.
 *
 * @author Sean A. Irvine
 */
public final class TeamUtils {

  private TeamUtils() { }

  private static final boolean SMASH_TEAMS = ChaosProperties.properties().getBooleanProperty("chaos.team.smash", true);

  /**
   * If all remaining players are on the same team, then smash the team so that
   * they can fight each other.
   * @param world game world
   */
  public static void smashTeams(final World world) {
    if (SMASH_TEAMS) {
      final Team team = world.getTeamInformation();
      final HashSet<Integer> seen = new HashSet<>();
      for (final Cell c : world) {
        final Actor a = c.peek();
        if (a != null && a.getState() == State.ACTIVE) {
          seen.add(team.getTeam(a));
          if (a instanceof Conveyance) {
            final Actor mount = ((Conveyance) a).getMount();
            if (mount != null) {
              seen.add(team.getTeam(mount));
            }
          }
        }
      }
      if (seen.size() <= 1) {
        // Everything visisble and active on the screen is on the same team.
        // Therefore smash the current teams.
        world.notify(new TextEvent("Teams are smashed!"));
        world.notify(new AudioEvent("team_killer"));
        for (final Wizard w : world.getWizardManager().getWizards()) {
          if (w != null && w.getState() == State.ACTIVE) {
            team.separate(w.getOwner());
          }
        }
      }
    }
  }
}
