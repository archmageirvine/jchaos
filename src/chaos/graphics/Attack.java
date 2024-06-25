package chaos.graphics;

import static java.lang.Math.abs;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

import irvine.tile.AttackEffect;
import irvine.tile.TileImage;

/**
 * Draw the standard attack graphics.
 * @author Sean A. Irvine
 */
public final class Attack {

  private Attack() {
  }

  /** Background color. */
  private static final int BG = 0xFF000000;
  /** Foreground color. */
  private static final int FG = 0xFFFF0000;
  /** Text foreground for positive attack. */
  private static final int POS_TFG = 0xFF0000FF;
  /** Text foreground for healing attack. */
  private static final int NEG_TFG = 0xFF00FF00;
  /** Highlighting color. */
  private static final int EC = 0xFFFFFF00;
  /** Maximum damage to display numerically. */
  private static final int MAX_DAMAGE = 32;

  /** Cache of effects. */
  private static final HashMap<String, BufferedImage[]> ATTACK_EFFECT = new HashMap<>();

  /**
   * Get the attack effect for the specified damage.
   * @param width width of image
   * @param damage damage
   * @return attack effect
   */
  public static BufferedImage[] getEffect(final int width, final int damage) {
    final int d = abs(damage);
    final String key = d <= MAX_DAMAGE ? String.valueOf(damage) : "";
    BufferedImage[] r = ATTACK_EFFECT.get(key);
    if (r == null) {
      final List<TileImage> rr = new AttackEffect(width, d <= MAX_DAMAGE ? String.valueOf(d) : "", BG, FG, damage < 0 ? NEG_TFG : POS_TFG, EC, 2).list();
      r = new BufferedImage[rr.size()];
      for (int i = 0; i < r.length; ++i) {
        r[i] = rr.get(i).toBufferedImage();
      }
      ATTACK_EFFECT.put(key, r);
    }
    return r;
  }

}
