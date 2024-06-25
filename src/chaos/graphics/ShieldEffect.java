package chaos.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.sound.Sound;
import chaos.util.BooleanLock;
import chaos.util.Sleep;
import irvine.tile.ExplodingCircleEffect;
import irvine.tile.ExplosionEffect;
import irvine.tile.TileEffect;
import irvine.tile.TileImage;

/**
 * Shield effect.
 * @author Sean A. Irvine
 */
public class ShieldEffect extends AbstractEffect {

  static final HashMap<Attribute, String> SHIELD_SOUND = new HashMap<>();

  static {
    SHIELD_SOUND.put(Attribute.AGILITY, "chaos/resources/sound/casting/Smash");
    SHIELD_SOUND.put(Attribute.AGILITY_RECOVERY, "chaos/resources/sound/casting/Smash");
    SHIELD_SOUND.put(Attribute.COMBAT, "chaos/resources/sound/casting/Laugh");
    SHIELD_SOUND.put(Attribute.COMBAT_RECOVERY, "chaos/resources/sound/casting/Laugh");
    SHIELD_SOUND.put(Attribute.INTELLIGENCE, "chaos/resources/sound/casting/Wisdom");
    SHIELD_SOUND.put(Attribute.INTELLIGENCE_RECOVERY, "chaos/resources/sound/casting/Wisdom");
    SHIELD_SOUND.put(Attribute.LIFE, "chaos/resources/sound/casting/Bless");
    SHIELD_SOUND.put(Attribute.LIFE_RECOVERY, "chaos/resources/sound/casting/Bless");
    SHIELD_SOUND.put(Attribute.MAGICAL_RESISTANCE, "chaos/resources/sound/words/Protection");
    SHIELD_SOUND.put(Attribute.MAGICAL_RESISTANCE_RECOVERY, "chaos/resources/sound/words/Protection");
    SHIELD_SOUND.put(Attribute.MOVEMENT, "chaos/resources/sound/casting/Speed");
    SHIELD_SOUND.put(Attribute.MOVEMENT_RECOVERY, "chaos/resources/sound/casting/Speed");
    SHIELD_SOUND.put(Attribute.RANGE, "chaos/resources/sound/casting/Thunk");
    SHIELD_SOUND.put(Attribute.RANGE_RECOVERY, "chaos/resources/sound/casting/Thunk");
    SHIELD_SOUND.put(Attribute.RANGED_COMBAT, "chaos/resources/sound/casting/Thunk");
    SHIELD_SOUND.put(Attribute.RANGED_COMBAT_RECOVERY, "chaos/resources/sound/casting/Thunk");
    SHIELD_SOUND.put(Attribute.SPECIAL_COMBAT, "chaos/resources/sound/casting/Magnum");
    SHIELD_SOUND.put(Attribute.SPECIAL_COMBAT_RECOVERY, "chaos/resources/sound/casting/Magnum");
    SHIELD_SOUND.put(Attribute.SHOTS, "chaos/resources/sound/casting/quickshot");
  }

  /* Lazy cache of shields. */
  private static final HashMap<String, BufferedImage[]> SHIELDS = new HashMap<>();

  /* Get a shield with the specified color. */
  static BufferedImage[] getShieldArray(final int width, final Color fg) {
    final String key = fg.toString() + "_" + width;
    BufferedImage[] res = SHIELDS.get(key);
    if (res == null) {
      final TileEffect i = new ExplodingCircleEffect(width, 0xFF000000, 0xFF000000 | fg.getRGB());
      i.next();
      i.next();
      i.next();
      i.next();
      i.next();
      i.next();
      final TileImage circle = i.next();
      final ArrayList<BufferedImage> k = new ArrayList<>();
      for (final TileImage j : new ExplosionEffect(circle, 0xFF000000, false).list()) {
        k.add(j.toBufferedImage());
      }
      res = new BufferedImage[k.size() + 1];
      for (int l = 0, m = k.size() - 1; m >= 0; ++l, --m) {
        res[l] = k.get(m);
      }
      res[k.size()] = circle.toBufferedImage();
      SHIELDS.put(key, res);
    }
    return res;
  }

  private static final Random RANDOM = new Random();

  private final World mWorld;
  private final Attribute mAttr;
  private final Actor mCause;

  ShieldEffect(final World world, final Attribute attr, final Actor cause) {
    mWorld = world;
    mAttr = attr;
    mCause = cause;
  }

  @Override
  public void performEffect(final ChaosScreen screen, final Graphics graphics, final Collection<Cell> cells, final int width) {
    if (screen != null && cells != null && !cells.isEmpty() && mAttr != null) {
      final int time = 1 + 1000 / cells.size();
      final int soundBase = 200 * (1 + mAttr.ordinal());
      final int soundLevel = SoundLevel.whatSoundLevel(mCause, cells);
      final Sound s = Sound.getSoundEngine();
      final int[] xy = new int[2];
      final int sleep = (50 * 16) / width;
      synchronized (screen.lock()) {
        s.startSynthetic();
        int k = 0;
        for (final BufferedImage i : getShieldArray(width, mAttr.getColor())) {
          for (final Cell c : cells) {
            if (k == 2) {
              final int freq = soundBase + RANDOM.nextInt(400);
              s.playSynthetic(freq, 1.0, time, soundLevel);
            }
            final int cn = c.getCellNumber();
            if (cn >= 0) {
              mWorld.getCellCoordinates(cn, xy);
              screen.drawCell(i, xy[0], xy[1]);
            }
          }
          Sleep.sleep(sleep);
          ++k;
        }
        s.stopSynthetic();
      }
    }
  }

  @Override
  public void performEffect(final ChaosScreen screen, final Graphics graphics, final Cell cell, final int width) {
    if (cell != null && mAttr != null) {
      final Sound s = Sound.getSoundEngine();
      final int[] xy = new int[2];
      mWorld.getCellCoordinates(cell.getCellNumber(), xy);
      final int x = xy[0];
      final int y = xy[1];
      final String sound = SHIELD_SOUND.get(mAttr);
      final int soundLevel = SoundLevel.whatSoundLevel(mCause, cell.peek());
      final BooleanLock status = s.play(sound, soundLevel);
      final int sleep = (30 * 16) / width;
      synchronized (screen.lock()) {
        for (final BufferedImage i : getShieldArray(width, mAttr.getColor())) {
          screen.drawCell(i, x, y);
          Sleep.sleep(sleep);
        }
      }
      s.wait(status, 5000);
    }
  }
}
