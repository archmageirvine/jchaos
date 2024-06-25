package chaos.util;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Links all the tests in this package.
 * @author Sean A. Irvine
 */
public class AllTests extends TestSuite {

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AttackCellEffectEventTest.class);
    suite.addTestSuite(AudioEventTest.class);
    suite.addTestSuite(BooleanLockTest.class);
    suite.addTestSuite(CastUtilsTest.class);
    suite.addTestSuite(CellEffectEventTest.class);
    suite.addTestSuite(CellEffectTypeTest.class);
    suite.addTestSuite(ChaosPropertiesTest.class);
    suite.addTestSuite(CloneTest.class);
    suite.addTestSuite(CombatUtilsTest.class);
    suite.addTestSuite(CurrentMoverEventTest.class);
    suite.addTestSuite(DefaultEventGeneratorTest.class);
    suite.addTestSuite(EventLoggerTest.class);
    suite.addTestSuite(EventsTest.class);
    suite.addTestSuite(HighlightEventTest.class);
    suite.addTestSuite(IntegerQueueTest.class);
    suite.addTestSuite(IntelligentWallTest.class);
    suite.addTestSuite(MergeTest.class);
    suite.addTestSuite(MovementUtilsTest.class);
    suite.addTestSuite(NameUtilsTest.class);
    suite.addTestSuite(PolycellAttackEventTest.class);
    suite.addTestSuite(PolycellEffectEventTest.class);
    suite.addTestSuite(PolyshieldDestroyEventTest.class);
    suite.addTestSuite(PolyshieldEventTest.class);
    suite.addTestSuite(PowerUpEventTest.class);
    suite.addTestSuite(RandomTest.class);
    suite.addTestSuite(RankingComparatorTest.class);
    suite.addTestSuite(RankingTableTest.class);
    suite.addTestSuite(RestoreTest.class);
    suite.addTestSuite(ShieldDestroyedEventTest.class);
    suite.addTestSuite(ShieldGrantedEventTest.class);
    suite.addTestSuite(ShootIconEventTest.class);
    suite.addTestSuite(SleepTest.class);
    suite.addTestSuite(TeamUtilsTest.class);
    suite.addTestSuite(TextEventTest.class);
    suite.addTestSuite(TextGridTest.class);
    suite.addTestSuite(WeaponEffectEventTest.class);
    suite.addTestSuite(WeaponEffectTypeTest.class);
    suite.addTestSuite(WingsIconEventTest.class);
    return suite;
  }

  public static void main(final String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
