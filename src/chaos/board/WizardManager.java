package chaos.board;

import java.io.Serializable;

import chaos.common.Actor;
import chaos.common.State;
import chaos.common.wizard.Wizard;
import chaos.common.wizard.Wizard1;
import chaos.common.wizard.Wizard10;
import chaos.common.wizard.Wizard11;
import chaos.common.wizard.Wizard12;
import chaos.common.wizard.Wizard13;
import chaos.common.wizard.Wizard14;
import chaos.common.wizard.Wizard15;
import chaos.common.wizard.Wizard2;
import chaos.common.wizard.Wizard3;
import chaos.common.wizard.Wizard4;
import chaos.common.wizard.Wizard5;
import chaos.common.wizard.Wizard6;
import chaos.common.wizard.Wizard7;
import chaos.common.wizard.Wizard8;
import chaos.common.wizard.Wizard9;
import irvine.math.Shuffle;

/**
 * Maintain global wizard objects for real wizards and independents.
 * @author Sean A. Irvine
 */
public class WizardManager implements Serializable {

  /** Constant for the number of wizards in the wizards array. */
  public static final int WIZ_ARRAY_SIZE = 17;

  private final Wizard[] mWizard = new Wizard[WIZ_ARRAY_SIZE];
  /** Pseudowizard used to represent the independents. */
  private final Wizard mIndependent;

  WizardManager() {
    setWizard(1, new Wizard1());
    setWizard(2, new Wizard2());
    setWizard(3, new Wizard3());
    setWizard(4, new Wizard4());
    setWizard(5, new Wizard5());
    setWizard(6, new Wizard6());
    setWizard(7, new Wizard7());
    setWizard(8, new Wizard8());
    setWizard(9, new Wizard9());
    setWizard(10, new Wizard10());
    setWizard(11, new Wizard11());
    setWizard(12, new Wizard12());
    setWizard(13, new Wizard13());
    setWizard(14, new Wizard14());
    setWizard(15, new Wizard15());
    mIndependent = new Wizard1();
    mIndependent.setOwner(Actor.OWNER_INDEPENDENT);
    mIndependent.setState(State.ACTIVE);
    mIndependent.setPersonalName("Independent");
  }

  /**
   * Get the independent pseudowizard.
   * @return independent
   */
  public Wizard getIndependent() {
    return mIndependent;
  }

  /**
   * Return the wizard for player <code>i</code>. If this player has
   * no wizard object then <code>null</code> is returned.
   * @param i id of the wizard
   * @return the wizard
   */
  public Wizard getWizard(final int i) {
    if (i == Actor.OWNER_INDEPENDENT) {
      return mIndependent;
    }
    try {
      return mWizard[i];
    } catch (final ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }

  /**
   * Get the array of all wizards.
   * @return wizards
   */
  public Wizard[] getWizards() {
    final Wizard[] r = new Wizard[mWizard.length];
    System.arraycopy(mWizard, 0, r, 0, mWizard.length);
    return r;
  }

  public int getMaximumPlayerNumber() {
    return mWizard.length;
  }

  /**
   * If possible, get the wizard owning the specified actor.
   * @param a an actor
   * @return a wizard or null
   */
  public Wizard getWizard(final Actor a) {
    if (a == null) {
      return null;
    }
    return getWizard(a.getOwner());
  }

  /**
   * Set the given wizard.
   * @param i wizard number
   * @param w the wizard
   */
  public final void setWizard(final int i, final Wizard w) {
    mWizard[i] = w;
    if (w != null) {
      mWizard[i].setOwner(i);
    }
  }

  /**
   * Randomize the order of the wizards.  This can be used to change the order of play.
   * @param team team information
   */
  public void shuffle(final Team team) {
    Shuffle.shuffle(mWizard);
    // Correct wizard 0
    for (int k = 1; k < mWizard.length; ++k) {
      if (mWizard[k] == null) {
        mWizard[k] = mWizard[0];
        mWizard[0] = null;
      }
    }
    // Compensate the team information
    final int[] team2 = new int[mWizard.length];
    for (int k = 0; k < mWizard.length; ++k) {
      team2[k] = team.getTeam(k);
    }
    for (int k = 0; k < mWizard.length; ++k) {
      if (mWizard[k] != null) {
        team.explicitSetTeam(k, team2[mWizard[k].getOwner()]);
      }
    }
    for (int k = 0; k < mWizard.length; ++k) {
      if (mWizard[k] != null) {
        mWizard[k].setOwner(k);
      }
    }
  }

  /**
   * Count the number of active wizards.
   * @return number of active wizards
   */
  public int getActiveCount() {
    int alive = 0;
    for (final Wizard w : getWizards()) {
      if (w != null && w.getState() == State.ACTIVE) {
        ++alive;
      }
    }
    return alive;
  }
}

