package chaos.graphics;

import irvine.TestUtils;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class CursorNameTest extends TestCase {

  public void testCursorName() {
    TestUtils.testEnum(CursorName.class, "[BLANK, SHOOT, WINGS, CAST, CROSS, DISMOUNT]");
  }
}

