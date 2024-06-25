package chaos.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
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

/**
 * Effect for removal of shields.
 * @author Sean A. Irvine
 */
public class UnshieldEffect extends AbstractEffect {

  static final HashMap<Attribute, String> UNSHIELD_SOUND = new HashMap<>();

  static {
    UNSHIELD_SOUND.put(Attribute.AGILITY, "chaos/resources/sound/casting/Clumsy");
    UNSHIELD_SOUND.put(Attribute.AGILITY_RECOVERY, "chaos/resources/sound/casting/Clumsy");
    UNSHIELD_SOUND.put(Attribute.COMBAT, "chaos/resources/sound/casting/Touch");
    UNSHIELD_SOUND.put(Attribute.COMBAT_RECOVERY, "chaos/resources/sound/casting/Touch");
    UNSHIELD_SOUND.put(Attribute.INTELLIGENCE, "chaos/resources/sound/casting/Idiocy");
    UNSHIELD_SOUND.put(Attribute.INTELLIGENCE_RECOVERY, "chaos/resources/sound/casting/Idiocy");
    UNSHIELD_SOUND.put(Attribute.LIFE, "chaos/resources/sound/casting/Ouch");
    UNSHIELD_SOUND.put(Attribute.LIFE_RECOVERY, "chaos/resources/sound/casting/Disease");
    UNSHIELD_SOUND.put(Attribute.MAGICAL_RESISTANCE, "chaos/resources/sound/words/Bolt");
    UNSHIELD_SOUND.put(Attribute.MAGICAL_RESISTANCE_RECOVERY, "chaos/resources/sound/words/Bolt");
    UNSHIELD_SOUND.put(Attribute.MOVEMENT, "chaos/resources/sound/casting/Stop");
    UNSHIELD_SOUND.put(Attribute.MOVEMENT_RECOVERY, "chaos/resources/sound/casting/Stop");
    UNSHIELD_SOUND.put(Attribute.RANGE, "chaos/resources/sound/casting/Thunk");
    UNSHIELD_SOUND.put(Attribute.RANGE_RECOVERY, "chaos/resources/sound/casting/Thunk");
    UNSHIELD_SOUND.put(Attribute.RANGED_COMBAT, "chaos/resources/sound/casting/Thunk");
    UNSHIELD_SOUND.put(Attribute.RANGED_COMBAT_RECOVERY, "chaos/resources/sound/casting/Thunk");
    UNSHIELD_SOUND.put(Attribute.SPECIAL_COMBAT, "chaos/resources/sound/casting/Nullify");
    UNSHIELD_SOUND.put(Attribute.SPECIAL_COMBAT_RECOVERY, "chaos/resources/sound/casting/Nullify");
  }

  private static final Random RANDOM = new Random();

  private final World mWorld;
  private final Attribute mAttr;
  private final Actor mCause;

  UnshieldEffect(final World world, final Attribute attr, final Actor cause) {
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
      final BufferedImage[] pics = ShieldEffect.getShieldArray(width, mAttr.getColor());
      final int sleep = (50 * 16) / width;
      synchronized (screen.lock()) {
        s.startSynthetic();
        int k = 0;
        for (int j = pics.length - 1; j >= 0; --j) {
          for (final Cell c : cells) {
            if (k == 2) {
              final Actor a = c.peek();
              if (a != null) {
                final int freq = soundBase + RANDOM.nextInt(400);
                s.playSynthetic(freq, 1.0, time, soundLevel);
              }
            }
            final int cn = c.getCellNumber();
            if (cn >= 0) {
              mWorld.getCellCoordinates(cn, xy);
              screen.drawCell(pics[j], xy[0], xy[1]);
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
      final String sound = UNSHIELD_SOUND.get(mAttr);
      final int soundLevel = SoundLevel.whatSoundLevel(mCause, cell.peek());
      final BooleanLock status = s.play(sound, soundLevel);
      final BufferedImage[] pics = ShieldEffect.getShieldArray(width, mAttr.getColor());
      final int sleep = (40 * 16) / width;
      synchronized (screen.lock()) {
        for (int j = pics.length - 1; j >= 0; --j) {
          screen.drawCell(pics[j], x, y);
          Sleep.sleep(sleep);
        }
      }
      s.wait(status, 5000);
    }
  }
}
