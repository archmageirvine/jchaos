package chaos.common;

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
    suite.addTestSuite(ActorTest.class);
    suite.addTestSuite(AttributeTest.class);
    suite.addTestSuite(CastableListTest.class);
    suite.addTestSuite(CastableTest.class);
    suite.addTestSuite(CasterTest.class);
    suite.addTestSuite(CatTest.class);
    suite.addTestSuite(CoreMonsterTest.class);
    suite.addTestSuite(DemonicMonsterTest.class);
    suite.addTestSuite(DragonTest.class);
    suite.addTestSuite(DrainerTest.class);
    suite.addTestSuite(DummyDecrementTest.class);
    suite.addTestSuite(DummyFreeDecrementTest.class);
    suite.addTestSuite(DummyFreeIncrementTest.class);
    suite.addTestSuite(DummyGeneratorTest.class);
    suite.addTestSuite(DummyIncrementTest.class);
    suite.addTestSuite(DummyShieldTest.class);
    suite.addTestSuite(DummyWallTest.class);
    suite.addTestSuite(DumpCreatureTest.class);
    suite.addTestSuite(FreeCastableTest.class);
    suite.addTestSuite(FrequencyTableTest.class);
    suite.addTestSuite(GrowthHelperTest.class);
    suite.addTestSuite(HumanoidTest.class);
    suite.addTestSuite(ListCasterTest.class);
    suite.addTestSuite(MaterialGrowthTest.class);
    suite.addTestSuite(MaterialMonsterTest.class);
    suite.addTestSuite(MaterialMonsterMountTest.class);
    suite.addTestSuite(MaterialMonsterRideTest.class);
    suite.addTestSuite(MonsterTest.class);
    suite.addTestSuite(MythosMonsterTest.class);
    suite.addTestSuite(PolycasterTest.class);
    suite.addTestSuite(PowerUpsTest.class);
    suite.addTestSuite(RealmTest.class);
    suite.addTestSuite(StateTest.class);
    suite.addTestSuite(UndeadMonsterTest.class);
    suite.addTestSuite(UnicasterTest.class);
    suite.addTest(chaos.common.beam.AllTests.suite());
    suite.addTest(chaos.common.dragon.AllTests.suite());
    suite.addTest(chaos.common.free.AllTests.suite());
    suite.addTest(chaos.common.growth.AllTests.suite());
    suite.addTest(chaos.common.inanimate.AllTests.suite());
    suite.addTest(chaos.common.monster.AllTests.suite());
    suite.addTest(chaos.common.mythos.AllTests.suite());
    suite.addTest(chaos.common.spell.AllTests.suite());
    suite.addTest(chaos.common.wizard.AllTests.suite());
    return suite;
  }

  public static void main(final String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
