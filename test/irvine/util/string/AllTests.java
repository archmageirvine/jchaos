package irvine.util.string;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Links all the tests in this package.
 * @author Sean A. Irvine
 */
public class AllTests extends TestSuite {

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(CasingTest.class);
    suite.addTestSuite(DateTest.class);
    suite.addTestSuite(PostScriptTest.class);
    suite.addTestSuite(StringUtilsTest.class);
    suite.addTestSuite(TextTableTest.class);
    suite.addTestSuite(WrappingStringBufferTest.class);
    return suite;
  }

  public static void main(final String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
