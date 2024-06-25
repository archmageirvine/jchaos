package irvine.heraldry;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Links all the tests in this package.
 * @author Sean A. Irvine
 */
public class AllTests extends TestSuite {

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(DisplayShieldTest.class);
    suite.addTestSuite(LozengeTest.class);
    suite.addTestSuite(OrdinaryTest.class);
    suite.addTestSuite(PostScriptHelperTest.class);
    suite.addTestSuite(ShapeTest.class);
    suite.addTestSuite(ShieldTest.class);
    suite.addTestSuite(TinctureTest.class);
    return suite;
  }

  public static void main(final String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
