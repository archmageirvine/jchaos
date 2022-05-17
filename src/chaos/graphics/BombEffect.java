package chaos.graphics;

import java.awt.Graphics;
import java.util.Collection;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.sound.Sound;
import chaos.util.BooleanLock;
import chaos.util.ChaosProperties;
import chaos.util.Sleep;
import irvine.util.graphics.ParticleExplosion;

/**
 * Bomb explosion effect.
 *
 * @author Sean A. Irvine
 */
public class BombEffect extends AbstractEffect {

  private static final int DECAY = ChaosProperties.properties().getIntProperty("chaos.bomb.decay", 5);
  private final World mWorld;
  private final int mColor;
  private final Actor mSource;

  BombEffect(final World world, final int color, final Actor source) {
    mWorld = world;
    mColor = color;
    mSource = source;
  }

  /*
   * Perform an explosion effect centered on the specified cell.  The effect
   * also hits adjacent cells if possible, taking care not to draw of the
   * edge of the screen.
   */
  private void bomb(final ChaosScreen screen, final Graphics graphics, final Cell cell, final int width, final int color, final Actor source) {
    // Determine top left and lower right cells up to one cell away
    int tx = Integer.MAX_VALUE;
    int ty = Integer.MAX_VALUE;
    int bx = 0;
    int by = 0;
    final int[] pxy = new int[2];
    final Collection<Cell> targets = mWorld.getCells(cell.getCellNumber(), 0, 1, false);
    for (final Cell c : targets) {
      mWorld.getCellCoordinates(c.getCellNumber(), pxy);
      final int px = (pxy[0] * width) + screen.getXOffset();
      final int py = (pxy[1] * width) + screen.getYOffset();
      tx = Math.min(tx, px);
      ty = Math.min(ty, py);
      bx = Math.max(bx, px + width);
      by = Math.max(by, py + width);
    }
    if (tx != Integer.MAX_VALUE && ty != Integer.MAX_VALUE && bx != 0 && by != 0) {
      assert bx > tx;
      assert by > ty;
      final BooleanLock s = Sound.getSoundEngine().play("chaos/resources/sound/casting/Explode", SoundLevel.whatSoundLevel(source, targets));
      // Found valid area to draw in, set up and run the effect
      final ParticleExplosion pe = new ParticleExplosion(bx - tx, by - ty, DECAY, color);
      synchronized (screen.lock()) {
        while (pe.update()) {
          graphics.drawImage(pe.image().toBufferedImage(), tx, ty, null);
          Sleep.sleep(5);
        }
      }
      Sound.getSoundEngine().wait(s, 5000);
    }
  }

  @Override
  public void performEffect(final ChaosScreen screen, final Graphics graphics, final Collection<Cell> cells, final int width) {
    if (screen != null && cells != null) {
      // Sequentially do it in each cell
      for (final Cell c : cells) {
        if (c != null) {
          bomb(screen, graphics, c, width, mColor, mSource);
        }
      }
    }
  }
}
