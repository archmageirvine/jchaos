package irvine.tile;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test class for all tests in this directory.
 *
 * @author Sean A. Irvine
 */
public class AllTests extends TestSuite {

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AttackEffectTest.class);
    suite.addTestSuite(DummyTileEffectTest.class);
    suite.addTestSuite(FadeEffectTest.class);
    suite.addTestSuite(HorizontalRollEffectTest.class);
    suite.addTestSuite(TileImageTest.class);
    suite.addTestSuite(ImageUtilsTest.class);
    suite.addTestSuite(ExplodingCircleEffectTest.class);
    suite.addTestSuite(ExplodingSquareEffectTest.class);
    suite.addTestSuite(ExplosionEffectTest.class);
    suite.addTestSuite(PixelFadeEffectTest.class);
    suite.addTestSuite(PortalOpenEffectTest.class);
    suite.addTestSuite(ReverseEffectTest.class);
    suite.addTestSuite(RotationEffectTest.class);
    suite.addTestSuite(TileSetTest.class);
    suite.addTestSuite(TwinkleEffectTest.class);
    suite.addTestSuite(TwirlEffectTest.class);
    suite.addTestSuite(VerticalRollEffectTest.class);
    return suite;
  }

  public static void main(final String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}

