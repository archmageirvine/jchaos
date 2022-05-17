package chaos.graphics;

import java.util.Collection;

import chaos.Chaos;
import chaos.board.Cell;
import chaos.board.WizardManager;
import chaos.common.Actor;
import chaos.common.Conveyance;
import chaos.common.wizard.Wizard;
import chaos.engine.HumanEngine;
import chaos.sound.Sound;

/**
 * Utility functions to determine appropriate sound priority level.
 *
 * @author Sean A. Irvine
 */
public final class SoundLevel {

  private SoundLevel() { }

  /**
   * Determine the sound priority based on whether or not a human is
   * involved in the action.
   *
   * @param s source
   * @param t target
   * @return sound level
   */
  public static int whatSoundLevel(final Actor s, final Actor t) {
    if (Sound.getSoundEngine().getSoundLevel() == Sound.SOUND_ALL) {
      return Sound.SOUND_ALL;
    }
    final WizardManager wm = Chaos.getChaos().getWorld().getWizardManager();
    if (s != null) {
      if (s instanceof Conveyance) {
        final Actor m = ((Conveyance) s).getMount();
        if (m != null) {
          final Wizard wc = wm.getWizard(m);
          if (wc != null && wc.getPlayerEngine() instanceof HumanEngine) {
            return Sound.SOUND_INTELLIGENT;
          }
        }
      }
      final Wizard ws = wm.getWizard(s);
      if (ws != null && ws.getPlayerEngine() instanceof HumanEngine) {
        return Sound.SOUND_INTELLIGENT;
      }
    }
    if (t != null) {
      final Wizard wt = wm.getWizard(t);
      if (wt != null && wt.getPlayerEngine() instanceof HumanEngine) {
        return Sound.SOUND_INTELLIGENT;
      }
    }
    return Sound.SOUND_ALL;
  }

  /**
   * Determine the sound priority based on whether or not a human is
   * involved in the action.
   *
   * @param s actor
   * @return sound level
   */
  public static int whatSoundLevel(final Actor s) {
    return whatSoundLevel(s, (Actor) null);
  }

  /**
   * Determine the sound priority based on whether or not a human is
   * involved in the action.
   *
   * @param s source
   * @param targets target cells
   * @return sound level
   */
  public static int whatSoundLevel(final Actor s, final Collection<Cell> targets) {
    if (Sound.getSoundEngine().getSoundLevel() == Sound.SOUND_ALL) {
      return Sound.SOUND_ALL;
    }
    final WizardManager wm = Chaos.getChaos().getWorld().getWizardManager();
    if (s != null) {
      final Wizard ws = wm.getWizard(s);
      if (ws != null && ws.getPlayerEngine() instanceof HumanEngine) {
        return Sound.SOUND_INTELLIGENT;
      }
    }
    if (targets != null) {
      for (final Cell tc : targets) {
        final Actor t = tc.peek();
        if (t != null) {
          final Wizard wt = wm.getWizard(t);
          if (wt != null && wt.getPlayerEngine() instanceof HumanEngine) {
            return Sound.SOUND_INTELLIGENT;
          }
        }
      }
    }
    return Sound.SOUND_ALL;
  }

}
