package chaos.common.inanimate;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Links all the tests in this package.
 * @author Sean A. Irvine
 */
public class AllTests extends TestSuite {

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AppleWoodTest.class);
    suite.addTestSuite(ArmouryTest.class);
    suite.addTestSuite(ArsenalTest.class);
    suite.addTestSuite(AviaryTest.class);
    suite.addTestSuite(BipedalGeneratorTest.class);
    suite.addTestSuite(ConiferTest.class);
    suite.addTestSuite(CrackedEggTest.class);
    suite.addTestSuite(DarkCitadelTest.class);
    suite.addTestSuite(DarkWoodTest.class);
    suite.addTestSuite(DragonNestTest.class);
    suite.addTestSuite(ElmTest.class);
    suite.addTestSuite(ExitTest.class);
    suite.addTestSuite(FenceHorizontalTest.class);
    suite.addTestSuite(FenceVerticalTest.class);
    suite.addTestSuite(FirTest.class);
    suite.addTestSuite(ForceFenceTest.class);
    suite.addTestSuite(GeneratorTest.class);
    suite.addTestSuite(HeptagramTest.class);
    suite.addTestSuite(HexTest.class);
    suite.addTestSuite(MagicCastleTest.class);
    suite.addTestSuite(MagicGlassTest.class);
    suite.addTestSuite(MagicWoodTest.class);
    suite.addTestSuite(ManaBatteryTest.class);
    suite.addTestSuite(MarbleColumnTest.class);
    suite.addTestSuite(NukedTest.class);
    suite.addTestSuite(PentagramTest.class);
    suite.addTestSuite(PitTest.class);
    suite.addTestSuite(PoolTest.class);
    suite.addTestSuite(PowerWallTest.class);
    suite.addTestSuite(PowerWallHorizontalTest.class);
    suite.addTestSuite(PowerWallVerticalTest.class);
    suite.addTestSuite(RockTest.class);
    suite.addTestSuite(RoperTest.class);
    suite.addTestSuite(ShadowCityTest.class);
    suite.addTestSuite(ShadowWoodTest.class);
    suite.addTestSuite(SpawnerTest.class);
    suite.addTestSuite(SpringTest.class);
    suite.addTestSuite(StandardWallTest.class);
    suite.addTestSuite(StrongWallTest.class);
    suite.addTestSuite(TempestTest.class);
    suite.addTestSuite(TombstoneTest.class);
    suite.addTestSuite(ToxicWasteTest.class);
    suite.addTestSuite(VolcanoTest.class);
    suite.addTestSuite(VortexTest.class);
    suite.addTestSuite(WaspNestTest.class);
    suite.addTestSuite(WeakWallTest.class);
    suite.addTestSuite(WebTest.class);
    suite.addTestSuite(WillyTest.class);
    return suite;
  }

  public static void main(final String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
