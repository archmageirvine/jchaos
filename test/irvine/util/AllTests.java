package irvine.util;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Links all the tests in this package.
 * @author Sean A. Irvine
 */
public class AllTests extends TestSuite {

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTest(irvine.util.array.AllTests.suite());
    suite.addTest(irvine.util.graphics.AllTests.suite());
    suite.addTest(irvine.util.io.AllTests.suite());
    suite.addTest(irvine.util.string.AllTests.suite());
    suite.addTest(irvine.util.time.AllTests.suite());

    suite.addTestSuite(CliFlagsTest.class);
    suite.addTestSuite(CollectionUtilsTest.class);
    suite.addTestSuite(DummyIteratorTest.class);
    suite.addTestSuite(KeyboardTest.class);
    suite.addTestSuite(LimitedLengthPriorityQueueTest.class);
    suite.addTestSuite(MaximumSeparationTest.class);
    suite.addTestSuite(PairTest.class);
    return suite;
  }

  public static void main(final String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
