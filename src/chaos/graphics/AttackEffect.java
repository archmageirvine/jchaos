package chaos.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Collection;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Realm;
import chaos.sound.Sound;
import chaos.sound.SoundSelection;
import chaos.util.BooleanLock;
import chaos.util.CombatUtils;
import chaos.util.Sleep;

/**
 * Attack graphics.
 *
 * @author Sean A. Irvine
 */
public class AttackEffect extends AbstractEffect {

  private static final Sound SOUND = Sound.getSoundEngine();
  /** Fundamental pause before launch consecutive sounds (ms). */
  private static final int SOUND_DELTA = 100;

  private final World mWorld;
  private final TileManager mTM;
  private final Actor mOffender;
  private final int mDamage;
  private final int mTypeOfCombat;
  private final float mPanShift;

  AttackEffect(final World world, final TileManager tm, final Actor offender, final int damage, final int type) {
    mWorld = world;
    mTM = tm;
    mOffender = offender;
    mDamage = damage;
    mTypeOfCombat = type;
    mPanShift = (float) mWorld.width();
  }

  /* Select the sound volume based on the damage to be inflicted. */
  float selectVolume(final int damage) {
    final int d = Math.abs(damage);
    return d >= 15 ? 1.0F : 0.5F + d * 0.033333F;
  }

  /** Draw the cell with the specified game coordinates (not pixel coordinates). */
  private void drawCell(final ChaosScreen s, final int x, final int y) {
    final Actor a = mWorld.actor(x, y);
    s.drawCell(a == null ? null : mTM.getTile(a, x, y, 0), x, y);
  }

  @Override
  public void performEffect(final ChaosScreen screen, final Graphics graphics, final Collection<Cell> cells, final int width) {
    if (screen != null && cells != null && !cells.isEmpty()) {
      final int[] xy = new int[2];
      final BooleanLock ss = SOUND.play(SoundSelection.getSpecialCombatSound(mOffender), mOffender != null ? SoundLevel.whatSoundLevel(mOffender, cells) : Sound.SOUND_ALL);
      final BufferedImage[] ae = Attack.getEffect(width, mDamage);
      synchronized (screen.lock()) {
        for (int i = 0; i < 20; ++i) {
          for (final Cell c : cells) {
            mWorld.getCellCoordinates(c.getCellNumber(), xy);
            screen.drawCell(ae[i & 1], xy[0], xy[1]);
          }
          Animator.sync();
          Sleep.sleep(10);
        }
        for (final Cell c : cells) {
          mWorld.getCellCoordinates(c.getCellNumber(), xy);
          drawCell(screen, xy[0], xy[1]);
        }
      }
      SOUND.wait(ss);
    }
  }

  @Override
  public void performEffect(final ChaosScreen screen, final Graphics graphics, final Cell cell, final int width) {
    if (screen != null && cell != null) {
      final int[] xy = new int[2];
      mWorld.getCellCoordinates(cell.getCellNumber(), xy);
      // highlight realms
      final Actor defender = cell.peek();
      if (defender != null && mOffender != null) {
        screen.highlight(new Realm[] {mOffender.getRealm(), defender.getRealm()});
      }
      final BufferedImage[] ae = Attack.getEffect(width, mDamage);
      final String[] soundList = mTypeOfCombat == CombatUtils.RANGED ? new String[0] : SoundSelection.getBattleSounds(mOffender, defender, mDamage);
      final BooleanLock[] ss = new BooleanLock[soundList.length];
      final float pan = (xy[0] - mPanShift) / mPanShift;
      float volume = selectVolume(mDamage);
      if (soundList.length == 0) {
        synchronized (screen.lock()) {
          for (int i = 0; i < 20; ++i) {
            screen.drawCell(ae[i & 1], xy[0], xy[1]);
            Animator.sync();
            Sleep.sleep(10);
          }
        }
      } else {
        // get a rough estimate of how long the sound will take
        // this will also help get the sounds into RAM
        int soundLength = 0;
        for (int k = 0; k < soundList.length; ++k) {
          soundLength = Math.max(soundLength, SOUND_DELTA * k + SOUND.getMillisecondLength(soundList[k]));
        }
        // perform one cycle of imagery per sound
        final int soundLevel = SoundLevel.whatSoundLevel(mOffender, defender);
        final int cycles, sleep;
        if (soundLevel <= SOUND.getSoundLevel()) {
          sleep = Math.max(SOUND_DELTA / 20, 20);
          cycles = 20;
        } else {
          sleep = 12;
          cycles = 10;
        }
        for (int k = 0; k < soundList.length; ++k, volume *= 0.6F) {
          if (soundList[k] != null) {
            ss[k] = SOUND.play(soundList[k], soundLevel, volume, pan);
          }
        }
        synchronized (screen.lock()) {
          for (int i = 0; i < cycles; ++i) {
            screen.drawCell(ae[i & 1], xy[0], xy[1]);
            Animator.sync();
            Sleep.sleep(sleep);
          }
        }
        // wait for all sounds to finish
        for (final BooleanLock s : ss) {
          SOUND.wait(s, soundLength);
        }
      }
      drawCell(screen, xy[0], xy[1]);
      screen.highlight((Realm) null);
    }
  }
}
