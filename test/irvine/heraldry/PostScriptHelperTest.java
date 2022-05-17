package irvine.heraldry;

import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class PostScriptHelperTest extends TestCase {

  public void testOval() {
    assertEquals("gsave 1 2.0 scale newpath 10.0 10.0 10.0 0 360 arc fill grestore", PostScriptHelper.fillOval(20, 40));
    assertEquals("gsave 1 2.0 scale newpath 10.0 10.0 10.0 0 360 arc stroke grestore", PostScriptHelper.drawOval(20, 40));
  }

  public void testFill() {
    assertEquals("newpath 0.0 0.0 moveto 5.0 1.0 lineto 2.0 6.0 lineto closepath fill", PostScriptHelper.fill(new double[] {0, 5, 2}, new double[] {0, 1, 6}));
  }

  public void testStroke() {
    assertEquals("newpath 0.0 0.0 moveto 5.0 1.0 lineto 2.0 6.0 lineto closepath stroke", PostScriptHelper.stroke(new double[] {0, 5, 2}, new double[] {0, 1, 6}));
  }

  public void testClip() {
    assertEquals("newpath 0.0 0.0 moveto 5.0 1.0 lineto 2.0 6.0 lineto closepath clip", PostScriptHelper.clip(new double[] {0, 5, 2}, new double[] {0, 1, 6}));
  }

  public void testFillArc() {
    assertEquals("newpath 2.0 3.0 20.0 10.0 45.0 arc 2.0 3.0 lineto closepath fill", PostScriptHelper.fillArc(2, 3, 20, 10, 45));
  }
}
