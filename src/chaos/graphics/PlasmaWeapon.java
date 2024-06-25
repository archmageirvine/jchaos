package chaos.graphics;

import java.awt.Graphics;

import chaos.board.World;
import chaos.sound.Sound;
import chaos.util.BooleanLock;
import chaos.util.ChaosProperties;
import chaos.util.WeaponEffectEvent;
import irvine.util.graphics.Plasma;

/**
 * Plasma weapon effect.
 * @author Sean A. Irvine
 */
public final class PlasmaWeapon {

  private PlasmaWeapon() {
  }

  private static final int TOTAL_TIME = ChaosProperties.properties().getIntProperty("chaos.plasma.time", 4000);

  static int[] cellToCoords(final World world, final ChaosScreen screen, final int widthBits, final int cell) {
    final int[] xy = new int[2];
    world.getCellCoordinates(cell, xy);
    xy[0] <<= widthBits;
    xy[0] += screen.getXOffset();
    xy[1] <<= widthBits;
    xy[1] += screen.getYOffset();
    return xy;
  }

  static void render(final World world, final ChaosScreen screen, final Graphics g, final WeaponEffectEvent e, final int width, final int widthBits) {
    final int source = e.getSource();
    final int target = e.getTarget();
    final int color = e.getAttribute().getColor().getRGB();
    final BooleanLock s = Sound.getSoundEngine().play("chaos/resources/sound/casting/Plasma", SoundLevel.whatSoundLevel(world.actor(source), world.actor(target)));
    final int[] xys = cellToCoords(world, screen, widthBits, source);
    final int[] xyt = cellToCoords(world, screen, widthBits, target);
    final int halfWidth = width >>> 1;
    final int pw = width / 3;
    synchronized (screen.lock()) {
      Plasma.plasma(g, xys[0] + halfWidth, xys[1] + halfWidth, xyt[0] + halfWidth, xyt[1] + halfWidth, pw, color, TOTAL_TIME);
    }
    Sound.getSoundEngine().wait(s, TOTAL_TIME);
  }
}
