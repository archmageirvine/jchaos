package chaos.graphics;

import java.awt.image.BufferedImage;

import junit.framework.TestCase;

/**
 * JUnit tests for the Attack class.
 *
 * @author Sean A. Irvine
 */
public class AttackTest extends TestCase {


  public void test() {
    final BufferedImage[][] pos = new BufferedImage[120][];
    final BufferedImage[][] neg = new BufferedImage[120][];
    for (int i = 0; i < pos.length; ++i) {
      pos[i] = Attack.getEffect(16, i);
      assertEquals(2, pos[i].length);
      assertNotNull(pos[i][0]);
      assertNotNull(pos[i][1]);
      neg[i] = Attack.getEffect(16, -i);
      assertEquals(2, neg[i].length);
      assertNotNull(neg[i][0]);
      assertNotNull(neg[i][1]);
    }
    for (int i = 0; i < pos.length; ++i) {
      assertTrue(pos[i] == Attack.getEffect(16, i));
      assertTrue(neg[i] == Attack.getEffect(16, -i));
    }
    for (int i = 1; i < 33; ++i) {
      assertFalse(String.valueOf(i), pos[i] == neg[i]);
      assertFalse(pos[i][0] == neg[i][0]);
      assertFalse(pos[i][1] == neg[i][1]);
      assertFalse(pos[i] == pos[i + 1]);
      assertFalse(neg[i] == neg[i + 1]);
    }
    for (int i = 33; i < pos.length; ++i) {
      assertTrue(String.valueOf(i), pos[i] == neg[i]);
      if (i < pos.length - 1) {
        assertTrue(pos[i] == pos[i + 1]);
      }
    }
    assertTrue(pos[0] == neg[0]);
  }
}
