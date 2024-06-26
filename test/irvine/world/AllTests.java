package irvine.world;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Links all the tests in this package.
 * @author Sean A. Irvine
 */
public class AllTests extends TestSuite {

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(CylindricalWorldTest.class);
    suite.addTestSuite(DummyWorldTest.class);
    suite.addTestSuite(FlatWorldTest.class);
    suite.addTestSuite(ToroidalWorldTest.class);
    return suite;
  }

  public static void main(final String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
