package chaos.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.sound.Sound;
import chaos.util.BooleanLock;
import chaos.util.ChaosProperties;
import chaos.util.Sleep;
import irvine.tile.ExplosionEffect;
import irvine.tile.TileImage;

/**
 * Meditation collapse effect.
 *
 * @author Sean A. Irvine
 */
public class CollapseEffect extends AbstractEffect {

  private static final String COLLAPSE = "chaos/resources/sound/misc/collapse";
  private static final int SLEEP = ChaosProperties.properties().getIntProperty("chaos.collapse.sleep", 30);

  private final World mWorld;
  private final TileManager mTM;
  private final Actor mCause;

  CollapseEffect(final World world, final TileManager tm, final Actor cause) {
    mWorld = world;
    mTM = tm;
    mCause = cause;
  }

  @Override
  public void performEffect(final ChaosScreen screen, final Graphics graphics, final Collection<Cell> cells, final int width) {
    if (screen != null && cells != null) {
      // Compute all the effects needed
      final int[] xy = new int[2];
      final HashMap<Cell, ExplosionEffect> effects = new HashMap<>();
      boolean ok = false;
      for (final Cell ce : cells) {
        final Actor a = ce.peek();
        if (a != null) {
          final BufferedImage bi = mTM.getSpellTile(a);
          effects.put(ce, new ExplosionEffect(new TileImage(bi), 0, true));
          ok = true;
        }
      }
      // Avoid this time delayed loop if there is nothing to do.
      if (ok) {
        final Sound s = Sound.getSoundEngine();
        final int soundLevel = SoundLevel.whatSoundLevel(mCause, cells.iterator().next().peek());
        final BooleanLock status = s.play(COLLAPSE, soundLevel);
        synchronized (screen.lock()) {
          do {
            ok = false;
            for (final Map.Entry<Cell, ExplosionEffect> e : effects.entrySet()) {
              final TileImage im = e.getValue().next();
              if (im != null) {
                ok = true;
                mWorld.getCellCoordinates(e.getKey().getCellNumber(), xy);
                screen.drawCell(im.toBufferedImage(), xy[0], xy[1]);
              }
            }
            Sleep.sleep(SLEEP);
          } while (ok);
        }
        s.wait(status, 5000);
      }
    }
  }
}
