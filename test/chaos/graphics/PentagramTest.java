package chaos.graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;

import chaos.common.Realm;
import irvine.TestUtils;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class PentagramTest extends TestCase {

  public void test() {
    final Pentagram mPentagram = new Pentagram(16);
    final MockGraphics g = new MockGraphics();
    mPentagram.highlightPentagram(g, null, Color.RED, 1, 2);
    mPentagram.highlightPentagram(g, Realm.ETHERIC, Color.RED, 3, 4);
    mPentagram.highlightPentagram(g, Realm.MATERIAL, Color.RED, 5, 6);
    mPentagram.highlightPentagram(g, Realm.MYTHOS, Color.RED, 7, 8);
    mPentagram.highlightPentagram(g, Realm.DRACONIC, Color.BLUE, 9, 10);
    mPentagram.highlightPentagram(g, Realm.DEMONIC, Color.GREEN, 11, 12);
    mPentagram.highlightPentagram(g, Realm.NONE, Color.GREEN, 13, 14);
    final String z = g.toString();
    assertTrue(z.contains("null)#setColor(java.awt.Color[r=255,g=0,b=0])#fillOval("));
    assertTrue(z.contains("#setColor(java.awt.Color[r=0,g=255,b=0])#fillOval("));
    final BufferedImage im = (BufferedImage) TestUtils.getField("mPentagram", mPentagram);
    assertEquals(40, im.getWidth());
    assertEquals(32, im.getHeight());
    int c = 0;
    final int cyan = Color.CYAN.getRGB();
    for (int y = 0; y < im.getHeight(); ++y) {
      for (int x = 0; x < im.getWidth(); ++x) {
        if (im.getRGB(x, y) == cyan) {
          ++c;
        }
      }
    }
    assertEquals(136, c);
  }
}
