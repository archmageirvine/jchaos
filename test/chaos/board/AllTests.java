package chaos.board;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Links all the tests in this package.
 * @author Sean A. Irvine
 */
public class AllTests extends TestSuite {

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AttentionDetailTest.class);
    suite.addTestSuite(CastMasterTest.class);
    suite.addTestSuite(CellTest.class);
    suite.addTestSuite(CellEventTest.class);
    suite.addTestSuite(GrowerTest.class);
    suite.addTestSuite(LineOfSightTest.class);
    suite.addTestSuite(MoveMasterTest.class);
    suite.addTestSuite(ReachableTest.class);
    suite.addTestSuite(ShootingRulesTest.class);
    suite.addTestSuite(TeamTest.class);
    suite.addTestSuite(UpdaterTest.class);
    suite.addTestSuite(WarpSpaceTest.class);
    suite.addTestSuite(WizardManagerTest.class);
    suite.addTestSuite(WorldTest.class);
    suite.addTestSuite(WorldUtilsTest.class);
    return suite;
  }

  public static void main(final String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
