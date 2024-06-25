package chaos.util;

import chaos.board.Team;
import chaos.board.WizardManager;
import chaos.board.World;
import chaos.common.State;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class TeamUtilsTest extends TestCase {

  public void testTeamUtils() {
    final World w = new World(1, 9);
    final WizardManager wm = w.getWizardManager();
    final Team team = w.getTeamInformation();
    for (int k = 1; k < 6; ++k) {
      wm.getWizard(k).setState(State.ACTIVE);
      w.getCell(k).push(wm.getWizard(k));
      if (k != 5) {
        team.setTeam(k, 1);
      }
    }
    for (int k = 1; k < 5; ++k) {
      assertEquals(team.getTeam(1), team.getTeam(k));
    }
    TeamUtils.smashTeams(w);
    for (int k = 1; k < 5; ++k) {
      assertEquals(team.getTeam(1), team.getTeam(k));
    }
    wm.getWizard(5).setState(State.DEAD);
    TeamUtils.smashTeams(w);
    for (int k = 1; k < 6; ++k) {
      for (int j = 1; j < k; ++j) {
        assertTrue(team.getTeam(k) != team.getTeam(j));
      }
    }
  }

}
