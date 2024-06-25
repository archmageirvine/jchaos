package chaos.graphics;

import java.awt.Graphics2D;
import java.awt.Shape;

import chaos.board.World;
import chaos.sound.Sound;
import chaos.util.BooleanLock;
import chaos.util.WeaponEffectEvent;
import irvine.util.graphics.Thunderbolt;

/**
 * Thunderbolt weapon effect.
 * @author Sean A. Irvine
 */
public final class ThunderboltWeapon {

  private ThunderboltWeapon() {
  }

  static void render(final World world, final ChaosScreen screen, final Graphics2D graphics, final WeaponEffectEvent e, final int width, final int widthBits) {
    final int source = e.getSource();
    final int target = e.getTarget();
    final int sl = SoundLevel.whatSoundLevel(world.actor(source), world.actor(target));
    final BooleanLock s = Sound.getSoundEngine().play("chaos/resources/sound/misc/Lightning", sl);
    final int[] xys = PlasmaWeapon.cellToCoords(world, screen, widthBits, source);
    final int[] xyt = PlasmaWeapon.cellToCoords(world, screen, widthBits, target);
    final int hw = width >>> 1;
    synchronized (screen.lock()) {
      // last param is effect time in seconds
      final Shape oldClip = screen.clipToArena(graphics);
      Thunderbolt.draw(screen, graphics, xys[0] + hw, xys[1] + hw, xyt[0] + hw, xyt[1] + hw, 1000);
      graphics.setClip(oldClip);
    }
    Sound.getSoundEngine().wait(s, 5000);
  }
}
