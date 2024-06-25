package chaos.common.spell;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Links all the tests in this package.
 * @author Sean A. Irvine
 */
public class AllTests extends TestSuite {

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AllianceTest.class);
    suite.addTestSuite(AlterRealityTest.class);
    suite.addTestSuite(AnimateTest.class);
    suite.addTestSuite(ArcheryTest.class);
    suite.addTestSuite(ArmourTest.class);
    suite.addTestSuite(BallLightningTest.class);
    suite.addTestSuite(BetrayalTest.class);
    suite.addTestSuite(BowBreakerTest.class);
    suite.addTestSuite(BuryTest.class);
    suite.addTestSuite(CloakTest.class);
    suite.addTestSuite(ClumsyTest.class);
    suite.addTestSuite(CombatTest.class);
    suite.addTestSuite(ConsecrateTest.class);
    suite.addTestSuite(ConvertTest.class);
    suite.addTestSuite(CursedSwordTest.class);
    suite.addTestSuite(DarkPowerTest.class);
    suite.addTestSuite(DemonifyTest.class);
    suite.addTestSuite(DemonicTouchTest.class);
    suite.addTestSuite(DemoteTest.class);
    suite.addTestSuite(DexterityTest.class);
    suite.addTestSuite(DiseaseTest.class);
    suite.addTestSuite(DragonAscentTest.class);
    suite.addTestSuite(EarthbindTest.class);
    suite.addTestSuite(DummyPowerUpTest.class);
    suite.addTestSuite(EarthquakeShieldTest.class);
    suite.addTestSuite(ExorciseTest.class);
    suite.addTestSuite(FireballTest.class);
    suite.addTestSuite(FireShieldTest.class);
    suite.addTestSuite(FleshToStoneTest.class);
    suite.addTestSuite(FloodShieldTest.class);
    suite.addTestSuite(FlyTest.class);
    suite.addTestSuite(FreeTest.class);
    suite.addTestSuite(FreezeTest.class);
    suite.addTestSuite(HealTest.class);
    suite.addTestSuite(HerbicideTest.class);
    suite.addTestSuite(HorrorTest.class);
    suite.addTestSuite(HyadicTest.class);
    suite.addTestSuite(IceBreathTest.class);
    suite.addTestSuite(IdiocyTest.class);
    suite.addTestSuite(InvertTest.class);
    suite.addTestSuite(JusticeTest.class);
    suite.addTestSuite(KillTest.class);
    suite.addTestSuite(LichTest.class);
    suite.addTestSuite(LifeLeechTest.class);
    suite.addTestSuite(LightningTest.class);
    suite.addTestSuite(LifeAttackTest.class);
    suite.addTestSuite(MagicBoltTest.class);
    suite.addTestSuite(MagicSwordTest.class);
    suite.addTestSuite(MassMorphTest.class);
    suite.addTestSuite(MergeTest.class);
    suite.addTestSuite(NukeTest.class);
    suite.addTestSuite(NullifyTest.class);
    suite.addTestSuite(PoisonDaggerTest.class);
    suite.addTestSuite(PromoteTest.class);
    suite.addTestSuite(ProtectionTest.class);
    suite.addTestSuite(QuickshotTest.class);
    suite.addTestSuite(RaiseDeadTest.class);
    suite.addTestSuite(RangeBoostTest.class);
    suite.addTestSuite(ReflectorTest.class);
    suite.addTestSuite(ReincarnateTest.class);
    suite.addTestSuite(ReplicateTest.class);
    suite.addTestSuite(RequestTest.class);
    suite.addTestSuite(RestorationTest.class);
    suite.addTestSuite(RevealTest.class);
    suite.addTestSuite(SanctifyTest.class);
    suite.addTestSuite(ShockerTest.class);
    suite.addTestSuite(SimulacrumTest.class);
    suite.addTestSuite(SleepTest.class);
    suite.addTestSuite(StopTest.class);
    suite.addTestSuite(SpeedTest.class);
    suite.addTestSuite(SubversionTest.class);
    suite.addTestSuite(SwapTest.class);
    suite.addTestSuite(TeleportTest.class);
    suite.addTestSuite(ThunderboltTest.class);
    suite.addTestSuite(TouchOfGodTest.class);
    suite.addTestSuite(VanishTest.class);
    suite.addTestSuite(VengeanceTest.class);
    suite.addTestSuite(WakeTest.class);
    suite.addTestSuite(WisdomTest.class);
    suite.addTestSuite(XRayTest.class);
    return suite;
  }

  public static void main(final String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
