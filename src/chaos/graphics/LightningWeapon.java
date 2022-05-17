package chaos.graphics;

import java.awt.Graphics;

import chaos.board.World;
import chaos.sound.Sound;
import chaos.util.BooleanLock;
import chaos.util.WeaponEffectEvent;
import irvine.util.graphics.Lightning;

/**
 * Lightning weapon effect.
 *
 * @author Sean A. Irvine
 */
public final class LightningWeapon {

  private LightningWeapon() { }

  static void render(final World world, final ChaosScreen screen, final Graphics g, final WeaponEffectEvent e, final int width, final int widthBits, final int milliseconds) {
    final int source = e.getSource();
    final int target = e.getTarget();
    final int sl = SoundLevel.whatSoundLevel(world.actor(source), world.actor(target));
    final BooleanLock s = Sound.getSoundEngine().play("chaos/resources/sound/misc/Lightning", sl);
    final int[] xys = PlasmaWeapon.cellToCoords(world, screen, widthBits, source);
    final int[] xyt = PlasmaWeapon.cellToCoords(world, screen, widthBits, target);
    final int hw = width >>> 1;
    synchronized (screen.lock()) {
      Lightning.draw(g, xys[0] + hw, xys[1] + hw, xyt[0] + hw, xyt[1] + hw, milliseconds);
    }
    Sound.getSoundEngine().wait(s, 5000);
  }
}
