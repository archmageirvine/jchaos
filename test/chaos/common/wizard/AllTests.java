package chaos.common.wizard;

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
    suite.addTestSuite(WizardTest.class);
    suite.addTestSuite(Wizard1Test.class);
    suite.addTestSuite(Wizard2Test.class);
    suite.addTestSuite(Wizard3Test.class);
    suite.addTestSuite(Wizard4Test.class);
    suite.addTestSuite(Wizard5Test.class);
    suite.addTestSuite(Wizard6Test.class);
    suite.addTestSuite(Wizard7Test.class);
    suite.addTestSuite(Wizard8Test.class);
    suite.addTestSuite(Wizard9Test.class);
    suite.addTestSuite(Wizard10Test.class);
    suite.addTestSuite(Wizard11Test.class);
    suite.addTestSuite(Wizard12Test.class);
    suite.addTestSuite(Wizard13Test.class);
    suite.addTestSuite(Wizard14Test.class);
    suite.addTestSuite(Wizard15Test.class);
    return suite;
  }

  public static void main(final String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
