package chaos;

import java.io.IOException;
import java.util.List;

import chaos.board.Cell;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.dragon.BlackDragon;
import chaos.common.monster.Megabeetle;
import chaos.common.monster.Zombie;
import chaos.common.wizard.Wizard;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class RankingModeExperimentTest extends TestCase {

  public void testColor() {
    assertEquals("000000", RankingModeExperiment.getColor(0));
    assertEquals("00ff00", RankingModeExperiment.getColor(200));
    assertEquals("ff0000", RankingModeExperiment.getColor(-200));
    assertEquals("007f00", RankingModeExperiment.getColor(100));
  }

  public void testName() {
    assertEquals("Black&nbsp;Dragon", RankingModeExperiment.htmlName(BlackDragon.class));
  }

  public void testRadioactiveConfig() {
    final Chaos chaos = RankingModeExperiment.radioactiveConfiguration(new BlackDragon(), new Zombie());
    final StringBuilder sb = new StringBuilder();
    for (final Cell cell : chaos.getWorld()) {
      sb.append(cell.peek().getName());
    }
    assertEquals("Radioactive LandRadioactive LandRadioactive LandRadioactive LandRadioactive LandRadioactive LandBlack DragonRadioactive LandZombieRadioactive LandRadioactive LandRadioactive LandRadioactive LandRadioactive LandRadioactive Land", sb.toString());
  }

  public void testAdjacentConfig() {
    final Chaos chaos = RankingModeExperiment.adjacentConfiguration(new BlackDragon(), new Zombie());
    final StringBuilder sb = new StringBuilder();
    for (final Cell cell : chaos.getWorld()) {
      if (cell.peek() != null) {
        sb.append(cell.peek().getName());
      } else {
        sb.append('.');
      }
    }
    assertEquals(".....Black DragonZombie.....", sb.toString());
  }

  public void testGetCastable() throws IOException {
    final List<Class<? extends Castable>> castables = RankingModeExperiment.getCastables();
    int wizCount = 0;
    boolean sawZeroFreq = false;
    for (Class<? extends Castable> castable : castables) {
      assertTrue(Actor.class.isAssignableFrom(castable));
      if (Wizard.class.isAssignableFrom(castable)) {
        ++wizCount;
      } else if (castable == Megabeetle.class) {
        sawZeroFreq = true;
      }
    }
    assertEquals(1, wizCount);
    assertTrue(sawZeroFreq);
  }
}
