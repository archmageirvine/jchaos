package chaos.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;

import javax.imageio.ImageIO;

import chaos.common.free.Horde;
import chaos.common.monster.NamedDeity;

/**
 * Extract explicit imagery for a name deity.
 * @author Sean A. Irvine
 */
public final class DumpDeityImages {

  private DumpDeityImages() { }

  // Example: for name in $(cat src/chaos/resources/names/deity | grep '^[A-Z]' | tr ' ' '_'); do java chaos.graphics.DumpDeityImages "${name/_/ }"; done
  /**
   * Extract explicit tiles for a named deity.
   * @param args name of deity
   * @exception IOException if an I/O error occurs
   */
  public static void main(final String[] args) throws IOException {
    final String name = args[0];
    final NamedDeity deity = new NamedDeity(name);
    final ActiveTileManager tileManager = new ActiveTileManager(5);
    //ImageIO.write(tileManager.getTile(new Spawner(), 0, 0), "png", new File("spawn.png"));
    ImageIO.write(tileManager.getTile(new Horde(), 0, 0, 0), "png", new File("horde.png"));
    final LinkedHashSet<BufferedImage> tiles = new LinkedHashSet<>();
    while (tiles.add(tileManager.getTile(deity, 0, 0, 0))) {
      // do nothing
    }
    final String out = name.replace(" ", "");
    int k = 0;
    for (final BufferedImage image : tiles) {
      ImageIO.write(image, "png", new File(out + k++ + ".png"));
    }
  }
}
