package chaos.selector;

import chaos.common.Castable;
import chaos.common.State;
import chaos.common.free.Hide;
import chaos.common.free.Points;
import chaos.common.monster.Iridium;
import chaos.common.wizard.Wizard1;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class SelectorUtilsTest extends TestCase {

  public void testWorst() {
    final Hide h = new Hide();
    assertNull(SelectorUtils.worst(new Castable[0], h));
    assertNull(SelectorUtils.worst(new Castable[5], h));
    final Iridium i = new Iridium();
    final Points p = new Points();
    assertEquals(h, SelectorUtils.worst(new Castable[] {p, h, i}, p));
    assertEquals(h, SelectorUtils.worst(new Castable[] {p, i, h}, p));
    assertEquals(h, SelectorUtils.worst(new Castable[] {h, i, p}, p));
    assertEquals(h, SelectorUtils.worst(new Castable[] {i, p, h}, p));
  }

  public void testBest() {
    Castable[] c = new Castable[0];
    assertEquals(0, SelectorUtils.best(c, 0).length);
    c = new Castable[] {new Points()};
    assertEquals(0, SelectorUtils.best(c, 0).length);
    assertEquals(1, SelectorUtils.best(c, 1).length);
    c = new Castable[] {
      new Iridium(),
      new Hide()
    };
    final Castable[] d = SelectorUtils.best(c, 1);
    assertEquals(1, d.length);
    assertEquals(c[0], d[0]);
    assertEquals(2, SelectorUtils.best(c, 2).length);
  }

  public void testNoSelection() {
    final Wizard1 wiz = new Wizard1();
    assertTrue(SelectorUtils.noSelectionPossible(null, wiz));
    assertTrue(SelectorUtils.noSelectionPossible(new Castable[0], wiz));
    final Castable[] c = {new Hide()};
    assertTrue(SelectorUtils.noSelectionPossible(c, null));
    assertTrue(SelectorUtils.noSelectionPossible(c, wiz));
    wiz.setState(State.ACTIVE);
    assertFalse(SelectorUtils.noSelectionPossible(c, wiz));
  }
}
