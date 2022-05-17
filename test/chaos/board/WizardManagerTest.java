package chaos.board;

import chaos.common.Actor;
import chaos.common.State;
import chaos.common.wizard.Wizard;
import chaos.common.wizard.Wizard1;
import junit.framework.TestCase;

/**
 * Test the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class WizardManagerTest extends TestCase {

  public void test() {
    final WizardManager wm = new WizardManager();
    final Wizard i = wm.getIndependent();
    assertNotNull(i);
    assertEquals(i, i);
    assertEquals(i, wm.getWizard(Actor.OWNER_INDEPENDENT));
    assertEquals("Independent", i.getPersonalName());
    assertEquals(Actor.OWNER_INDEPENDENT, i.getOwner());
    assertEquals(State.ACTIVE, i.getState());
    final Wizard[] w = wm.getWizards();
    assertEquals(WizardManager.WIZ_ARRAY_SIZE, w.length);
    for (int k = 1; k < wm.getMaximumPlayerNumber(); ++k) {
      assertEquals(w[k], wm.getWizard(k));
    }
    assertNull(wm.getWizard(WizardManager.WIZ_ARRAY_SIZE + 1));
    assertEquals(w.length, wm.getMaximumPlayerNumber());
    for (int k = 1; k < wm.getMaximumPlayerNumber(); ++k) {
      final Wizard1 o = new Wizard1();
      o.setOwner(42);
      wm.setWizard(k, o);
      assertEquals(k, o.getOwner());
      assertEquals(o, wm.getWizard(k));
      assertEquals(o, wm.getWizard(o));
      wm.setWizard(k, null);
      assertNull(wm.getWizard(k));
    }
    assertNull(wm.getWizard(null));
  }

  public void testShuffle() {
    final WizardManager wm = new WizardManager();
    final Wizard w1 = wm.getWizard(1);
    for (int k = 0; k < 10; ++k) {
      wm.shuffle(new Team());
      if (wm.getWizard(1) != w1) {
        return;
      }
    }
    fail();
  }

  public void testActive() {
    final WizardManager wm = new WizardManager();
    assertEquals(0, wm.getActiveCount());
    wm.getWizard(2).setState(State.ACTIVE);
    assertEquals(1, wm.getActiveCount());
  }
}
