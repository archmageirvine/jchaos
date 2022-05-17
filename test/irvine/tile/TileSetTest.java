package irvine.tile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import irvine.TestUtils;
import irvine.util.io.IOUtils;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class TileSetTest extends TestCase {

  public void test() throws IOException {
    final File dir = TestUtils.createTempDir("tile", "set");
    try {
      final TileSet ts = new TileSet(4, dir.getPath());
      final ExplodingCircleEffect ece = new ExplodingCircleEffect(16, 0xFFFF0000, 0xFF00FF00);
      ece.next();
      ece.next();
      final BufferedImage image = ece.next().toBufferedImage();
      assertEquals(16, image.getWidth());
      assertEquals(16, image.getHeight());
      ts.setImage(5, image);
      ts.flush();
      final TileSet ts2 = new TileSet(4, dir.getPath());
      final BufferedImage i = ts2.getImage(5);
      assertEquals(16, i.getWidth());
      assertEquals(16, i.getHeight());
      int red = 0;
      int green = 0;
      for (int y = 0; y < 16; ++y) {
        for (int x = 0; x < 16; ++x) {
          if (i.getRGB(x, y) == 0xFFFF0000) {
            ++red;
          } else if (i.getRGB(x, y) == 0xFF00FF00) {
            ++green;
          }
        }
      }
      assertEquals(235, red);
      assertEquals(21, green);
    } finally {
      assertTrue(IOUtils.deleteAll(dir));
    }
  }

}
