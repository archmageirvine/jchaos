package chaos.common;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.WizardManager;
import chaos.board.World;
import chaos.common.wizard.Wizard;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class GrowthHelperTest extends TestCase {

  public void testFilter() {
    final World w = new World(5, 3);
    final WizardManager wm = w.getWizardManager();
    final Wizard wiz1 = wm.getWizard(1);
    wiz1.setState(State.ACTIVE);
    final Wizard wiz2 = wm.getWizard(2);
    wiz2.setState(State.ACTIVE);
    w.getCell(0).push(wiz1);
    w.getCell(8).push(wiz2);
    final HashSet<Cell> t = new HashSet<>();
    t.add(w.getCell(3));
    t.add(w.getCell(9));
    t.add(w.getCell(11));
    t.add(w.getCell(12));
    GrowthHelper.filter(t, wiz1, w);
    assertEquals(2, t.size());
    assertTrue(t.contains(w.getCell(3)));
    assertTrue(t.contains(w.getCell(9)));
  }

  public void testFilterNoSensibleTargets() {
    final World w = new World(5, 3);
    final WizardManager wm = w.getWizardManager();
    final Wizard wiz1 = wm.getWizard(1);
    wiz1.setState(State.ACTIVE);
    w.getCell(0).push(wiz1);
    final HashSet<Cell> t = new HashSet<>();
    t.add(w.getCell(3));
    t.add(w.getCell(9));
    t.add(w.getCell(11));
    t.add(w.getCell(12));
    GrowthHelper.filter(t, wiz1, w);
    assertEquals(4, t.size());
  }
}
