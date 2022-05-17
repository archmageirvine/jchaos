package chaos.selector;

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
    suite.addTestSuite(CreaturologistTest.class);
    suite.addTestSuite(GenericScreenSelectorTest.class);
    suite.addTestSuite(OrdinarySelectorTest.class);
    suite.addTestSuite(RandomAiSelectorTest.class);
    suite.addTestSuite(RankerTest.class);
    suite.addTestSuite(SelectorFactoryTest.class);
    suite.addTestSuite(SelectorUtilsTest.class);
    suite.addTestSuite(StrategiserTest.class);
    return suite;
  }

  public static void main(final String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
