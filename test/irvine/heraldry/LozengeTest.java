package irvine.heraldry;

import chaos.graphics.MockGraphics;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class LozengeTest extends ShieldTest {

  public void testL() {
    final Lozenge s = new Lozenge();
    final MockGraphics graphics = new MockGraphics();
    s.renderShape(graphics, 60, Tincture.MURREY);
    final String sh = graphics.toString();
    //System.out.println(sh);
    assertEquals("setColor(java.awt.Color[r=188,g=143,b=143])#fillPolygon(4)#", sh);
  }
}
