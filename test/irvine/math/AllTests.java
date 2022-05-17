package irvine.math;

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
    suite.addTest(irvine.math.r.AllTests.suite());
    suite.addTestSuite(IntegerUtilsTest.class);
    suite.addTestSuite(ShuffleTest.class);
    return suite;
  }

  public static void main(final String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
