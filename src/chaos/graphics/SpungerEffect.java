package chaos.graphics;

import java.awt.Graphics;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.sound.Sound;
import chaos.util.Sleep;
import irvine.tile.TileImage;

/**
 * Superclass for effects.
 *
 * @author Sean A. Irvine
 */
public class SpungerEffect extends AbstractEffect {

  private static final Random RANDOM = new Random();

  private final TileManager mTM;
  private final World mWorld;
  private final int mSoundLevel;

  SpungerEffect(final TileManager tm, final World world, final int soundLevel) {
    mTM = tm;
    mWorld = world;
    mSoundLevel = soundLevel;
  }

  /* Randomly change up to px non-black pixels to another colour. */
  static void randomBump(final TileImage im, final int px) {
    final int w = im.getWidth();
    final int h = im.getHeight();
    for (int k = 0; k < px; ++k) {
      for (int i = 0; i < 20; ++i) {
        final int x = RANDOM.nextInt(w);
        final int y = RANDOM.nextInt(h);
        final int p = im.getPixel(x, y);
        if ((p & 0xFFFFFF) != 0) {
          im.setPixel(x, y, (p & 0xFF000000) | RANDOM.nextInt(0xFFFFFF));
          break;
        }
      }
    }
  }

  /** Thread to play sponger sound effect. */
  private static class SpungerSoundThread extends Thread {

    private final int mSoundLevel;

    SpungerSoundThread(final int soundLevel) {
      super();
      mSoundLevel = soundLevel;
      start();
    }

    @Override
    public void run() {
      final Sound s = Sound.getSoundEngine();
      s.startSynthetic();
      try {
        for (int i = 0; i < 3; ++i) {
          for (int j = 0; j < 1000; j += 10) {
            s.playSynthetic(2000 - j, 1.0, 4, mSoundLevel);
            s.playSynthetic(1500 + j, 1.0, 2, mSoundLevel);
            s.playSynthetic(2000 - j, 1.0, 4, mSoundLevel);
          }
          for (int j = 0; j < 1000; j += 10) {
            s.playSynthetic(1000 + j, 1.0, 4, mSoundLevel);
            s.playSynthetic(2500 - j, 1.0, 2, mSoundLevel);
            s.playSynthetic(1000 + j, 1.0, 4, mSoundLevel);
          }
        }
      } finally {
        s.stopSynthetic();
      }
    }
  }

  private Thread mSoundThread = null;

  private void startSound() {
    if (mSoundLevel <= Sound.getSoundEngine().getSoundLevel()) {
      mSoundThread = new SpungerSoundThread(mSoundLevel);
    }
  }

  private void stopSound() {
    if (mSoundThread != null) {
      try {
        mSoundThread.join();
      } catch (final InterruptedException e) {
        // too bad
      }
    }
  }

  private boolean isSoundActive() {
    return mSoundThread != null && mSoundThread.isAlive();
  }

  @Override
  public void performEffect(final ChaosScreen screen, final Graphics graphics, final Collection<Cell> cells, final int width) {
    if (screen != null && cells != null) {
      // Compute initial images and coordinates
      final HashMap<Cell, TileImage> images = new HashMap<>();
      for (final Cell c : cells) {
        final Actor a = c.peek();
        if (a != null) {
          images.put(c, new TileImage(mTM.getTile(a, 0, 0, 0)));
        }
      }

      final int[] xy = new int[2];
      synchronized (screen.lock()) {
        startSound();

        // Loop for longer of fixed time or while sound is playing
        for (int k = 0; k < 30 || isSoundActive(); ++k) {
          for (final Cell c : cells) {
            final TileImage i = images.get(c);
            if (i != null) {
              randomBump(i, 5);
              mWorld.getCellCoordinates(c.getCellNumber(), xy);
              screen.drawCell(i.toBufferedImage(), xy[0], xy[1]);
            }
          }
          Sleep.sleep(isSoundActive() ? 50 : 10);
        }

        // Redraw cells
        for (final Cell c : cells) {
          final TileImage i = images.get(c);
          if (i != null) {
            mWorld.getCellCoordinates(c.getCellNumber(), xy);
            screen.drawCell(mTM.getTile(c.peek(), xy[0], xy[1], 0), xy[0], xy[1]);
          }
        }
        stopSound();
      }
    }
  }

}
