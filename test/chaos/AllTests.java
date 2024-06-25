package chaos;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Links all the tests in this package.
 * @author Sean A. Irvine
 */
public class AllTests extends TestSuite {

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(CastResourceTest.class);
    suite.addTestSuite(ChaosExperimentalTest.class);
    suite.addTestSuite(ConfigurationTest.class);
    suite.addTestSuite(GenericSetUpTest.class);
    suite.addTestSuite(MaskTest.class);
    suite.addTestSuite(RankingModeExperimentTest.class);
    suite.addTest(chaos.board.AllTests.suite());
    suite.addTest(chaos.common.AllTests.suite());
    suite.addTest(chaos.engine.AllTests.suite());
    suite.addTest(chaos.graphics.AllTests.suite());
    suite.addTest(chaos.scenario.AllTests.suite());
    suite.addTest(chaos.selector.AllTests.suite());
    suite.addTest(chaos.setup.AllTests.suite());
    suite.addTest(chaos.sound.AllTests.suite());
    suite.addTest(chaos.util.AllTests.suite());

    // This one must be last because it monkeys with game state in strange ways
    suite.addTestSuite(ChaosTest.class);

    return suite;
  }

  public static void main(final String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
