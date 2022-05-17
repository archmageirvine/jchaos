package chaos.graphics.active16;

import chaos.graphics.AbstractGenerateGraphics;
import irvine.tile.ImageUtils;
import irvine.tile.TileImage;
import irvine.tile.TileSet;

import javax.imageio.ImageIO;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Generate shields for attribute increase spells.
 *
 * @author Sean A. Irvine
 */
public class GenerateGraphics extends AbstractGenerateGraphics {

  @Override
  protected int getWidthBits() {
    return 4;
  }

  private static final String SHIELD_DATA =
      "~~++++++++++++~."
    + "~.+##########+.%"
    + "~.+##########+.%"
    + "~.+##########+.%"
    + "~.+##########+.%"
    + "~.+##########+.%"
    + "~.+##########+.%"
    + "~.+##########+.%"
    + "~..+########+..%"
    + "~..+########+..%"
    + "~..+########+..%"
    + "~...+######+...%"
    + "~...+######+...%"
    + "~....+####+....%"
    + "~.....+##+.....%"
    + ".%%%%%%++%%%%%%%";

  private static final String CARTOUCHE_DATA =
      "~~~~~~++++~~~~~."
    + "~....+####+....%"
    + "~...+######+...%"
    + "~..+########+..%"
    + "~.+##########+.%"
    + "~.+##########+.%"
    + "~.+##########+.%"
    + "~.+##########+.%"
    + "~.+##########+.%"
    + "~.+##########+.%"
    + "~.+##########+.%"
    + "~.+##########+.%"
    + "~..+########+..%"
    + "~...+######+...%"
    + "~....+####+....%"
    + ".%%%%%++++%%%%%%";

  private static final String ELIXIR_DATA =
      "~~~~~~~~~~~~~~~."
    + "~..............%"
    + "~..............%"
    + "~....ww..ww....%"
    + "~.....w..w.....%"
    + "~.....w##w.....%"
    + "~.....w##w.....%"
    + "~.....w##w.....%"
    + "~.....w##w.....%"
    + "~.....w##w.....%"
    + "~.....w##w.....%"
    + "~.....w##w.....%"
    + "~.....w##w.....%"
    + "~......ww......%"
    + "~..............%"
    + ".%%%%%%%%%%%%%%%";

  private static final String HELMET_DATA =
      "~~~~~~~~~~~~~~~."
    + "~.....####.....%"
    + "~......++......%"
    + "~....+####+....%"
    + "~...+######+...%"
    + "~..+########+..%"
    + "~..+..####..+..%"
    + "~..#...##...#..%"
    + "~..#...##...#..%"
    + "~..#...##...#..%"
    + "~..#+..##..+#..%"
    + "~..##+....+##..%"
    + "~...#+....+#...%"
    + "~...#+....+#...%"
    + "~..............%"
    + ".%%%%%%%%%%%%%%%";

  private static final String WINGS_DATA =
      "~~~~~~~~~~~~~~~."
    + "~..............%"
    + "~.##...++...##.%"
    + "~####..++..####%"
    + "################"
    + "#...########...#"
    + "~....#.##.#....%"
    + "~...#..##..#...%"
    + "~......##......%"
    + "~.....####.....%"
    + "~.....#..#.....%"
    + "~....##..##....%"
    + "~....#....#....%"
    + "~....#....#....%"
    + "~..............%"
    + ".%%%%%%%%%%%%%%%";

  private static final String BOW_DATA =
      "~~~~~~~~~~~~~~~."
    + "~.......g......%"
    + "~......q#g.....%"
    + "~.....q..#g....%"
    + "~....q....#G...%"
    + "~...q.....##...%"
    + "~..q......##.+.%"
    + "~.++++++++##+++%"
    + "~..q......##.+.%"
    + "~...q.....##...%"
    + "~....q....#G...%"
    + "~.....q..#g....%"
    + "~......q#g.....%"
    + "~.......g......%"
    + "~..............%"
    + ".%%%%%%%%%%%%%%%";

  private static final String PLAIN_DATA =
      "~~~~~~~~~~~~~~~."
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + ".%%%%%%%%%%%%%%%";

  private static final String ELFBOOTS_DATA =
      "~~~~~~~~~~~~~~~."
    + "~..........y...%"
    + "~........##w...%"
    + "~........ywwwy.%"
    + "~........##w...%"
    + "~........##y...%"
    + "~........###...%"
    + "~........###...%"
    + "~........###...%"
    + "~........+##...%"
    + "~........+##...%"
    + "~...+###+##+...%"
    + "~..+#######+...%"
    + "~.+++++..++....%"
    + "~..............%"
    + ".%%%%%%%%%%%%%%%";

  private static final String CIRCLE_DATA =
      "~~~~~~~~~~~~~~~."
    + "~..............%"
    + "~..............%"
    + "~.....++++.....%"
    + "~....+####+....%"
    + "~...+######+...%"
    + "~..+########+..%"
    + "~..+########+..%"
    + "~..+########+..%"
    + "~..+########+..%"
    + "~...+######+...%"
    + "~....+####+....%"
    + "~.....++++.....%"
    + "~..............%"
    + "~..............%"
    + ".%%%%%%%%%%%%%%%";

  static int bright(final int bg) {
    final int b = bg & 0xFF;
    final int g = (bg >> 8) & 0xFF;
    final int r = (bg >> 16) & 0xFF;
    return bg
      + (((256 - r) / 4) << 16)
      + (((256 - g) / 4) << 8)
      + (256 - b) / 4;
  }

  static int dim(final int bg) {
    final int b = bg & 0xFF;
    final int g = (bg >> 8) & 0xFF;
    final int r = (bg >> 16) & 0xFF;
    return bg
      - ((r / 4) << 16)
      - ((g / 4) << 8)
      - b / 4;
  }

  /** Get the shield image in the specified colors. */
  private static TileImage getShield(final int fg, final int bg, final int edge) {
    final int b = bright(bg);
    final int d = dim(bg);
    final TileImage i = new TileImage(16, 16);
    for (int c = 0, y = 0; y < 16; ++y) {
      for (int x = 0; x < 16; ++x) {
        final int color;
        switch (SHIELD_DATA.charAt(c++)) {
        case '#':
          color = fg;
          break;
        case '+':
          color = edge;
          break;
        case '~':
          color = b;
          break;
        case '%':
          color = d;
          break;
        default:
          color = bg;
          break;
        }
        i.setPixel(x, y, color);
      }
    }
    return i.textureColor(fg, 3);
  }

  /** Get the circle image in the specified colors. */
  private static TileImage getCartouche(final int fg, final int bg, final int edge) {
    final int b = bright(bg);
    final int d = dim(bg);
    final TileImage i = new TileImage(16, 16);
    for (int c = 0, y = 0; y < 16; ++y) {
      for (int x = 0; x < 16; ++x) {
        final int color;
        switch (CARTOUCHE_DATA.charAt(c++)) {
        case '#':
          color = fg;
          break;
        case '+':
          color = edge;
          break;
        case '~':
          color = b;
          break;
        case '%':
          color = d;
          break;
        default:
          color = bg;
          break;
        }
        i.setPixel(x, y, color);
      }
    }
    return i.textureColor(fg, 3);
  }

  /** Get the shield image in the specified colors. */
  private static TileImage getWizardSpecial(final String imageData, final int fg, final int bg, final int edge) {
    final int b = bright(bg);
    final int d = dim(bg);
    final TileImage i = new TileImage(16, 16);
    for (int c = 0, y = 0; y < 16; ++y) {
      for (int x = 0; x < 16; ++x) {
        final int color;
        switch (imageData.charAt(c++)) {
        case '#':
          color = fg;
          break;
        case '+':
          color = edge;
          break;
        case '~':
          color = b;
          break;
        case '%':
          color = d;
          break;
        case 'w':
          color = 0xFFFFFFFF;
          break;
        case 'q':
          color = 0xFFA0FFA0;
          break;
        case 'g':
          color = 0xFF408040;
          break;
        case 'G':
          color = 0xFF20C020;
          break;
        case 'y':
          color = 0xFFFFFF00;
          break;
        default:
          color = bg;
          break;
        }
        i.setPixel(x, y, color);
      }
    }
    return i.textureColor(fg, 3);
  }

  private static TileImage getImage(final String resource) {
    try {
      return new TileImage(ImageIO.read(GenerateGraphics.class.getClassLoader().getResourceAsStream(resource)));
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void writeImage(final String name, final TileImage image) throws IOException {
    try (final PrintStream ps = new PrintStream(new FileOutputStream(name))) {
      ImageUtils.writePPM(image, ps);
    }
  }

  private static void makeShield(final int fg, final int bg, final int edge, final String stat, final int tilePosition) throws IOException {
    final String res = "chaos/resources/icons/" + stat + ".png";
    final TileImage image = getShield(fg, bg, edge).over(4, 2, getImage(res));
    writeImage(stat + (edge == 0xFFFF0000 ? "q" : "") + ".ppm", image);
    if (tilePosition >= 0) {
      final TileSet ts = new TileSet(4, System.getProperty("user.home") + "/chaos/src/chaos/graphics/active16/");
      ts.setImage(tilePosition, image.toBufferedImage());
    }
  }

  private static void makeCartouche(final int fg, final int bg, final int edge, final String stat, final int tilePosition, final String suffix) throws IOException {
    final String res = "chaos/resources/icons/" + stat + ".png";
    final TileImage image = getCartouche(fg, bg, edge).over(4, 4, getImage(res));
    writeImage(stat + suffix + ".ppm", image);
    if (tilePosition >= 0) {
      final TileSet ts = new TileSet(4, System.getProperty("user.home") + "/chaos/src/chaos/graphics/active16/");
      ts.setImage(tilePosition, image.toBufferedImage());
    }
  }

  private static void makeCartouche(final int fg, final int bg, final int edge, final String stat, final int tilePosition) throws IOException {
    makeCartouche(fg, bg, edge, stat, tilePosition, "x");
  }

  private static void makeSpecial(final String imageData, final int fg, final int bg, final int edge, final String stat, final int tilePosition) throws IOException {
    final TileImage image = getWizardSpecial(imageData, fg, bg, edge);
    writeImage(stat + "w.ppm", image);
    if (tilePosition >= 0) {
      final TileSet ts = new TileSet(4, System.getProperty("user.home") + "/chaos/src/chaos/graphics/active16/");
      ts.setImage(tilePosition, image.toBufferedImage());
    }
  }

  private static void makePlain(final int bg, final String stat, final int tilePosition) throws IOException {
    final String res = "chaos/resources/icons/" + stat + ".png";
    final TileImage image = getWizardSpecial(PLAIN_DATA, 0, bg, 0).over(4, 4, getImage(res));
    writeImage(stat + "w.ppm", image);
    if (tilePosition >= 0) {
      final TileSet ts = new TileSet(4, System.getProperty("user.home") + "/chaos/src/chaos/graphics/active16/");
      ts.setImage(tilePosition, image.toBufferedImage());
    }
  }

  private static void makeCircleShield(final int bg, final int fg, final String stat, final int tilePosition) throws IOException {
    final String res = "chaos/resources/icons/" + stat + ".png";
    final TileImage image = getWizardSpecial(CIRCLE_DATA, fg, bg, ~0).over(4, 4, getImage(res));
    writeImage(stat + "w.ppm", image);
    if (tilePosition >= 0) {
      final TileSet ts = new TileSet(4, System.getProperty("user.home") + "/chaos/src/chaos/graphics/active16/");
      ts.setImage(tilePosition, image.toBufferedImage());
    }
  }

  /**
   * Algorithmically produce some tiles.
   *
   * @param args ignored
   * @exception Exception if an error occurs
   */
  public static void main(final String[] args) throws Exception {
    makeShield(0xFF60432E, 0xFF646464, 0xFFC0865C, "life", 0x24);
    makeShield(0xFF60432E, 0xFF646464, 0xFFC0865C, "agility", 0x2AB);
    makeShield(0xFF60432E, 0xFF646464, 0xFFC0865C, "intelligence", 0x59);
    makeShield(0xFF60432E, 0xFF646464, 0xFFC0865C, "magicalresistance", 0x179);
    makeShield(0xFF60432E, 0xFF646464, 0xFFC0865C, "combat", 0x79);
    makeShield(0xFF60432E, 0xFF646464, 0xFFC0865C, "rangedcombat", 0x1D0);
    makeShield(0xFF60432E, 0xFF646464, 0xFFC0865C, "specialcombat", 0x209);
    makeShield(0xFF60432E, 0xFF646464, 0xFFC0865C, "recovery", 0x1D1);
    makeShield(0xFF60432E, 0xFF646464, 0xFFC0865C, "movement", 0x220);

    makeCartouche(0xFF60432E, 0xFF646464, 0xFFC0865C, "life", 0x4A);
    makeCartouche(0xFF60432E, 0xFF646464, 0xFFC0865C, "agility", 0x7A);
    makeCartouche(0xFF60432E, 0xFF646464, 0xFFC0865C, "intelligence", 0x2AA);
    makeCartouche(0xFF60432E, 0xFF646464, 0xFFC0865C, "magicalresistance", 0x2AC);
    makeCartouche(0xFF60432E, 0xFF646464, 0xFFC0865C, "combat", 0x2AD);
    makeCartouche(0xFF60432E, 0xFF646464, 0xFFC0865C, "rangedcombat", 0x2AE);
    makeCartouche(0xFF60432E, 0xFF646464, 0xFFC0865C, "specialcombat", 0x2AF);
    makeCartouche(0xFF60432E, 0xFF646464, 0xFFC0865C, "recovery", 0x264);
    makeCartouche(0xFF60432E, 0xFF646464, 0xFFC0865C, "movement", 0x12F);

    makePlain(0xFF646464, "combat", 0x178);
    makeCircleShield(0xFF646464, 0xFF000000, "magicalresistance", 0x2B1);
    makeCircleShield(0xFF646464, 0xFF000000, "specialcombat", 0x2B2);
    makeCircleShield(0xFF646464, 0xFF0000FF, "intelligence", 0x2B3);
    makeSpecial(ELIXIR_DATA, 0xFFFF0000, 0xFF646464, 0, "recovery", 0x1EC);
    makeSpecial(HELMET_DATA, 0xFFA00000, 0xFF646464, 0xFFFF0000, "life", 0x2B0);
    makeSpecial(WINGS_DATA, 0xFF0000FF, 0xFF646464, 0xFFECC089, "movement", 0x298);
    makeSpecial(BOW_DATA, 0xFF00FF00, 0xFF646464, 0xFF000000, "rangedcombat", 0x175);
    makeSpecial(ELFBOOTS_DATA, 0xFF00FFFF, 0xFF646464, 0xFF00A0A0, "agility", 0xC2);

    makeShield(0xFF60432E, 0xFF640000, 0xFFFF0000, "life", 0x173);
    makeShield(0xFF60432E, 0xFF640000, 0xFFFF0000, "recovery", 0x2E7);
    makeShield(0xFF60432E, 0xFF640000, 0xFFFF0000, "movement", 0x213);
    makeShield(0xFF60432E, 0xFF640000, 0xFFFF0000, "intelligence", 0x5A);
    makeShield(0xFF60432E, 0xFF640000, 0xFFFF0000, "agility", 0x2E9);
    makeShield(0xFF60432E, 0xFF640000, 0xFFFF0000, "magicalresistance", 0x174);
    makeShield(0xFF60432E, 0xFF640000, 0xFFFF0000, "combat", 0x96);
    makeShield(0xFF60432E, 0xFF640000, 0xFFFF0000, "rangedcombat", 0x2F1);
    makeShield(0xFF60432E, 0xFF640000, 0xFFFF0000, "specialcombat", 0x2EE);

    makeCartouche(0xFF60432E, 0xFF640000, 0xFFFF0000, "life", 0x86, "z");
    makeCartouche(0xFF60432E, 0xFF640000, 0xFFFF0000, "recovery", 0x43, "z");
    makeCartouche(0xFF60432E, 0xFF640000, 0xFFFF0000, "movement", 0xA9, "z");
    makeCartouche(0xFF60432E, 0xFF640000, 0xFFFF0000, "intelligence", 0x2E8, "z");
    makeCartouche(0xFF60432E, 0xFF640000, 0xFFFF0000, "agility", 0x2EA, "z");
    makeCartouche(0xFF60432E, 0xFF640000, 0xFFFF0000, "magicalresistance", 0x2EB, "z");
    makeCartouche(0xFF60432E, 0xFF640000, 0xFFFF0000, "combat", 0x2ED, "z");
    makeCartouche(0xFF60432E, 0xFF640000, 0xFFFF0000, "rangedcombat", 0x2F0, "z");
    makeCartouche(0xFF60432E, 0xFF640000, 0xFFFF0000, "specialcombat", 0x2EF, "z");

    new GenerateGraphics().generate();
  }

  /*
  private static final String PLAIN_DATA =
      "~~~~~~~~~~~~~~~."
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + ".%%%%%%%%%%%%%%%";
  */

}

