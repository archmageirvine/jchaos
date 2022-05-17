package chaos.graphics;

import java.awt.Graphics;
import java.util.Collection;
import java.util.Random;

import chaos.board.Cell;
import chaos.board.World;
import chaos.sound.Sound;
import chaos.util.BooleanLock;
import chaos.util.Sleep;

/**
 * Explosion using an image list.  Starting from a set of specified cells, the
 * explosion moves outwards to all immediately adjacent cells.
 *
 * @author Sean A. Irvine
 */
public class ImageListExplodeEffect extends AbstractEffect {

  private static final Random RANDOM = new Random();
  private static final int[] DX = {0, 0, 1, -1, 1, -1, 1, -1};
  private static final int[] DY = {1, -1, 0, 0, 1, -1, -1, 1};

  private final World mWorld;
  private final Animator mAnimator;
  private final ImageList mImages;
  private final String mSound;
  private final int mSoundStatus;
  private final int mWidth;

  ImageListExplodeEffect(final World world, final Animator animator, final ImageList images, final String sound, final int soundStatus) {
    mWorld = world;
    mAnimator = animator;
    mImages = images;
    mSound = sound;
    mSoundStatus = soundStatus;
    mWidth = images.get(0).getWidth();
  }

  private boolean onScreen(final int x, final int y, final int minX, final int minY, final int maxX, final int maxY) {
    return x >= minX && x < maxX && y >= minY && y < maxY;
  }

  @Override
  public void performEffect(final ChaosScreen screen, final Graphics graphics, final Collection<Cell> cells, final int width) {
    if (screen != null && cells != null) {
      // Current image number for each direction (initially random)
      final int[] imageNumber = new int[8];
      for (int k = 0; k < imageNumber.length; ++k) {
        imageNumber[k] = RANDOM.nextInt(mImages.size());
      }
      final int minX = screen.getXOffset();
      final int minY = screen.getYOffset();
      final int maxX = screen.getXOffset() + mWorld.width() * width - mWidth;
      final int maxY = screen.getYOffset() + mWorld.height() * width - mWidth;
      final Sound sound = Sound.getSoundEngine();
      final int[] pxy = new int[2];
      final BooleanLock status = mSound != null ? sound.play(mSound, mSoundStatus) : null;
      synchronized (screen.lock()) {
        for (int k = 0; k < mWidth; ++k) {
          for (final Cell c : cells) {
            final int cellNumber = c.getCellNumber();
            mWorld.getCellCoordinates(cellNumber, pxy);
            final int px = (pxy[0] * mWidth) + minX;
            final int py = (pxy[1] * mWidth) + minY;
            for (int dir = 0; dir < imageNumber.length; ++dir) {
              if (k != 0) {
                final int oldX = px + DX[dir] * (k - 1);
                final int oldY = py + DY[dir] * (k - 1);
                if (onScreen(oldX, oldY, minX, minY, maxX, maxY)) {
                  graphics.drawImage(mImages.getMask(), oldX, oldY, null);
                }
              }
              final int x = px + DX[dir] * k;
              final int y = py + DY[dir] * k;
              if (onScreen(x, y, minX, minY, maxX, maxY)) {
                graphics.drawImage(mImages.get(imageNumber[dir]), x, y, null);
              }
              imageNumber[dir]++;
              imageNumber[dir] %= mImages.size();
            }
            Sleep.sleep(15);
          }
        }
        // Redraw damaged cells.
        for (final Cell c : cells) {
          for (final Cell r : mWorld.getCells(c.getCellNumber(), 0, 1, false)) {
            mAnimator.drawCell(r.getCellNumber());
          }
        }
      }
      if (status != null) {
        sound.wait(status, 10000);
      }
    }
  }
}
