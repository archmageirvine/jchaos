package chaos.common.dragon;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Links all the tests in this package.
 *
 * @author Sean A. Irvine
 */
public class AllTests extends TestSuite {

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(BlackDragonTest.class);
    suite.addTestSuite(BlueDragonTest.class);
    suite.addTestSuite(EmeraldDragonTest.class);
    suite.addTestSuite(GoldenDragonTest.class);
    suite.addTestSuite(GreenDragonTest.class);
    suite.addTestSuite(PlatinumDragonTest.class);
    suite.addTestSuite(RedDragonTest.class);
    suite.addTestSuite(ShadowDragonTest.class);
    suite.addTestSuite(WhiteDragonTest.class);
    return suite;
  }
  public static void main(final String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
