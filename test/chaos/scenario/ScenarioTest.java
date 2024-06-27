package chaos.scenario;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;

import chaos.Chaos;
import chaos.Configuration;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.State;
import chaos.common.inanimate.DarkWood;
import chaos.common.inanimate.Exit;
import chaos.common.inanimate.MagicWood;
import chaos.common.inanimate.Pool;
import chaos.common.inanimate.Rock;
import chaos.common.inanimate.ShadowWood;
import chaos.common.inanimate.StandardWall;
import chaos.common.monster.GiantBeetle;
import chaos.common.monster.KingCobra;
import chaos.common.monster.WoodElf;
import chaos.common.wizard.Wizard;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class ScenarioTest extends TestCase {

  private Scenario transmute(final Scenario scenario) throws Exception {
    final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      try (final ObjectOutputStream oos = new ObjectOutputStream(bos)) {
        oos.writeObject(scenario);
      }
    } finally {
      bos.close();
    }
    try (final ObjectInputStream oos = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()))) {
      return (Scenario) oos.readObject();
    }
  }

  private void checkScenario(final Scenario scenario, final World w) throws Exception {
    assertTrue(scenario.isValid());
    scenario.update(w);
    assertTrue(w.actor(0) instanceof StandardWall);
    assertTrue(w.actor(1) instanceof StandardWall);
    assertTrue(w.actor(2) instanceof StandardWall);
    assertTrue(w.actor(3) instanceof StandardWall);
    assertEquals(Actor.OWNER_INDEPENDENT, w.actor(3).getOwner());
    assertTrue(w.actor(4) instanceof GiantBeetle);
    assertTrue(w.actor(5) instanceof GiantBeetle);
    assertTrue(w.actor(6) instanceof Pool);
    assertTrue(w.actor(7) instanceof KingCobra);
    assertNull(w.actor(8));
    assertNull(w.actor(9));
    assertNull(w.actor(10));
    assertNull(w.actor(11));
    assertTrue(w.actor(12) instanceof DarkWood);
    assertTrue(w.actor(13) instanceof ShadowWood);
    assertTrue(w.actor(14) instanceof WoodElf);
    assertTrue(w.actor(15) instanceof MagicWood);
    assertTrue(w.actor(31) instanceof Rock);
  }

  public void testScenario() throws Exception {
    final Scenario scenario = Scenario.load("chaos/scenario/default.dat");
    assertTrue(scenario.isValid());
    final World w = new World(32, 2);
    for (int k = 0; k < 9; ++k) {
      scenario.update(w);
    }
    checkScenario(scenario, w);
  }

  public void testScenarioSeralized() throws Exception {
    final Scenario scenario = Scenario.load("chaos/scenario/default.dat");
    assertTrue(scenario.isValid());
    final World w = new World(32, 2);
    for (int k = 0; k < 9; ++k) {
      scenario.update(w);
    }
    final Scenario s2 = transmute(scenario);
    checkScenario(s2, w);
  }

  public void testScenario2() throws Exception {
    final PrintStream oldErr = System.err;
    try {
      final ByteArrayOutputStream output = new ByteArrayOutputStream();
      try {
        try (final PrintStream ps = new PrintStream(output)) {
          System.setErr(ps);
          final Scenario scenario = Scenario.load("chaos/scenario/test.dat");
          assertTrue(scenario.isValid());
          final World w = new World(scenario.getWidth(), 2);
          scenario.update(w);
          assertNull(w.actor(0));
          assertTrue(w.actor(1) instanceof WoodElf);
          assertTrue(w.actor(2) instanceof Rock);
          assertEquals(1, w.actor(1).getOwner());
          assertEquals(State.ACTIVE, w.actor(1).getState());
          assertFalse(scenario.isValid());
        }
      } finally {
        output.close();
      }
      assertEquals("Skipping unknown: NoSuchCreature", output.toString().trim());
    } finally {
      System.setErr(oldErr);
    }
  }

  public void testBeetleMania() throws Exception {
    final Scenario scenario = Scenario.load("chaos/resources/scenario/raven/beetle_mania.scn");
    assertTrue(scenario.isValid());
    final Configuration config = new Configuration(scenario.getHeight(), scenario.getWidth());
    final Chaos chaos = new Chaos(config, false);
    scenario.init(chaos, null);
    final World world = chaos.getWorld();
    assertTrue(world.getCell(0, 12).peek() instanceof Wizard);
    assertTrue(world.getCell(31, 12).peek() instanceof Exit);
    scenario.update(world);
    assertTrue(world.getCell(0, 12).peek() instanceof Wizard);
    assertTrue(world.getCell(31, 12).peek() instanceof Exit);
  }

}
