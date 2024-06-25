package chaos.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import irvine.tile.TileSet;
import irvine.util.graphics.BufferedImageUtils;
import irvine.util.graphics.Stars;
import irvine.util.graphics.Walls;

/**
 * Algorithmically generate some imagery.
 *
 * @author Sean A. Irvine
 */
public abstract class AbstractGenerateGraphics {

  private static final int[] STANDARD_WALL_BRICK_COLOURS = {0xFF653626, 0xFF824A31, 0xFF7E3B2B, 0xFF682920, 0xFF854730};
  private static final int[] STANDARD_WALL_MORTAR_COLOURS = {0xCCA38F6C, 0xCC9D8569, 0xA0A38F6C, 0xA09D8569};
  private static final int[] WEAK_WALL_BRICK_COLOURS = {0xFFD93624, 0xFFDC4A18, 0xFFEF782C, 0xFFCE741C, 0xFFC93629};
  private static final int[] WEAK_WALL_MORTAR_COLOURS = {0x90D7D45B, 0x90C4C34B, 0x90E4D371};
  private static final int[] STRONG_WALL_BRICK_COLOURS = {0xFF4E4E4E, 0xFF6D6D6D, 0xFF787878, 0xFF606060, 0xFF656565};
  private static final int[] STRONG_WALL_MORTAR_COLOURS = {0x906070CF, 0xC06070CF};
  private static final Color FLUORO_PINK = new Color(0xFF, 0x33, 0x99);
  private static final Color FLUORO_GREEN = new Color(0x66, 0xFF, 0x66);
  private static final Color FLUORO_CYAN = new Color(0x66, 0xCC, 0xFF);

  protected abstract int getWidthBits();

  protected int getWidth() {
    return 1 << getWidthBits();
  }

  public TileSet getTileSet() {
    return new TileSet(getWidthBits(), System.getProperty("user.home") + "/jchaos/src/chaos/graphics/active" + getWidth() + "/");
  }

  private void write(final int tilePosition, final BufferedImage i) throws IOException {
    if (tilePosition >= 0) {
      final TileSet ts = getTileSet();
      ts.setImage(tilePosition, i);
    }
  }

  private void makePentagram(final int tilePosition, final double theta) throws IOException {
    final BufferedImage i = new BufferedImage(getWidth(), getWidth(), BufferedImage.TYPE_INT_RGB);
    final Graphics g = i.getGraphics();
    final int c = getWidth() / 2;
    final int d = c - getWidthBits() + 3;
    g.setColor(Color.MAGENTA);
    Stars.drawStar(g, c, c, d, 5, 2, theta);
    g.dispose();
    write(tilePosition, i);
  }

  private void makeHexagon(final int tilePosition, final double theta) throws IOException {
    final BufferedImage i = new BufferedImage(getWidth(), getWidth(), BufferedImage.TYPE_INT_RGB);
    final Graphics g = i.getGraphics();
    final int c = getWidth() / 2;
    final int d = c - getWidthBits() + 3;
    g.setColor(Color.MAGENTA);
    Stars.drawStar(g, c, c, d, 6, 1, theta);
    g.dispose();
    write(tilePosition, i);
  }

  private void makeHeptagram(final int tilePosition, final double theta) throws IOException {
    final BufferedImage i = new BufferedImage(getWidth(), getWidth(), BufferedImage.TYPE_INT_RGB);
    final Graphics g = i.getGraphics();
    final int c = getWidth() / 2;
    final int d = c - getWidthBits() + 3;
    g.setColor(Color.GREEN);
    Stars.drawStar(g, c, c, d, 7, 3, theta);
    g.dispose();
    write(tilePosition, i);
  }

  private void makeStandardWall(final int tilePosition, final int rows, final int cols) throws IOException {
    final BufferedImage i = new BufferedImage(getWidth(), getWidth(), BufferedImage.TYPE_INT_RGB);
    final Graphics g = i.getGraphics();
    Walls.drawWall(g, 0, 0, getWidth(), rows, cols, STANDARD_WALL_BRICK_COLOURS, STANDARD_WALL_MORTAR_COLOURS);
    g.dispose();
    write(tilePosition, i);
  }

  private void makeWeakWall(final int tilePosition, final int rows, final int cols) throws IOException {
    final BufferedImage i = new BufferedImage(getWidth(), getWidth(), BufferedImage.TYPE_INT_RGB);
    final Graphics g = i.getGraphics();
    Walls.drawWall(g, 0, 0, getWidth(), rows, cols, WEAK_WALL_BRICK_COLOURS, WEAK_WALL_MORTAR_COLOURS);
    g.dispose();
    write(tilePosition, i);
  }

  private void makeStrongWall(final int tilePosition, final int rows, final int cols) throws IOException {
    final BufferedImage i = new BufferedImage(getWidth(), getWidth(), BufferedImage.TYPE_INT_RGB);
    final Graphics g = i.getGraphics();
    Walls.drawWall(g, 0, 0, getWidth(), rows, cols, STRONG_WALL_BRICK_COLOURS, STRONG_WALL_MORTAR_COLOURS);
    g.dispose();
    write(tilePosition, i);
  }

  private void makeBolter(final int tilePosition, final int source, final int quadrant) throws IOException {
    final TileSet ts = getTileSet();
    final BufferedImage i = BufferedImageUtils.copy(ts.getImage(source));
    final Graphics g = i.getGraphics();
    final int m = getWidth() / 2;
    final int d = getWidth() / 8;
    g.setColor(Color.CYAN);
    g.fillRect(m - d, m - d, 2 * d + 1, 2 * d + 1);
    g.setColor(Color.RED);
    final int x0, y0, x1, y1;
    switch (quadrant) {
    case 0:
      x0 = m - d;
      y0 = m - d;
      x1 = m + d + 1;
      y1 = m - d;
      break;
    case 1:
      x0 = m - d;
      y0 = m + d + 1;
      x1 = m - d;
      y1 = m - d;
      break;
    case 2:
      x0 = m + d + 1;
      y0 = m + d + 1;
      x1 = m - d;
      y1 = m + d + 1;
      break;
    default:
      x0 = m + d + 1;
      y0 = m - d;
      x1 = m + d + 1;
      y1 = m + d + 1;
      break;
    }
    final int[] x = {m, x0, x1};
    final int[] y = {m, y0, y1};
    g.fillPolygon(x, y, x.length);
    g.setColor(Color.BLUE);
    g.drawLine(m - d - 1, m - d, m + d, m + d + 1);
    g.drawLine(m - d - 1, m - d - 1, m + d + 1, m + d + 1);
    g.drawLine(m - d, m - d - 1, m + d + 1, m + d);
    g.drawLine(m - d - 1, m + d, m + d, m - d - 1);
    g.drawLine(m - d - 1, m + d + 1, m + d + 1, m - d - 1);
    g.drawLine(m - d, m + d + 1, m + d + 1, m - d);
    g.setColor(Color.RED);
    g.drawLine(m - d, m - d, m + d, m + d);
    g.drawLine(m - d, m + d, m + d, m - d);
    g.dispose();
    write(tilePosition, i);
  }

  private void makeTextTile(final int tilePosition, final String code, final String text, final int bg) throws IOException {
    final TextSpellTile t = new TextSpellTile(getWidthBits(), bg, code, 0xFFDD9800, 0xFFFFEE00, text, 0xFFFF0000);
    write(tilePosition, t.image());
  }

  private void makeTextTile(final int tilePosition, final String code, final String text) throws IOException {
    makeTextTile(tilePosition, code, text,  0xFF636363);
  }

  private void makePlainTile(final int tilePosition, final int color) throws IOException {
    final BasicSpellTile t = new BasicSpellTile(getWidthBits(), color);
    write(tilePosition, t.image());
  }

  private void makeUncertainty(final int tilePosition) throws IOException {
    final BufferedImage i = new BasicSpellTile(getWidthBits(), 0xFF636363).image();
    final int w = getWidth();
    final Graphics2D g = (Graphics2D) i.getGraphics();
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g.setColor(Color.MAGENTA);
    final int u = w / 4;
    final int v = w / 2 - w / 8;
    g.fillOval(u - 1, v - 1, u + 2, u + 2);
    g.setColor(Color.MAGENTA.darker());
    g.fillOval(u, v, u, u);
    g.setColor(Color.GREEN);
    g.drawArc(w / 8, w / 8, w / 2, 6 * w / 8, -40, 80);
    g.drawArc(w / 4, 0, w / 2, w, -50, 100);
    g.dispose();
    write(tilePosition, i);
  }

  private void makeGollop(final int tilePosition, final int c1, final int c2, final double degrees, final int radius) throws IOException {
    final BufferedImage i = new BufferedImage(getWidth(), getWidth(), BufferedImage.TYPE_INT_RGB);
    final Graphics2D g = (Graphics2D) i.getGraphics();
    try {
      final double theta = degrees * Math.PI / 180;
      final int delta = (getWidth() - radius) / 2;
      Gollop.drawGollopSphere(g, new Color(c1), new Color(c2), delta, delta, radius, theta);
      write(tilePosition, i);
    } finally {
      g.dispose();
    }
  }

  private void makeGollop(final int firstTilePosition) throws IOException {
    int p = firstTilePosition;
    for (int k = 0, t = 0; k < 10; ++k, t += 0x00080800) {
      for (int degrees = 0; degrees < 360; degrees += 30) {
        makeGollop(p++, 0xFF0060FF + t, 0xFF001080 + t, degrees, getWidth() - k * (getWidthBits() - 2));
      }
    }
  }

  private void makeAntiTile(final int destination, final int source) throws IOException {
    if (destination >= 0 && source >= 0) {
      final TileSet ts = getTileSet();
      ts.setImage(destination, AntiTile.antiTile(BufferedImageUtils.copy(ts.getImage(source))));
    }
  }

  private void makeGenerator(final int tilePosition, final char symbol, final int symbolColor, final int ringColor, final double theta) throws IOException {
    final int w = getWidth();
    final BufferedImage i = new BufferedImage(w, w, BufferedImage.TYPE_INT_RGB);
    final Graphics2D g = (Graphics2D) i.getGraphics();
    try {
      Portal.drawPortal(g, w, symbol, symbolColor, ringColor, theta);
    } finally {
      g.dispose();
    }
    write(tilePosition, i);
  }

  private void makeSubhyadicForceWall(final int tilePosition, final int bitCode) throws IOException {
    final int w = getWidth();
    final int b = getWidthBits() + 1;
    final int h = w / 2;
    final int d = b - 2;
    final Color dfg = FLUORO_GREEN.darker();
    final BufferedImage i = new BufferedImage(w, w, BufferedImage.TYPE_INT_RGB);
    final Graphics2D g = (Graphics2D) i.getGraphics();
    try {
      // Central portion
      g.setColor(dfg);
      g.fillRect(h - d / 2, h - d / 2, d, d);
      g.setColor(FLUORO_GREEN);
      g.drawRect(h - b / 2, h - b / 2, b, b);
      if ((bitCode & 1) != 0) {
        g.setColor(FLUORO_PINK);
        g.drawLine(h + b / 2, h - b / 2, w, h - b / 2);
        g.setColor(FLUORO_CYAN);
        g.drawLine(h + b / 2, h + b / 2, w, h + b / 2);
      }
      if ((bitCode & 2) != 0) {
        g.setColor(FLUORO_PINK);
        g.drawLine(h - b / 2, h - b / 2, 0, h - b / 2);
        g.setColor(FLUORO_CYAN);
        g.drawLine(h - b / 2, h + b / 2, 0, h + b / 2);
      }
      if ((bitCode & 4) != 0) {
        g.setColor(FLUORO_PINK);
        g.drawLine(h + b / 2, h - b / 2, h + b / 2, 0);
        g.setColor(FLUORO_CYAN);
        g.drawLine(h - b / 2, h - b / 2, h - b / 2, 0);
      }
      if ((bitCode & 8) != 0) {
        g.setColor(FLUORO_PINK);
        g.drawLine(h + b / 2, h - b / 2, h + b / 2, w);
        g.setColor(FLUORO_CYAN);
        g.drawLine(h - b / 2, h - b / 2, h - b / 2, w);
      }
    } finally {
      g.dispose();
    }
    write(tilePosition, i);
  }

  private void makeSubhyadicForceWalls(final int tilePosition) throws IOException {
    for (int k = 0; k < 16; ++k) {
      makeSubhyadicForceWall(tilePosition + k, k);
    }
  }

  /**
   * Generate graphics.
   * @throws IOException if an I/O error occurs.
   */
  public void generate() throws IOException {
    // These position codes are defined in active.txt files
    makeStandardWall(0x22B, 1, 1);
    for (int k = 0x324; k <= 0x32A; ++k) {
      makeStandardWall(k, 4, 3);
    }
    for (int k = 0x32B; k <= 0x332; ++k) {
      makeWeakWall(k, 6, 4);
    }
    for (int k = 0x333; k <= 0x337; ++k) {
      makeStrongWall(k, 2, 2);
    }

    makeBolter(0x55, 0x324, 0);
    makeBolter(0x56, 0x324, 1);
    makeBolter(0x57, 0x324, 2);
    makeBolter(0x58, 0x324, 3);

    makePentagram(0x2BF, 0);
    makePentagram(0x2F8, 6);
    makePentagram(0x2F9, 12);
    makePentagram(0x2FA, 18);
    makePentagram(0x2FB, 24);
    makePentagram(0x2FC, 30);
    makePentagram(0x2FD, 36);
    makePentagram(0x2FE, 42);
    makePentagram(0x2FF, 48);
    makePentagram(0x300, 54);
    makePentagram(0x301, 60);
    makePentagram(0x302, 66);
    makeHexagon(0x113, 0);
    makeHexagon(0x303, 5);
    makeHexagon(0x304, 10);
    makeHexagon(0x305, 15);
    makeHexagon(0x306, 20);
    makeHexagon(0x307, 25);
    makeHexagon(0x308, 30);
    makeHexagon(0x309, 35);
    makeHexagon(0x30A, 40);
    makeHexagon(0x30B, 45);
    makeHexagon(0x30C, 50);
    makeHexagon(0x30D, 55);
    makeHeptagram(0x2F2, 0);
    makeHeptagram(0x30E, 8.5714);
    makeHeptagram(0x30F, 17.1429);
    makeHeptagram(0x310, 25.7143);
    makeHeptagram(0x311, 34.2857);
    makeHeptagram(0x312, 42.8571);

    makeTextTile(0xA3, "1/2", "DISECTION");
    makeTextTile(0xA5, "II", "DOUBLE");
    makeTextTile(0xED, "F", "FREEZE");
    makeTextTile(0x146, "\u00D72", "HYPERCLONE", 0xFF1DEC43);
    makeTextTile(0x15A, "JK", "JOKER");
    makeTextTile(0x16B, "L+", "LEVEL");
    makeTextTile(0x272, "M", "MERGE");
    makeTextTile(0x18E, "GO!", "MOVE IT");
    makeTextTile(0x1BB, "100", "POINTS");
    makeTextTile(0x1CB, "Q", "QUICKSHOT");
    makeTextTile(0x1D8, "\u00D72", "REPLICATE");
    makeTextTile(0x1DA, "?!", "REQUEST");
    makeTextTile(0x20A, "\u00D72", "SIMULACRUM", 0xFF0000FF);
    makeTextTile(0x24C, "T", "TORMENT");
    makeTextTile(0x24E, "III", "TRIPLE");
    makeTextTile(0x443, "H", "HYADIC");

    makePlainTile(0x78, 0xFFBF8F6F);
    makePlainTile(0x9D, 0xFFCC0000);

    makeGollop(0x338);
    makeUncertainty(0x255);
    double theta = 0;
    for (int k = 0; k < 12; ++k, theta += Math.PI / 6) {
      makeGenerator(0x3C1 + k, 'G', 0xC00000, 0x0000C0, theta);
      makeGenerator(0x3CD + k, 'E', 0x0000C0, 0xC00000, theta);
    }

    makeAntiTile(0x19E, 0x10C); // Herbicide
    makeAntiTile(0x26D, 0x212); // Wake
    makeAntiTile(0x53, 0x1BD);  // Boil
    makeAntiTile(0x9C, 0x325);  // Destroy Wall
    makeAntiTile(0x1CA, 0xD4);  // Quench
    makeAntiTile(0x1ED, 0x15);  // Separation

    makeSubhyadicForceWalls(0x455);
  }

}

