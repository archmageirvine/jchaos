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
import irvine.tile.RotationEffect;
import irvine.tile.TileImage;

/**
 * Change of owner effect.
 * @author Sean A. Irvine
 */
public class OwnerChangeEffect extends AbstractEffect {

  private static final int STEPS = ChaosProperties.properties().getIntProperty("chaos.ownerchange.steps", 12);
  private static final int SLEEP = ChaosProperties.properties().getIntProperty("chaos.ownerchange.sleep", 30);
  private static final int ANGLE = ChaosProperties.properties().getIntProperty("chaos.ownerchange.angle", 30);

  private final World mWorld;
  private final TileManager mTM;

  OwnerChangeEffect(final World world, final TileManager tm) {
    mWorld = world;
    mTM = tm;
  }

  @Override
  public void performEffect(final ChaosScreen screen, final Graphics graphics, final Collection<Cell> cells, final int width) {
    if (screen != null && cells != null) {
      // Compute all the effects needed
      final int[] xy = new int[2];
      final HashMap<Cell, RotationEffect> effects = new HashMap<>();
      boolean ok = false;
      for (final Cell ce : cells) {
        final Actor a = ce.peek();
        if (a != null) {
          final BufferedImage bi = mTM.getSpellTile(a);
          effects.put(ce, new RotationEffect(new TileImage(bi), ANGLE, STEPS, 0, false));
          ok = true;
        }
      }
      if (ok) {
        // Avoid this time delayed loop if there is nothing to do.
        final BooleanLock s = Sound.getSoundEngine().play("chaos/resources/sound/misc/owner", SoundLevel.whatSoundLevel(null, cells));
        synchronized (screen.lock()) {
          for (int k = 0; k < STEPS; ++k) {
            for (final Map.Entry<Cell, RotationEffect> e : effects.entrySet()) {
              final TileImage im = e.getValue().next();
              if (im != null) {
                mWorld.getCellCoordinates(e.getKey().getCellNumber(), xy);
                screen.drawCell(im.toBufferedImage(), xy[0], xy[1]);
              }
            }
            Sleep.sleep(SLEEP);
          }
        }
        Sound.getSoundEngine().wait(s, 5000);
      }
    }
  }
}
