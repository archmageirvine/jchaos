package chaos.engine;

import chaos.board.CastMaster;
import chaos.board.MoveMaster;
import chaos.board.World;
import junit.framework.TestCase;

/**
 * JUnit tests for the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class EngineFactoryTest extends TestCase {


  public void test() {
    final World w = new World(1, 1);
    final CastMaster c = new CastMaster(w);
    final MoveMaster m = new MoveMaster(w);
    assertNotNull(EngineFactory.createEngine("chaos.engine.AiEngine", w, m, c, 3));
    assertNotNull(EngineFactory.createEngine("chaos.engine.HumanEngine", w, m, c, 3));
    try {
      EngineFactory.createEngine("no-such-engine", w, m, c, 3);
    } catch (final RuntimeException e) {
      // ok
    }
  }
}
