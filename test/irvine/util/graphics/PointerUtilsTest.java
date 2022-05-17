package irvine.util.graphics;

import java.awt.GraphicsEnvironment;

import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class PointerUtilsTest extends TestCase {

  public void test() {
    if (!GraphicsEnvironment.isHeadless()) {
      try {
        PointerUtils.setBlankCursor(null);
        fail();
      } catch (final NullPointerException e) {
        // ok
      }
    }
  }

}
