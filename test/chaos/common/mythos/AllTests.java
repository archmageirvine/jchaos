package chaos.common.mythos;

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
    suite.addTestSuite(Alien8Test.class);
    suite.addTestSuite(CylinderTest.class);
    suite.addTestSuite(DarthVaderTest.class);
    suite.addTestSuite(DiamondSwordTest.class);
    suite.addTestSuite(FlerkenTest.class);
    suite.addTestSuite(HoraceTest.class);
    suite.addTestSuite(MrStrongTest.class);
    suite.addTestSuite(OrangeTest.class);
    suite.addTestSuite(PliersTest.class);
    suite.addTestSuite(QuazatronTest.class);
    suite.addTestSuite(SkulkrinTest.class);
    suite.addTestSuite(SupermanTest.class);
    suite.addTestSuite(SwissKnifeTest.class);
    return suite;
  }
  public static void main(final String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
