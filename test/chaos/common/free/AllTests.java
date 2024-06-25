package chaos.common.free;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Links all the tests in this package.
 * @author Sean A. Irvine
 */
public class AllTests extends TestSuite {

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AbductionTest.class);
    suite.addTestSuite(AcidRainTest.class);
    suite.addTestSuite(AcquisitionTest.class);
    suite.addTestSuite(AmnesiaTest.class);
    suite.addTestSuite(AmuletTest.class);
    suite.addTestSuite(AngerTest.class);
    suite.addTestSuite(ArboristTest.class);
    suite.addTestSuite(AssuranceTest.class);
    suite.addTestSuite(BattleCryTest.class);
    suite.addTestSuite(BiohazardTest.class);
    suite.addTestSuite(BlessTest.class);
    suite.addTestSuite(BoilTest.class);
    suite.addTestSuite(ChaosLordTest.class);
    suite.addTestSuite(CharmTest.class);
    suite.addTestSuite(CoercionTest.class);
    suite.addTestSuite(CommandTest.class);
    suite.addTestSuite(ConfidenceTest.class);
    suite.addTestSuite(CountermandTest.class);
    suite.addTestSuite(CrystalBallTest.class);
    suite.addTestSuite(CurseTest.class);
    suite.addTestSuite(DeadRevengeTest.class);
    suite.addTestSuite(DeathBringerTest.class);
    suite.addTestSuite(DepthTest.class);
    suite.addTestSuite(DestroyWallTest.class);
    suite.addTestSuite(DevastationTest.class);
    suite.addTestSuite(DisectionTest.class);
    suite.addTestSuite(DisruptTest.class);
    suite.addTestSuite(DoubleTest.class);
    suite.addTestSuite(DrainTest.class);
    suite.addTestSuite(ElfBootsTest.class);
    suite.addTestSuite(ElixirTest.class);
    suite.addTestSuite(EnfeeblementTest.class);
    suite.addTestSuite(FerengiTest.class);
    suite.addTestSuite(ForceSlamTest.class);
    suite.addTestSuite(FortitudeTest.class);
    suite.addTestSuite(FreeAllTest.class);
    suite.addTestSuite(FreePowerUpTest.class);
    suite.addTestSuite(HasteTest.class);
    suite.addTestSuite(HelmetTest.class);
    suite.addTestSuite(HideTest.class);
    suite.addTestSuite(HordeTest.class);
    suite.addTestSuite(HypercloneTest.class);
    suite.addTestSuite(ImpairTest.class);
    suite.addTestSuite(InsecurityTest.class);
    suite.addTestSuite(IrvinesInvulnerabilityTest.class);
    suite.addTestSuite(JokerTest.class);
    suite.addTestSuite(LevelTest.class);
    suite.addTestSuite(LichLordTest.class);
    suite.addTestSuite(LifeGiverTest.class);
    suite.addTestSuite(MagicBowTest.class);
    suite.addTestSuite(MagicKnifeTest.class);
    suite.addTestSuite(MagicShieldTest.class);
    suite.addTestSuite(MagicWandTest.class);
    suite.addTestSuite(MassResurrectTest.class);
    suite.addTestSuite(MaterializeTest.class);
    suite.addTestSuite(MeddleTest.class);
    suite.addTestSuite(MeteorStormTest.class);
    suite.addTestSuite(MoveItTest.class);
    suite.addTestSuite(MutateTest.class);
    suite.addTestSuite(NecropotenceTest.class);
    suite.addTestSuite(NoMountTest.class);
    suite.addTestSuite(ParadigmShiftTest.class);
    suite.addTestSuite(PointsTest.class);
    suite.addTestSuite(PyrotechnicsTest.class);
    suite.addTestSuite(QuenchTest.class);
    suite.addTestSuite(RampageTest.class);
    suite.addTestSuite(RepulsionTest.class);
    suite.addTestSuite(RideTest.class);
    suite.addTestSuite(SeparationTest.class);
    suite.addTestSuite(SharpshootingTest.class);
    suite.addTestSuite(ShortsightednessTest.class);
    suite.addTestSuite(SpaceMakerTest.class);
    suite.addTestSuite(StillTest.class);
    suite.addTestSuite(StormTest.class);
    suite.addTestSuite(StupidityTest.class);
    suite.addTestSuite(SummonsTest.class);
    suite.addTestSuite(TalismanTest.class);
    suite.addTestSuite(TormentTest.class);
    suite.addTestSuite(TripleTest.class);
    suite.addTestSuite(TurmoilTest.class);
    suite.addTestSuite(UncertaintyTest.class);
    suite.addTestSuite(VenomTest.class);
    suite.addTestSuite(VitalityTest.class);
    suite.addTestSuite(VirtueTest.class);
    suite.addTestSuite(WizardWingsTest.class);
    return suite;
  }

  public static void main(final String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
