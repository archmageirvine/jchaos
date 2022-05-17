package irvine.heraldry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import chaos.graphics.MockGraphics;
import irvine.TestUtils;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class ShieldTest extends TestCase {

  public void testPostScript() throws IOException {
    final Shield s = new Shield();
    assertEquals(21, s.midHeight(42));
    assertEquals(0.5, s.midHeightPS());
    assertTrue(s.fusil() instanceof Lozenge);
    try (final ByteArrayOutputStream bos = new ByteArrayOutputStream();
         final PrintStream out = new PrintStream(bos)) {
      s.renderShapePS(out);
      final String sh = bos.toString();
      //System.out.println(sh);
      TestUtils.containsAll(sh,
        "0.000 0.000 0.000 setrgbcolor",
        "newpath 0.0 0.8660254037844386 1.0 -60.0 0.0 arc stroke",
        "newpath 1.0 0.8660254037844386 1.0 180.0 240.0 arc stroke",
        "newpath 0 0.8660254037844386 moveto 0 1.3333333333333333 lineto 1 1.3333333333333333 lineto 1 0.8660254037844386 lineto stroke");
    }
  }

  public void test2() {
    final Shield s = new Shield();
    assertEquals(21, s.midHeight(42));
    assertEquals(0.5, s.midHeightPS());
    assertTrue(s.fusil() instanceof Lozenge);
    final MockGraphics graphics = new MockGraphics();
    s.renderShape(graphics, 60);
    final String sh = graphics.toString();
    //System.out.println(sh);
    assertEquals("setColor(java.awt.Color[r=0,g=0,b=0])#drawArc(-60,-32,120,120,0,-60)#drawArc(0,-32,120,120,180,60)#L(0,28,0,0)#L(0,0,60,0)#L(60,0,60,28)#", sh);
  }
}
