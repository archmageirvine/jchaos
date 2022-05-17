package chaos.scenario;

import chaos.board.World;
import chaos.common.Actor;
import chaos.common.State;
import chaos.common.inanimate.MagicWood;
import chaos.common.inanimate.Rock;
import chaos.common.inanimate.StandardWall;
import chaos.common.monster.Horse;
import chaos.common.monster.Python;
import chaos.common.monster.WoodElf;
import chaos.common.wizard.Wizard1;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class ScenarioUtilsTest extends TestCase {

  private static final boolean[] EXPECTED = {
    true, false, false, false, false, true, false, true, true, false,
  };

  public void testScenario() throws Exception {
    final Scenario scenario = Scenario.load("chaos/scenario/default.dat");
    assertTrue(scenario.isValid());
    final World w = new World(32, 2);
    final WoodElf elf = new WoodElf();
    w.getCell(0).push(elf);
    final Horse h = new Horse();
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    h.setMount(wiz);
    w.getCell(1).push(h);
    for (int k = 0; k < EXPECTED.length; ++k) {
      //System.out.println(ScenarioUtils.update(w, scenario));
      assertEquals(String.valueOf(k), EXPECTED[k], ScenarioUtils.update(w, scenario));
    }
    assertEquals(State.DEAD, elf.getState());
    assertEquals(State.DEAD, h.getState());
    assertEquals(State.DEAD, wiz.getState());
    assertTrue(scenario.isValid());
    assertTrue(w.actor(0) instanceof StandardWall);
    assertTrue(w.actor(1) instanceof StandardWall);
    assertTrue(w.actor(2) instanceof StandardWall);
    assertTrue(w.actor(3) instanceof StandardWall);
    assertEquals(Actor.OWNER_INDEPENDENT, w.actor(3).getOwner());
    assertNull(w.actor(4));
    assertNull(w.actor(5));
    assertNull(w.actor(6));
    assertNull(w.actor(7));
    assertNull(w.actor(8));
    assertNull(w.actor(9));
    assertNull(w.actor(10));
    assertNull(w.actor(11));
    assertNull(w.actor(12));
    assertNull(w.actor(13));
    assertTrue(w.actor(14) instanceof WoodElf);
    assertTrue(w.actor(15) instanceof MagicWood);
    assertTrue(w.actor(31) instanceof Rock);
    assertTrue(w.actor(w.size() - 3) instanceof Python);
  }
}
