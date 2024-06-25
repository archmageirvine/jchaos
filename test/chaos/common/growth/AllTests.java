package chaos.common.growth;

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
    suite.addTestSuite(BalefireTest.class);
    suite.addTestSuite(DarkMatterTest.class);
    suite.addTestSuite(EarthquakeTest.class);
    suite.addTestSuite(FireTest.class);
    suite.addTestSuite(FloodTest.class);
    suite.addTestSuite(GooeyBlobTest.class);
    suite.addTestSuite(GreenOozeTest.class);
    suite.addTestSuite(MagmaTest.class);
    suite.addTestSuite(OrangeJellyTest.class);
    suite.addTestSuite(SpikyTest.class);
    suite.addTestSuite(VioletFungiTest.class);
    suite.addTestSuite(WheatTest.class);
    return suite;
  }

  public static void main(final String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
