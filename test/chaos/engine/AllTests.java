package chaos.engine;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Links all the tests in this package.
 * @author Sean A. Irvine
 */
public class AllTests extends TestSuite {

  public static Test suite() {
    final TestSuite suite = new TestSuite();
    suite.addTestSuite(AiEngineTest.class);
    suite.addTestSuite(CursorHelperTest.class);
    suite.addTestSuite(EngineFactoryTest.class);
    suite.addTestSuite(HumanEngineHelperTest.class);
    suite.addTestSuite(HumanEngineStateTest.class);
    suite.addTestSuite(HumanEngineTest.class);
    suite.addTestSuite(HumanMoveHelperTest.class);
    suite.addTestSuite(IntelligentMeditationCasterTest.class);
    suite.addTestSuite(WeightTest.class);
    suite.addTestSuite(WizardFieldTest.class);
    return suite;
  }

  public static void main(final String[] args) {
    junit.textui.TestRunner.run(suite());
  }
}
