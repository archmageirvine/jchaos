package chaos.graphics;

import chaos.common.Realm;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class PentagramHighlighterTest extends TestCase {

  public void test() {
    final MockGraphics graphics = new MockGraphics();
    final PentagramHighlighter ph = new PentagramHighlighter(64, graphics, 42, 84);
    ph.reset();
    ph.highlight(Realm.DEMONIC, Realm.ETHERIC, Realm.MATERIAL);
    final String s = graphics.toString();
    assertEquals("I(42,84,null)#setColor(java.awt.Color[r=255,g=0,b=0])#fillOval(120,168,5,5)#setColor(java.awt.Color[r=0,g=0,b=255])#fillOval(104,124,5,5)#setColor(java.awt.Color[r=0,g=255,b=0])#fillOval(56,124,5,5)#", s);
  }
}
