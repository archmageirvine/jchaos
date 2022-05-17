package irvine.util.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import chaos.util.Sleep;

/**
 * Draw regular walls.
 * @author Sean A. Irvine
 */
public final class Walls {

  static final int[] BRICK_COLOURS = {0xFF653626, 0xFF824A31, 0xFF7E3B2B, 0xFF682920, 0xFF854730};
  static final int[] MORTAR_COLOURS = {0xCCA38F6C, 0xCC9D8569, 0xA0A38F6C, 0xA09D8569};

  private Walls() { }

  /**
   * Draw a wall with given number of bricks in vertical direction.
   *
   * @param g place to draw
   * @param x x-coordinate
   * @param y y-coordinate
   * @param w pixel width (should be a power of 2)
   * @param rows number of rows of bricks
   * @param cols number of columns of bricks
   * @param brickColours brick colours
   * @param mortarColours mortar colours
   */
  public static void drawWall(final Graphics g, final int x, final int y, final int w, final int rows, final int cols, final int[] brickColours, final int[] mortarColours) {
    if (w < 16 || rows < 1 || rows > w || (w & (w - 1)) != 0 || cols < 1) {
      throw new IllegalArgumentException();
    }
    if (g != null) {
      // Draw the brick field
      final BufferedImage im = Stipple.stipple(0, 0, w, w, brickColours).toBufferedImage();
      g.drawImage(im, x, y, null);
      // Draw the mortar lines
      int thickness = 1;
      int q = w >>> 5;
      while (q != 0) {
        ++thickness;
        q >>>= 1;
      }
      // Draw horizontal mortar
      final int halfMortarHeight = Math.max(1, thickness / 2);
      final BufferedImage horiz2 = Stipple.stipple(0, 0, w, halfMortarHeight, mortarColours).toBufferedImage();
      g.drawImage(horiz2, x, y, null); // top line
      g.drawImage(horiz2, x, y + w - horiz2.getHeight(), null); // bottom line
      for (int k = 1; k < rows; ++k) {
        final BufferedImage horiz = Stipple.stipple(0, 0, w, thickness, mortarColours).toBufferedImage();
        g.drawImage(horiz, x, y + k * w / rows - thickness / 2, null);
      }
      // Draw vertical mortar
      final BufferedImage vert = Stipple.stipple(0, 0, thickness, w / rows, mortarColours).toBufferedImage();
      final BufferedImage vert2 = Stipple.stipple(0, 0, halfMortarHeight, w / rows, mortarColours).toBufferedImage();
      for (int j = 0; j < rows; j += 2) {
        for (int k = 1; k < cols; ++k) {
          g.drawImage(vert, x + k * w / cols - thickness / 2, y + j * w / rows, null);
        }
        g.drawImage(vert2, x, y + j * w / rows, null);
        g.drawImage(vert2, x + w - vert2.getWidth(), y + j * w / rows, null);
      }
      final int halfBrickOffset = x + w / cols / 2;
      for (int j = 1; j < rows; j += 2) {
        for (int k = 0; k < cols; ++k) {
          g.drawImage(vert, halfBrickOffset + k * w / cols - thickness / 2, y + j * w / rows, null);
        }
      }
    }
  }

  static void demo(final Graphics g) {
    drawWall(g, 50, 100, 64, 5, 4, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 150, 100, 32, 5, 4, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 200, 100, 16, 5, 4, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 50, 20, 64, 6, 4, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 350, 100, 32, 5, 4, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 350 + 32, 100, 32, 5, 4, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 350, 100 + 32, 32, 5, 4, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 350 + 32, 100 + 32, 32, 5, 4, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 150, 20, 32, 6, 4, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 200, 20, 16, 6, 4, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 350, 20, 32, 6, 4, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 350 + 32, 20, 32, 6, 4, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 350, 20 + 32, 32, 6, 4, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 350 + 32, 20 + 32, 32, 6, 4, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 50, 200, 64, 1, 1, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 150, 200, 32, 1, 1, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 200, 200, 16, 1, 1, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 350, 200, 32, 1, 1, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 350 + 32, 200, 32, 1, 1, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 350, 200 + 32, 32, 1, 1, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 350 + 32, 200 + 32, 32, 1, 1, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 50, 300, 64, 4, 3, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 150, 300, 32, 4, 3, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 200, 300, 16, 4, 3, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 350, 300, 32, 4, 3, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 350 + 32, 300, 32, 4, 3, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 350, 300 + 32, 32, 4, 3, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 350 + 32, 300 + 32, 32, 4, 3, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 50, 400, 64, 4, 4, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 150, 400, 32, 4, 4, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 200, 400, 16, 4, 4, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 350, 400, 32, 4, 4, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 350 + 32, 400, 32, 4, 4, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 350, 400 + 32, 32, 4, 4, BRICK_COLOURS, MORTAR_COLOURS);
    drawWall(g, 350 + 32, 400 + 32, 32, 4, 4, BRICK_COLOURS, MORTAR_COLOURS);
  }

  /**
   * Used for testing.
   * @param args See usage.
   */
  public static void main(final String[] args) {
    final JFrame f = new JFrame("Shield");
    SwingUtilities.invokeLater(() -> {
      f.setSize(530, 560);
      f.setVisible(true);
      for (int k = 0; k < 200; ++k) {
        final Graphics g = f.getGraphics();
        if (g != null) {
          demo(g);
          g.dispose();
        }
        Sleep.sleep(1000);
      }
      f.dispose();
    });
  }

}
