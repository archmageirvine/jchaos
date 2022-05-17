package chaos.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Collection;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Realm;
import chaos.sound.Sound;
import chaos.util.BooleanLock;
import chaos.util.Sleep;
import irvine.tile.HorizontalRollEffect;
import irvine.tile.TileImage;

/**
 * Realm change effect.
 *
 * @author Sean A. Irvine
 */
public class RealmChangeEffect extends AbstractEffect {

  private final World mWorld;
  private final Actor mCause;
  private final TileManager mTM;

  RealmChangeEffect(final World world, final TileManager tm, final Actor cause) {
    mWorld = world;
    mTM = tm;
    mCause = cause;
  }

  @Override
  public void performEffect(final ChaosScreen screen, final Graphics graphics, final Collection<Cell> cells, final int width) {
    if (screen != null && cells != null && !cells.isEmpty()) {
      // Choose the realm based on any cell in the set, it is assumed that the set is homogeneous anyway.
      final Cell[] ca = cells.toArray(new Cell[0]);
      final Actor a = ca[0].peek();
      final Realm r = a == null ? null : a.getRealm();
      final Sound s = Sound.getSoundEngine();
      final BooleanLock status = r == null ? null : s.play("chaos/resources/sound/realm/" + r, SoundLevel.whatSoundLevel(mCause, cells));
      final int[] xy = new int[2];
      final HorizontalRollEffect[] effects = new HorizontalRollEffect[ca.length];
      // Precompute roll arrays, all will be same length
      boolean sawNonNull = false;
      for (int k = 0; k < ca.length; ++k) {
        final BufferedImage bi = mTM.getSpellTile(ca[k].peek());
        effects[k] = bi == null ? null : new HorizontalRollEffect(new TileImage(bi), true);
        sawNonNull |= effects[k] != null;
      }
      if (sawNonNull) {
        synchronized (screen.lock()) {
          boolean ok = true;
          while (ok) {
            for (int k = 0; k < ca.length; ++k) {
              if (effects[k] != null) {
                final TileImage i = effects[k].next();
                ok = i != null;
                if (ok) {
                  final int cn = ca[k].getCellNumber();
                  if (cn >= 0) {
                    mWorld.getCellCoordinates(cn, xy);
                    screen.drawCell(i.toBufferedImage(), xy[0], xy[1]);
                  }
                } else {
                  break;
                }
              }
            }
            Sleep.sleep(50);
          }
        }
      }
      s.wait(status);
    }
  }
}
