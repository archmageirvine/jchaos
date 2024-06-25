package irvine.tile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Generate all the imagery needed by the documentation.
 * @author Sean A. Irvine
 */
public final class GenerateEPSImages {

  private GenerateEPSImages() {
  }

  /** A test image used for demonstration purposes. */
  private static final String RED_DRAGON_DATA =
    "  ##            "
      + "    ##          "
      + "     ##   #     "
      + "    ###   ###   "
      + "  ####    ##### "
      + "    ##   ####   "
      + "    ### ##    # "
      + "   ######     # "
      + "  #   ####    # "
      + " #   # ####    #"
      + "    #   ####   #"
      + "    #   #####  #"
      + "         #### # "
      + "         #### # "
      + "          ### # "
      + "       #######  ";

  /** Get the dragon image in the specified colors. */
  private static TileImage getDragon(final int fg, final int bg) {
    final TileImage i = new TileImage(16, 16);
    for (int c = 0, y = 0; y < 16; ++y) {
      for (int x = 0; x < 16; ++x) {
        i.setPixel(x, y, RED_DRAGON_DATA.charAt(c++) == '#' ? fg : bg);
      }
    }
    return i;
  }

  /** Get dual thickness square. */
  private static TileImage getSquare(final int fg, final int bg) {
    final TileImage i = new TileImage(16, 16);
    i.fill(bg);
    for (int x = 0; x < 16; ++x) {
      i.setPixel(x, 0, fg);
      i.setPixel(x, 1, fg);
      i.setPixel(x, 14, fg);
      i.setPixel(x, 15, fg);
      i.setPixel(0, x, fg);
      i.setPixel(1, x, fg);
      i.setPixel(14, x, fg);
      i.setPixel(15, x, fg);
    }
    return i;
  }

  private static TileImage getCircle(final int fg, final int bg, final int w) {
    final TileEffect i = new ExplodingCircleEffect(w, bg, fg);
    i.next();
    i.next();
    i.next();
    i.next();
    i.next();
    i.next();
    return i.next();
  }

  private static void writeImage(final String name, final TileImage image) throws IOException {
    try (final PrintStream ps = new PrintStream(new FileOutputStream(name))) {
      ImageUtils.writeEPS(image, ps, true);
    }
  }

  private static void writeImageNoGrid(final String name, final TileImage image) throws IOException {
    try (final PrintStream ps = new PrintStream(new FileOutputStream(name))) {
      ImageUtils.writeEPS(image, ps, false);
    }
  }

  static void ppmToEPS(final String source, final String dest) throws IOException {
    // don't die if we can read it
    TileImage i = null;
    try {
      try (final FileInputStream is = new FileInputStream(source)) {
        i = ImageUtils.readPPM(is);
      }
    } catch (final IOException e) {
      System.err.println("Problem with: " + source);
    }
    if (i != null) {
      writeImage(dest, i);
    }
  }

  static void ppmToGridlessEPS(final String source, final String dest) throws IOException {
    // don't die if we can read it
    TileImage i = null;
    try {
      try (final FileInputStream is = new FileInputStream(source)) {
        i = ImageUtils.readPPM(is);
      }
    } catch (final IOException e) {
      System.err.println("Problem with: " + source);
    }
    if (i != null) {
      writeImageNoGrid(dest, i);
    }
  }

  private static void dumpVectorField(final int w, final PrintStream ps) {
    ps.println("\\tabcolsep=0mm");
    ps.print("\\begin{tabular}{c|");
    for (int x = 0; x < w; ++x) {
      ps.print("c|");
    }
    ps.println('}');
    ps.print("\\multicolumn{1}{c}{}");
    for (int x = 0; x < w; ++x) {
      if (x == 0) {
        ps.print("&\\multicolumn{1}{c}{\\," + x + "\\,}");
      } else {
        ps.print("&\\multicolumn{1}{c}{" + x + "}");
      }
    }
    ps.println("\\\\\\cline{2-" + (w + 1) + "}");
    final TileImage r = ExplosionEffect.getVectorField(w);
    for (int y = 0; y < w; ++y) {
      ps.print(y + "&");
      for (int x = 0; x < w; ++x) {
        final int p = r.getPixel(x, y);
        if (p != -1) {
          final String arrow;
          final int nx = p & 0xFFFF;
          final int ny = p >>> 16;
          if (nx == x) {
            arrow = ny == y - 1 ? "up" : "down";
          } else if (ny == y) {
            arrow = nx == x - 1 ? "left" : "right";
          } else {
            arrow = (ny == y - 1 ? "n" : "s") + (nx == x - 1 ? "w" : "e");
          }
          ps.print("$\\" + arrow + "arrow$");
        } else {
          ps.print("$\\circ$");
        }
        ps.print(x == w - 1 ? "\\\\" : "&");
      }
      ps.println("\\cline{2-" + (w + 1) + "}");
    }
    ps.println("\\end{tabular}");
  }

  private static void writeNumerals() throws IOException {
    for (int j = 8; j <= 32; j <<= 1) {
      for (int k = 0; k < 10; ++k) {
        writeImage("numeral" + j + "_" + k + ".eps", ImageUtils.getCharImage((char) (k + '0'), j));
      }
    }
    for (int k = 0; k < 10; ++k) {
      writeImage("numeral6_" + k + ".eps", ImageUtils.getCharImage((char) (k + '0'), 6));
    }
    for (int k = 0; k < 10; ++k) {
      writeImage("numeral9_" + k + ".eps", ImageUtils.getCharImage((char) (k + '0'), 9));
    }
    for (int k = 0; k < 10; ++k) {
      writeImage("numeral10_" + k + ".eps", ImageUtils.getCharImage((char) (k + '0'), 10));
    }
    for (int k = 0; k < 10; ++k) {
      writeImage("numeral11_" + k + ".eps", ImageUtils.getCharImage((char) (k + '0'), 11));
    }
    for (int k = 0; k < 10; ++k) {
      writeImage("numeral12_" + k + ".eps", ImageUtils.getCharImage((char) (k + '0'), 12));
    }
    for (int k = 0; k < 10; ++k) {
      writeImage("numeral14_" + k + ".eps", ImageUtils.getCharImage((char) (k + '0'), 14));
    }
    for (int k = 0; k < 10; ++k) {
      writeImage("numeral24_" + k + ".eps", ImageUtils.getCharImage((char) (k + '0'), 24));
    }
  }

  private static void writeTextures() throws IOException {
    final TileImage white = new TileImage(16, 16);
    white.fill(~0);
    for (int j = 3; j <= 8; ++j) {
      for (int i = 0; i < 4; ++i) {
        writeImage("texture" + j + "_" + i + ".eps", white.copy().textureColor(~0, j));
      }
    }
  }

  private static void writeVectorField() throws IOException {
    // a piece of TeX code for the vector field
    try (PrintStream ps = new PrintStream(new FileOutputStream("vf.tex"))) {
      dumpVectorField(32, ps);
    }
  }

  private static void writeSeriesNoGrid(final String prefix, final TileEffect ef) throws IOException {
    int c = 0;
    TileImage image;
    while ((image = ef.next()) != null) {
      writeImageNoGrid(prefix + c++ + ".eps", image);
    }
  }

  private static void writeSeries(final String prefix, final TileEffect ef, int c) throws IOException {
    TileImage image;
    while ((image = ef.next()) != null) {
      writeImage(prefix + c++ + ".eps", image);
    }
  }

  private static void writeSeries(final String prefix, final TileEffect ef) throws IOException {
    writeSeries(prefix, ef, 0);
  }

  private static void lena() throws IOException {
    try (final FileInputStream fis = new FileInputStream("../lena.ppm")) {
      final TileImage lena = ImageUtils.readPPM(fis);
      //      writeImageNoGrid("lena.eps", lena);
      try (final FileOutputStream fos = new FileOutputStream("lena1.ppm")) {
        ImageUtils.writePPM(lena.rotate(23.439, ~0, false), fos);
      }
      try (final FileOutputStream fos = new FileOutputStream("lena2.ppm")) {
        ImageUtils.writePPM(lena.rotate(23.439, ~0, true), fos);
      }
    }
  }

  /**
   * Generate some images used in documentation.
   * @param args ignored
   * @throws Exception if an error occurs
   */
  public static void main(final String[] args) throws Exception {
    writeNumerals();
    writeTextures();
    writeVectorField();
    lena();
    // process the pnm rotated images
    ppmToEPS("../circle15.ppm", "pnmrotate15.eps");
    ppmToEPS("../circle30.ppm", "pnmrotate30.eps");
    ppmToEPS("../circle45.ppm", "pnmrotate45.eps");
    ppmToEPS("../circle60.ppm", "pnmrotate60.eps");
    ppmToEPS("../circlea15.ppm", "pnmrotatea15.eps");
    ppmToEPS("../circlea30.ppm", "pnmrotatea30.eps");
    ppmToEPS("../circlea45.ppm", "pnmrotatea45.eps");
    ppmToEPS("../circlea60.ppm", "pnmrotatea60.eps");
    writeSeries("explodingcircle", new ExplodingCircleEffect(16, 0xFFFFFF, 0x000000));
    writeSeries("explodingsquare", new ExplodingSquareEffect(16, 0xFFFFFF, new int[] {0x000000, 0x3F3F3F, 0x7F7F7F}));
    writeSeries("twirl", new TwirlEffect(16, 0xFFFFFF, 0x000000, 2, 30.0));
    writeImage("rotatecircle0.eps", getCircle(0, ~0, 15));
    writeSeries("rotatecircle", new RotationEffect(getCircle(0, ~0, 15), 15, 4, ~0, false), 1);
    writeSeries("rotatecirclea", new RotationEffect(getCircle(0, ~0, 15), 15, 4, ~0, true), 1);
    writeSeries("rotatesq", new RotationEffect(getSquare(0, ~0), 15, 6, ~0, false));
    writeSeries("rotatesqa", new RotationEffect(getSquare(0, ~0), 15, 6, ~0, true));
    writeSeriesNoGrid("rotate", new RotationEffect(getDragon(0, ~0), -15, 24, ~0, false));
    writeSeriesNoGrid("rotatea", new RotationEffect(getDragon(0, ~0), -15, 24, ~0, true));
    writeImageNoGrid("dragon.eps", getDragon(0, ~0));
    writeImage("circle16.eps", getCircle(0, ~0, 16));
    writeSeriesNoGrid("exdragon", new ExplosionEffect(getDragon(0, ~0), ~0, false));
    writeSeriesNoGrid("exdragond", new ExplosionEffect(getDragon(0, ~0), ~0, true));
    writeSeries("excircle", new ExplosionEffect(getCircle(0, ~0, 16), ~0, false));
    writeSeries("excircled", new ExplosionEffect(getCircle(0, ~0, 16), ~0, true));
    writeSeries("fade", new FadeEffect(getCircle(0, ~0, 16), getDragon(0x77777777, ~0)));
    writeSeries("ufade", new FadeEffect(getDragon(0, ~0), 0x77777777));
    writeSeries("pfade", new PixelFadeEffect(getCircle(0, ~0, 16), getDragon(0x77777777, ~0), 8));
    writeSeriesNoGrid("twinkle", new TwinkleEffect(32, 0, ~0, 60));
    writeSeries("roll", new HorizontalRollEffect(getDragon(0, ~0), true));
    writeSeries("portalopen", new PortalOpenEffect(getDragon(0xFF0000, 0x8000)));
    writeSeries("attack", new AttackEffect(16, "42", ~0, 0xFFFF0000, 0xFF000000, 0xFF802020, 4));
    writeSeriesNoGrid("attackl", new AttackEffect(32, "42", ~0, 0xFFFF0000, 0xFF000000, 0xFF802020, 4));
    writeSeriesNoGrid("attackb", new AttackEffect(64, "42", ~0, 0xFFFF0000, 0xFF000000, 0xFF802020, 4));
  }
}
