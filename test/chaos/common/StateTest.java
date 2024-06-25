package chaos.common;

import irvine.TestUtils;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class StateTest extends TestCase {

  public void testEnum() {
    TestUtils.testEnum(State.class, "[ACTIVE, DEAD, ASLEEP]");
  }
}
