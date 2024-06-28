package chaos.scenario;

import java.io.IOException;
import java.util.ArrayList;

import chaos.Chaos;
import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Conveyance;
import chaos.common.Inanimate;
import chaos.common.Monster;
import chaos.common.State;
import chaos.common.inanimate.Exit;
import chaos.common.wizard.Wizard;
import chaos.graphics.ChaosScreen;

/**
 * Utility functions associated with scenarios.
 * @author Sean A. Irvine
 */
public final class ScenarioUtils {

  private ScenarioUtils() {
  }

  private static boolean copy(final World world, final int x, final int y) {
    final ArrayList<Actor> stack = new ArrayList<>();
    final Cell source = world.getCell(x, y);
    Actor a;
    do {
      a = source.pop();
      if (a != null) {
        stack.add(a);
      }
    } while (a != null);
    final Cell dest = world.getCell(x, y + 1);
    if (dest != null) {
      for (int k = stack.size() - 1; k >= 0; --k) {
        dest.push(stack.get(k));
      }
    } else if (!stack.isEmpty()) {
      // Things going off the bottom of the screen -- check for any non-human
      final Actor d = stack.get(0);
      if (d instanceof Monster && d.getState() == State.ACTIVE) {
        d.setState(State.DEAD); // essential to do this for wizards
        // Need to deep check for mounts
        if (d instanceof Conveyance) {
          final Actor m = ((Conveyance) d).getMount();
          if (m instanceof Wizard) {
            m.setState(State.DEAD);
            return true;
          }
        }
        return !(d instanceof Inanimate) && (d instanceof Wizard || d.getOwner() == Actor.OWNER_INDEPENDENT);
      }
    }
    return false;
  }

  private static boolean scrollDown(final World world) {
    boolean ok = false;
    for (int y = world.height() - 1; y >= 0; --y) {
      for (int x = 0; x < world.width(); ++x) {
        ok |= copy(world, x, y);
      }
    }
    return ok;
  }

  /**
   * If a scenario is set, then perform a single step of the scenario update.
   * @param world the world
   * @param scenario the scenario
   * @return true if update was successful
   */
  public static boolean update(final World world, final Scenario scenario) {
    if (scenario == null) {
      return true;
    }
    if (scenario.getHeight() > 0) {
      return true; //scenario.isValid();
    }
    final boolean scrollDown = scrollDown(world);
    if (scenario.isValid()) {
      scenario.update(world);
      return !scrollDown;
    }
    return false;
  }

  /**
   * This is the main workhorse for detecting the completion of a scenario. Normally
   * this will return false, indicating that the current play should continue.
   * Otherwise, a whole new instance of Chaos is needed.
   * @param chaos current universe
   * @param screen screen we are using
   * @return false if existing play should continue
   * @throws java.io.IOException if the next scenario does not exist.
   */
  public static boolean isScenarioChainDone(final Chaos chaos, final ChaosScreen screen) throws IOException {
    // Handle Exit
    boolean exit = false;
    String nextScenario = null;
    for (final Cell c : chaos.getWorld()) {
      if (c.peek() instanceof Exit && ((Exit) c.peek()).getMount() != null) {
        nextScenario = ((Exit) c.peek()).nextScenario();
        exit = true;
        break;
      }
    }
    if (!exit) {
      return false;
    }
    // Scenario is complete
    if (nextScenario == null) {
      return true; // We are done, but there is no next scenario
    }
    // Actually switch to the new scenario
    final Scenario scenario = Scenario.load(nextScenario);
    System.out.println("Switch to scenario: " + nextScenario);
    final int worldRows = scenario.getHeight();
    final int worldCols = scenario.getWidth();
    chaos.setWorld(new World(worldCols, worldRows));
    scenario.init(chaos, screen);
    chaos.prepareToPlay(screen);
    chaos.getUpdater().update(); // Ensures exit's that should be open are marked at such
    return false;
  }
}
