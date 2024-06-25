package chaos.graphics;

import java.util.Arrays;
import java.util.Comparator;

import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.State;
import chaos.common.wizard.Wizard;
import irvine.util.string.TextTable;

/**
 * Text version of current scores.
 *
 * @author Sean A. Irvine
 */
public class TextScoreDisplay implements ScoreDisplay {

  /** The world. */
  private final World mWorld;
  /** The wizards. */
  private final Wizard[] mWizards;

  /**
   * Construct a new score display.
   *
   * @param w the world
   * @param wizards the array of wizards
   * @param indp wizards representing independents
   */
  public TextScoreDisplay(final World w, final Wizard[] wizards, final Wizard indp) {
    mWorld = w;
    // copy independents into wizard array
    mWizards = new Wizard[wizards.length + 1];
    System.arraycopy(wizards, 0, mWizards, 0, wizards.length);
    mWizards[wizards.length] = indp;
  }

  private static final Comparator<Wizard> SCORE_COMPARATOR = (a, b) -> {
    if (a == b) {
      // this should only be true if both are null
      return 0;
    }
    if (a == null) {
      return -1;
    }
    if (b == null) {
      return 1;
    }
    if (a.getScore() == b.getScore()) {
      return b.getMass() - a.getMass();
    }
    return b.getScore() - a.getScore();
  };

  private void getMass() {
    for (final Wizard w : mWizards) {
      if (w != null) {
        w.setMass(0);
      }
    }
    for (int c = 0; c < mWorld.size(); ++c) {
      final Actor a = mWorld.actor(c);
      if (a != null && a.getState() == State.ACTIVE) {
        final Wizard w = mWorld.getWizardManager().getWizard(a);
        if (w != null) {
          w.setMass(w.getMass() + a.get(Attribute.LIFE));
        }
      }
    }
  }

  @Override
  public void showScores(final int turn) {
    System.out.println("Turn number: " + turn);
    // sort based on score, then mass
    getMass();
    Arrays.sort(mWizards, SCORE_COMPARATOR);
    final TextTable scoreTable = new TextTable();
    scoreTable.addRow("id", "name", "state", "score", "life");
    scoreTable.addSeparator();
    for (final Wizard w : mWizards) {
      if (w != null) {
        scoreTable.addRow(String.valueOf(w.getOwner()), w.getPersonalName(), w.getState().toString(), String.valueOf(w.getScore()), String.valueOf(w.getMass()));
      }
    }
    System.out.print(scoreTable);
  }

}
