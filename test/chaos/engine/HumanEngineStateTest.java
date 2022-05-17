package chaos.engine;

import irvine.TestUtils;
import junit.framework.TestCase;

/**
 * JUnit tests for the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class HumanEngineStateTest extends TestCase {


  public void test() {
    TestUtils.testEnum(HumanEngineState.class, "[AWAITING_SELECT, MOVE_IN_PROGRESS, CAST_CLICK, IDLE, SHOOTING, MOUNTED_SHOOTING, DISMOUNTING]");
  }
}
