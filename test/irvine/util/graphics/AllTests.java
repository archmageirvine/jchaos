package irvine.util.graphics;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Links all the tests in this package.
 * @author Sean A. Irvine
 */
public class AllTests extends TestSuite {

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(BufferedImageUtilsTest.class);
    suite.addTestSuite(ButtonTest.class);
    suite.addTestSuite(EstimatePbmSkewTest.class);
    suite.addTestSuite(FullScreenTest.class);
    suite.addTestSuite(LightningTest.class);
    suite.addTestSuite(ParticleExplosionTest.class);
    suite.addTestSuite(PlasmaTest.class);
    suite.addTestSuite(PointerUtilsTest.class);
    suite.addTestSuite(StarsTest.class);
    suite.addTestSuite(StippleTest.class);
    suite.addTestSuite(ThunderboltTest.class);
    suite.addTestSuite(WallsTest.class);
    return suite;
  }

  public static void main(final String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
