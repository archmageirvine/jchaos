package chaos.graphics.active16;

import irvine.tile.TileImage;
import irvine.tile.TileSet;

import java.io.IOException;

/**
 * Generate shields for attribute increase spells.
 *
 * @author Sean A. Irvine
 */
public final class GenerateOwnerChangers {

  private GenerateOwnerChangers() { }

  private static final String SUBVERSION_DATA =
      "~~~~~~~~~~~~~~~."
    + "~....#.##.#....%"
    + "~.....####.....%"
    + "~......##......%"
    + "~.....++++.....%"
    + "~...+++//+++...%"
    + "~..++//XX//++..%"
    + "~.++f##ff##f++.%"
    + "~.++ff#ff#ff++.%"
    + "~.++ffffffff++.%"
    + "~.++fffrrfff++.%"
    + "~..++fr//rf++..%"
    + "~...+++//+++...%"
    + "~.....++++.....%"
    + "~..............%"
    + ".%%%%%%%%%%%%%%%";

  private static final String KEY_DATA =
      "~~~~~~~~~~~~~~~."
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~.$##$.........%"
    + "~$#$$#$$$$$$$$$%"
    + "~#$..$#########%"
    + "~#$..$#$$$#$##.%"
    + "~$#$$#$.....##.%"
    + "~.$##$......$$.%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + "~..............%"
    + ".%%%%%%%%%%%%%%%";

  static int average(final int x, final int y) {
    final int b = ((x & 0xFF) + (y & 0xFF)) / 2;
    final int g = (((x >> 8) & 0xFF) + ((y >> 8) & 0xFF)) / 2;
    final int r = (((x >> 16) & 0xFF) + ((y >> 16) & 0xFF)) / 2;
    final int a = (((x >> 24) & 0xFF) + ((y >> 24) & 0xFF)) / 2;
    return (a << 24) + (r << 16) + (g << 8) + b;
  }

  /** Get the shield image in the specified colors. */
  private static TileImage getImage(final String imageData, final int fg, final int bg, final int face) {
    final TileImage i = new TileImage(16, 16);
    final int b = GenerateGraphics.bright(bg);
    final int d = GenerateGraphics.dim(bg);
    final int a = average(bg, fg);
    for (int c = 0, y = 0; y < 16; ++y) {
      for (int x = 0; x < 16; ++x) {
        final int color;
        switch (imageData.charAt(c++)) {
        case '#':
          color = fg;
          break;
        case '~':
          color = b;
          break;
        case '%':
          color = d;
          break;
        case '$':
          color = a;
          break;
        case '+':
          color = 0xFFA90000;
          break;
        case 'r':
          color = 0xFFCC0000;
          break;
        case 'f':
          color = face;
          break;
        case 'b':
          color = 0xFF2132BA;
          break;
        case 'X':
          color = 0xFF000000;
          break;
        case '/':
          color = face | 0xFF454545;
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

  private static void makeSpecial(final String imageData, final int fg, final int bg, final int face, final int tilePosition) throws IOException {
    final TileImage image = getImage(imageData, fg, bg, face);
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
    makeSpecial(SUBVERSION_DATA, 0xFF0000FF, 0xFF646464, 0xFF905040, 0x235);
    makeSpecial(SUBVERSION_DATA, 0xFF0000FF, 0xFFAF6F00, 0xFF905040, 0x4);
    makeSpecial(SUBVERSION_DATA, 0xFF0000FF, 0xFF1DEC43, 0xFF905040, 0x6E);
    makeSpecial(SUBVERSION_DATA, 0xFF0000FF, 0xFF106010, 0xFF905040, 0x42);
    makeSpecial(KEY_DATA, 0xFFEEBA11, 0xFF646464, 0xFF905040, 0xEB);
    makeSpecial(KEY_DATA, 0xFFEEBA11, 0xFF1DEC43, 0xFF905040, 0xEC);
  }

}

