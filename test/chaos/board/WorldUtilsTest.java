package chaos.board;

import chaos.common.Actor;
import chaos.common.State;
import chaos.common.inanimate.Generator;
import chaos.common.wizard.Wizard;
import chaos.common.wizard.Wizard1;
import junit.framework.TestCase;

/**
 * Tests for the corresponding class.
 * @author Sean A. Irvine
 */
public class WorldUtilsTest extends TestCase {

  public void testInsertGenerators() {
    final World w = new World(2, 2);
    WorldUtils.insertGenerators(w, -1);
    for (int k = 0; k < w.size(); ++k) {
      assertNull(w.actor(k));
    }
    WorldUtils.insertGenerators(w, 4);
    for (int k = 0; k < w.size(); ++k) {
      assertTrue(w.actor(k) instanceof Generator);
      assertEquals(Actor.OWNER_INDEPENDENT, w.actor(k).getOwner());
    }
  }

  public void testInsertGeneratorsTooMany() {
    final World w = new World(2, 2);
    WorldUtils.insertGenerators(w, 5);
    for (int k = 0; k < w.size(); ++k) {
      assertTrue(w.actor(k) instanceof Generator);
      assertEquals(Actor.OWNER_INDEPENDENT, w.actor(k).getOwner());
    }
  }

  public void testInsertWizards() {
    final World w = new World(2, 2);
    WorldUtils.insertWizards(w, new Wizard[0], false);
    for (int k = 0; k < w.size(); ++k) {
      assertNull(w.actor(k));
    }
    WorldUtils.insertWizards(w, new Wizard[0], true);
    for (int k = 0; k < w.size(); ++k) {
      assertNull(w.actor(k));
    }
    final Wizard[] wiz = {
      new Wizard1(),
      null,
      new Wizard1(),
      new Wizard1(),
      new Wizard1(),
      new Wizard1(),
    };
    wiz[0].setState(State.ACTIVE);
    wiz[2].setState(State.ACTIVE);
    wiz[4].setState(State.ACTIVE);
    wiz[5].setState(State.ACTIVE);
    WorldUtils.insertWizards(w, wiz, false);
    for (int k = 0; k < w.size(); ++k) {
      assertTrue(w.actor(k) instanceof Wizard);
      assertEquals(State.ACTIVE, w.actor(k).getState());
    }
    final Wizard[] wiz2 = {
      new Wizard1(),
      new Wizard1(),
    };
    wiz2[0].setState(State.ACTIVE);
    wiz2[1].setState(State.ACTIVE);
    WorldUtils.insertWizards(w, wiz2, true);
    assertTrue(w.actor(2) instanceof Wizard);
    assertTrue(w.actor(3) instanceof Wizard);
  }

}
