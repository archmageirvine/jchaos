package irvine.util.graphics;

import irvine.tile.TileImage;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class ButtonTest extends TestCase {




  public void testNull() {
    try {
      new Button(null, 5, 5);
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
  }

  public void testContains() {
    final TileImage i = new TileImage(3, 4);
    i.fill(~0);
    final Button b = new Button(i.toBufferedImage(), 10, 12);
    b.act();
    assertEquals(3, b.getWidth());
    assertEquals(4, b.getHeight());
    assertEquals(10, b.getX());
    assertEquals(12, b.getY());
    assertTrue(b.contains(10, 12));
    assertTrue(b.contains(10, 13));
    assertTrue(b.contains(10, 14));
    assertTrue(b.contains(10, 15));
    assertFalse(b.contains(10, 16));
    assertTrue(b.contains(11, 12));
    assertTrue(b.contains(12, 12));
    assertFalse(b.contains(13, 12));
    final TileImage j = new TileImage(50, 50);
    j.fill(0xFFFF0000);
    final BufferedImage k = j.toBufferedImage();
    final Graphics g = k.getGraphics();
    if (g != null) {
      b.paint(g);
      assertTrue(k.getRGB(10, 12) != k.getRGB(0, 0));
      g.dispose();
    }
  }
}
