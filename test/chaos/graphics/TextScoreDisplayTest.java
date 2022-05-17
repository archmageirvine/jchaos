package chaos.graphics;

import java.io.IOException;

import chaos.board.World;
import chaos.common.State;
import chaos.common.wizard.Wizard;
import chaos.common.wizard.Wizard1;
import irvine.StandardIoTestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class TextScoreDisplayTest extends StandardIoTestCase {

  private static final String LS = System.lineSeparator();
  private static final String EXPECTED = ""
    + "Turn number: 42" + LS
    + "id         name   state  score  life" + LS
    + "------------------------------------" + LS
    + " 2        test2  ACTIVE      0    19" + LS
    + " 1        test0    DEAD      0     0" + LS
    + "-1  Independent  ACTIVE      0     0" + LS;

  public void test() throws IOException {
    final Wizard[] w = new Wizard[3];
    w[0] = new Wizard1();
    w[0].setPersonalName("test0");
    w[2] = new Wizard1();
    w[2].setPersonalName("test2");
    w[2].setState(State.ACTIVE);
    final World wld = new World(3, 3);
    wld.getCell(0).push(w[2]);
    wld.getWizardManager().setWizard(1, w[0]);
    wld.getWizardManager().setWizard(2, w[2]);
    final Wizard1 indp = new Wizard1();
    indp.setPersonalName("Independent");
    indp.setState(State.ACTIVE);
    final TextScoreDisplay d = new TextScoreDisplay(wld, w, indp);
    d.showScores(42);
    assertEquals(EXPECTED, getOut());
  }
}
