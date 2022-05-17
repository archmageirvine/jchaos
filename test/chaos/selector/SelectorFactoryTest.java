package chaos.selector;

import java.util.HashSet;

import junit.framework.TestCase;
import chaos.board.CastMaster;
import chaos.board.World;
import chaos.common.wizard.Wizard;

/**
 * JUnit tests for the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class SelectorFactoryTest extends TestCase {

  public void testCreate() {
    final World w = new World(1, 1);
    final CastMaster c = new CastMaster(w);
    final Wizard wiz = w.getWizardManager().getWizard(1);
    assertNotNull(SelectorFactory.createSelector("chaos.selector.Strategiser", wiz, w, c));
    assertNotNull(SelectorFactory.createSelector("chaos.selector.OrdinarySelector", wiz, w, c));
    assertNotNull(SelectorFactory.createSelector("chaos.selector.RandomAiSelector", wiz, w, c));
    try {
      SelectorFactory.createSelector("no-such-engine", wiz, w, c);
    } catch (final RuntimeException e) {
      // ok
    }
  }

  public void testRandom() {
    final World w = new World(1, 1);
    final CastMaster c = new CastMaster(w);
    final Wizard wiz = w.getWizardManager().getWizard(1);
    final HashSet<Class<? extends Selector>> seen = new HashSet<>();
    for (int k = 0; k < 1000; ++k) {
      seen.add(SelectorFactory.randomSelector(wiz, w, c).getClass());
    }
    assertEquals(7, seen.size());
    assertTrue(seen.contains(Ranker.class));
  }
}
