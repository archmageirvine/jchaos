package irvine.util.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import irvine.tile.TileImage;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class LightningTest extends TestCase {


  public void testBad() {
    try {
      Lightning.draw(null, 0, 0, 0, 0, 0);
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
  }

  public void testGood() {
    final BufferedImage i = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
    final Graphics g = i.getGraphics();
    try {
      g.setColor(Color.GREEN);
      g.fillRect(0, 0, 100, 100);
      Lightning.draw(g, 10, 10, 90, 90, 0);
      // result should only have green and black pixels
      final TileImage ir = new TileImage(i);
      for (int y = 0; y < ir.getHeight(); ++y) {
        for (int x = 0; x < ir.getWidth(); ++x) {
          final int p = ir.getPixel(x, y);
          if (p != 0xFF00FF00) {
            assertEquals(0xFF000000, p);
          }
        }
      }
      assertEquals(0xFF000000, ir.getPixel(10, 10));
      assertEquals(0xFF000000, ir.getPixel(90, 90));
    } finally {
      g.dispose();
    }
  }

  public void testGood2() {
    final BufferedImage i = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
    final Graphics g = i.getGraphics();
    try {
      g.setColor(Color.GREEN);
      g.fillRect(0, 0, 10, 10);
      Lightning.draw(g, 0, 2, 9, 7, 0);
      final TileImage ir = new TileImage(i);
      assertEquals(0xFF00FF00, ir.getPixel(0, 0));
      assertEquals(0xFF00FF00, ir.getPixel(1, 0));
      assertEquals(0xFF00FF00, ir.getPixel(2, 0));
      assertEquals(0xFF00FF00, ir.getPixel(3, 0));
      assertEquals(0xFF00FF00, ir.getPixel(4, 0));
      assertEquals(0xFF00FF00, ir.getPixel(5, 0));
      assertEquals(0xFF00FF00, ir.getPixel(6, 0));
      assertEquals(0xFF00FF00, ir.getPixel(7, 0));
      assertEquals(0xFF00FF00, ir.getPixel(8, 0));
      assertEquals(0xFF00FF00, ir.getPixel(9, 0));
      assertEquals(0xFF00FF00, ir.getPixel(0, 1));
      assertEquals(0xFF00FF00, ir.getPixel(1, 1));
      assertEquals(0xFF00FF00, ir.getPixel(2, 1));
      assertEquals(0xFF00FF00, ir.getPixel(3, 1));
      assertEquals(0xFF00FF00, ir.getPixel(4, 1));
      assertEquals(0xFF00FF00, ir.getPixel(5, 1));
      assertEquals(0xFF00FF00, ir.getPixel(6, 1));
      assertEquals(0xFF00FF00, ir.getPixel(7, 1));
      assertEquals(0xFF00FF00, ir.getPixel(8, 1));
      assertEquals(0xFF00FF00, ir.getPixel(9, 1));
      assertEquals(0xFF000000, ir.getPixel(0, 2));
      assertEquals(0xFF000000, ir.getPixel(1, 2));
      assertEquals(0xFF00FF00, ir.getPixel(2, 2));
      assertEquals(0xFF00FF00, ir.getPixel(3, 2));
      assertEquals(0xFF00FF00, ir.getPixel(4, 2));
      assertEquals(0xFF00FF00, ir.getPixel(5, 2));
      assertEquals(0xFF00FF00, ir.getPixel(6, 2));
      assertEquals(0xFF00FF00, ir.getPixel(7, 2));
      assertEquals(0xFF00FF00, ir.getPixel(8, 2));
      assertEquals(0xFF00FF00, ir.getPixel(9, 2));
      assertEquals(0xFF00FF00, ir.getPixel(0, 3));
      assertEquals(0xFF000000, ir.getPixel(1, 3));
      assertEquals(0xFF000000, ir.getPixel(2, 3));
      assertEquals(0xFF000000, ir.getPixel(3, 3));
      assertEquals(0xFF000000, ir.getPixel(4, 3));
      assertEquals(0xFF00FF00, ir.getPixel(5, 3));
      assertEquals(0xFF00FF00, ir.getPixel(6, 3));
      assertEquals(0xFF00FF00, ir.getPixel(7, 3));
      assertEquals(0xFF00FF00, ir.getPixel(8, 3));
      assertEquals(0xFF00FF00, ir.getPixel(9, 3));
      assertEquals(0xFF00FF00, ir.getPixel(0, 4));
      assertEquals(0xFF00FF00, ir.getPixel(1, 4));
      assertEquals(0xFF00FF00, ir.getPixel(2, 4));
      assertEquals(0xFF000000, ir.getPixel(3, 4));
      assertEquals(0xFF000000, ir.getPixel(4, 4));
      assertEquals(0xFF000000, ir.getPixel(5, 4));
      assertEquals(0xFF000000, ir.getPixel(6, 4));
      assertEquals(0xFF000000, ir.getPixel(7, 4));
      assertEquals(0xFF00FF00, ir.getPixel(8, 4));
      assertEquals(0xFF00FF00, ir.getPixel(9, 4));
      assertEquals(0xFF00FF00, ir.getPixel(0, 5));
      assertEquals(0xFF00FF00, ir.getPixel(1, 5));
      assertEquals(0xFF00FF00, ir.getPixel(2, 5));
      assertEquals(0xFF00FF00, ir.getPixel(3, 5));
      assertEquals(0xFF00FF00, ir.getPixel(4, 5));
      assertEquals(0xFF000000, ir.getPixel(5, 5));
      assertEquals(0xFF000000, ir.getPixel(6, 5));
      assertEquals(0xFF00FF00, ir.getPixel(7, 5));
      assertEquals(0xFF000000, ir.getPixel(8, 5));
      assertEquals(0xFF000000, ir.getPixel(9, 5));
      assertEquals(0xFF00FF00, ir.getPixel(0, 6));
      assertEquals(0xFF00FF00, ir.getPixel(1, 6));
      assertEquals(0xFF00FF00, ir.getPixel(2, 6));
      assertEquals(0xFF00FF00, ir.getPixel(3, 6));
      assertEquals(0xFF00FF00, ir.getPixel(4, 6));
      assertEquals(0xFF00FF00, ir.getPixel(5, 6));
      assertEquals(0xFF00FF00, ir.getPixel(6, 6));
      assertEquals(0xFF000000, ir.getPixel(7, 6));
      assertEquals(0xFF000000, ir.getPixel(8, 6));
      assertEquals(0xFF00FF00, ir.getPixel(9, 6));
      assertEquals(0xFF00FF00, ir.getPixel(0, 7));
      assertEquals(0xFF00FF00, ir.getPixel(1, 7));
      assertEquals(0xFF00FF00, ir.getPixel(2, 7));
      assertEquals(0xFF00FF00, ir.getPixel(3, 7));
      assertEquals(0xFF00FF00, ir.getPixel(4, 7));
      assertEquals(0xFF00FF00, ir.getPixel(5, 7));
      assertEquals(0xFF00FF00, ir.getPixel(6, 7));
      assertEquals(0xFF00FF00, ir.getPixel(7, 7));
      assertEquals(0xFF00FF00, ir.getPixel(8, 7));
      assertEquals(0xFF000000, ir.getPixel(9, 7));
      assertEquals(0xFF00FF00, ir.getPixel(0, 8));
      assertEquals(0xFF00FF00, ir.getPixel(1, 8));
      assertEquals(0xFF00FF00, ir.getPixel(2, 8));
      assertEquals(0xFF00FF00, ir.getPixel(3, 8));
      assertEquals(0xFF00FF00, ir.getPixel(4, 8));
      assertEquals(0xFF00FF00, ir.getPixel(5, 8));
      assertEquals(0xFF00FF00, ir.getPixel(6, 8));
      assertEquals(0xFF00FF00, ir.getPixel(7, 8));
      assertEquals(0xFF00FF00, ir.getPixel(8, 8));
      assertEquals(0xFF00FF00, ir.getPixel(9, 8));
      assertEquals(0xFF00FF00, ir.getPixel(0, 9));
      assertEquals(0xFF00FF00, ir.getPixel(1, 9));
      assertEquals(0xFF00FF00, ir.getPixel(2, 9));
      assertEquals(0xFF00FF00, ir.getPixel(3, 9));
      assertEquals(0xFF00FF00, ir.getPixel(4, 9));
      assertEquals(0xFF00FF00, ir.getPixel(5, 9));
      assertEquals(0xFF00FF00, ir.getPixel(6, 9));
      assertEquals(0xFF00FF00, ir.getPixel(7, 9));
      assertEquals(0xFF00FF00, ir.getPixel(8, 9));
      assertEquals(0xFF00FF00, ir.getPixel(9, 9));
    } finally {
      g.dispose();
    }
  }

  public void testGood3() {
    final BufferedImage i = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
    final Graphics g = i.getGraphics();
    try {
      g.setColor(Color.GREEN);
      g.fillRect(0, 0, 10, 10);
      Lightning.draw(g, 2, 0, 7, 9, 0);
      final TileImage ir = new TileImage(i);
      assertEquals(0xFF00FF00, ir.getPixel(0, 0));
      assertEquals(0xFF00FF00, ir.getPixel(8, 0));
      assertEquals(0xFF00FF00, ir.getPixel(9, 0));
      assertEquals(0xFF00FF00, ir.getPixel(0, 1));
      assertEquals(0xFF00FF00, ir.getPixel(1, 1));
      assertEquals(0xFF00FF00, ir.getPixel(2, 1));
      assertEquals(0xFF000000, ir.getPixel(3, 1));
      assertEquals(0xFF00FF00, ir.getPixel(4, 1));
      assertEquals(0xFF00FF00, ir.getPixel(5, 1));
      assertEquals(0xFF00FF00, ir.getPixel(6, 1));
      assertEquals(0xFF00FF00, ir.getPixel(7, 1));
      assertEquals(0xFF00FF00, ir.getPixel(8, 1));
      assertEquals(0xFF00FF00, ir.getPixel(9, 1));
      assertEquals(0xFF00FF00, ir.getPixel(0, 2));
      assertEquals(0xFF00FF00, ir.getPixel(1, 2));
      assertEquals(0xFF00FF00, ir.getPixel(2, 2));
      assertEquals(0xFF000000, ir.getPixel(3, 2));
      assertEquals(0xFF000000, ir.getPixel(4, 2));
      assertEquals(0xFF00FF00, ir.getPixel(5, 2));
      assertEquals(0xFF00FF00, ir.getPixel(6, 2));
      assertEquals(0xFF00FF00, ir.getPixel(1, 3));
      assertEquals(0xFF00FF00, ir.getPixel(2, 3));
      assertEquals(0xFF00FF00, ir.getPixel(3, 3));
      assertEquals(0xFF000000, ir.getPixel(4, 3));
      assertEquals(0xFF000000, ir.getPixel(5, 3));
      assertEquals(0xFF00FF00, ir.getPixel(6, 3));
      assertEquals(0xFF00FF00, ir.getPixel(7, 3));
      assertEquals(0xFF00FF00, ir.getPixel(8, 3));
      assertEquals(0xFF00FF00, ir.getPixel(9, 3));
      assertEquals(0xFF00FF00, ir.getPixel(0, 4));
      assertEquals(0xFF00FF00, ir.getPixel(1, 4));
      assertEquals(0xFF00FF00, ir.getPixel(2, 4));
      assertEquals(0xFF00FF00, ir.getPixel(3, 4));
      assertEquals(0xFF000000, ir.getPixel(4, 4));
      assertEquals(0xFF000000, ir.getPixel(5, 4));
      assertEquals(0xFF00FF00, ir.getPixel(6, 4));
      assertEquals(0xFF00FF00, ir.getPixel(7, 4));
      assertEquals(0xFF00FF00, ir.getPixel(2, 5));
      assertEquals(0xFF00FF00, ir.getPixel(3, 5));
      assertEquals(0xFF00FF00, ir.getPixel(4, 5));
      assertEquals(0xFF000000, ir.getPixel(5, 5));
      assertEquals(0xFF000000, ir.getPixel(6, 5));
      assertEquals(0xFF00FF00, ir.getPixel(7, 5));
      assertEquals(0xFF00FF00, ir.getPixel(8, 5));
      assertEquals(0xFF00FF00, ir.getPixel(9, 5));
      assertEquals(0xFF00FF00, ir.getPixel(0, 6));
      assertEquals(0xFF00FF00, ir.getPixel(1, 6));
      assertEquals(0xFF00FF00, ir.getPixel(2, 6));
      assertEquals(0xFF00FF00, ir.getPixel(3, 6));
      assertEquals(0xFF00FF00, ir.getPixel(4, 6));
      assertEquals(0xFF000000, ir.getPixel(5, 6));
      assertEquals(0xFF00FF00, ir.getPixel(6, 6));
      assertEquals(0xFF000000, ir.getPixel(7, 6));
      assertEquals(0xFF00FF00, ir.getPixel(8, 6));
      assertEquals(0xFF00FF00, ir.getPixel(9, 6));
      assertEquals(0xFF00FF00, ir.getPixel(0, 7));
      assertEquals(0xFF00FF00, ir.getPixel(1, 7));
      assertEquals(0xFF00FF00, ir.getPixel(2, 7));
      assertEquals(0xFF00FF00, ir.getPixel(3, 7));
      assertEquals(0xFF00FF00, ir.getPixel(4, 7));
      assertEquals(0xFF00FF00, ir.getPixel(5, 7));
      assertEquals(0xFF000000, ir.getPixel(6, 7));
      assertEquals(0xFF00FF00, ir.getPixel(7, 7));
      assertEquals(0xFF000000, ir.getPixel(8, 7));
      assertEquals(0xFF00FF00, ir.getPixel(2, 8));
      assertEquals(0xFF00FF00, ir.getPixel(3, 8));
      assertEquals(0xFF00FF00, ir.getPixel(4, 8));
      assertEquals(0xFF00FF00, ir.getPixel(5, 8));
      assertEquals(0xFF000000, ir.getPixel(6, 8));
      assertEquals(0xFF00FF00, ir.getPixel(7, 8));
      assertEquals(0xFF00FF00, ir.getPixel(8, 8));
      assertEquals(0xFF00FF00, ir.getPixel(9, 8));
      assertEquals(0xFF00FF00, ir.getPixel(0, 9));
      assertEquals(0xFF00FF00, ir.getPixel(1, 9));
    } finally {
      g.dispose();
    }
  }

  public void testGood4() {
    final BufferedImage i = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
    final Graphics g = i.getGraphics();
    try {
      g.setColor(Color.GREEN);
      g.fillRect(0, 0, 10, 10);
      Lightning.draw(g, 1, 1, 1, 1, 0);
      final TileImage ir = new TileImage(i);
      assertEquals(0xFF00FF00, ir.getPixel(0, 0));
      assertEquals(0xFF000000, ir.getPixel(1, 0));
      assertEquals(0xFF00FF00, ir.getPixel(2, 0));
      assertEquals(0xFF00FF00, ir.getPixel(0, 1));
      assertEquals(0xFF000000, ir.getPixel(1, 1));
      assertEquals(0xFF00FF00, ir.getPixel(2, 1));
      assertEquals(0xFF00FF00, ir.getPixel(0, 2));
      assertEquals(0xFF000000, ir.getPixel(1, 2));
      assertEquals(0xFF00FF00, ir.getPixel(2, 2));
    } finally {
      g.dispose();
    }
  }

  private static class MyThread extends Thread {
    private final Graphics mG;

    MyThread(final Graphics g) {
      mG = g;
    }

    @Override
    public void run() {
      Lightning.draw(mG, 1, 1, 1, 1, 1000);
    }
  }

  /**
   * A tricky test that attempts to examine the colors part way through a
   * cycle of the lightning effect.
   */
  public void testTricky() {
    final BufferedImage i = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
    final Graphics g = i.getGraphics();
    try {
      g.setColor(Color.GREEN);
      g.fillRect(0, 0, 10, 10);
      boolean sawWhite = false;
      boolean sawCyan = false;
      // this test will normally pass when k = 0
      for (int k = 0; k < 10 && (!sawWhite || !sawCyan); ++k) {
        final Thread t = new MyThread(g);
        t.start();
        final long stopTime = System.currentTimeMillis() + 1000;
        try {
          Thread.sleep(50);
        } catch (final InterruptedException e) {
          // too bad
        }
        do {
          final TileImage ir = new TileImage(i);
          final int p = ir.getPixel(1, 1);
          if (p == 0xFFFFFFFF) {
            sawWhite = true;
          } else if (p == 0xFF00FFFF) {
            sawCyan = true;
          }
        } while (System.currentTimeMillis() < stopTime && (!sawWhite || !sawCyan));
        try {
          t.join();
        } catch (final InterruptedException e) {
          // too bad
        }
      }
      assertTrue(sawWhite);
      assertTrue(sawCyan);
    } finally {
      g.dispose();
    }
  }

  public void testNegativeTime() {
    final BufferedImage i = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
    final Graphics g = i.getGraphics();
    try {
      g.setColor(Color.GREEN);
      g.fillRect(0, 0, 10, 10);
      final long current = System.currentTimeMillis();
      Lightning.draw(g, 1, 1, 1, 1, -10000);
      assertTrue(System.currentTimeMillis() - current < 1000);
    } finally {
      g.dispose();
    }
  }

}
