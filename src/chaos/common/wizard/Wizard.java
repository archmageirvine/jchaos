package chaos.common.wizard;

import java.util.ArrayList;

import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.BowShooter;
import chaos.common.Castable;
import chaos.common.CastableList;
import chaos.common.Caster;
import chaos.common.FrequencyTable;
import chaos.common.Humanoid;
import chaos.common.Monster;
import chaos.common.Named;
import chaos.common.PowerUps;
import chaos.common.Realm;
import chaos.common.Rider;
import chaos.common.Singleton;
import chaos.common.State;
import chaos.selector.Selector;
import chaos.util.NameUtils;
import chaos.util.Random;

/**
 * This is a special Caster representing a wizard. It contains a lot of
 * extra information specific to wizards.
 * @author Sean A. Irvine
 */
public abstract class Wizard extends Caster implements Bonus, Humanoid, BowShooter, Named, Rider, Singleton {
  {
    setDefault(Attribute.LIFE, 19);
    setDefault(Attribute.MAGICAL_RESISTANCE, 53);
    setDefault(Attribute.INTELLIGENCE, 100);
    setDefault(Attribute.COMBAT, 1);
    setDefault(Attribute.AGILITY, 7);
    setDefault(Attribute.MOVEMENT, 1);
    setState(State.DEAD); // by default wizards are dead
    setRealm(Realm.MATERIAL);
    setCombatApply(Attribute.LIFE);
    setRangedCombatApply(Attribute.LIFE);
    setSpecialCombatApply(Attribute.LIFE);
  }

  @Override
  public int getBonus() {
    return 6;
  }

  @Override
  public long getLosMask() {
    return 0x007E7E7E3C3C7E00L;
  }

  /** The score associated with this wizard */
  private int mScore;

  /**
   * Return this Wizard's score.
   * @return score
   */
  public int getScore() {
    return mScore;
  }

  /**
   * Add the specified points to this Wizard's score.
   * @param points number of points to add
   */
  public void addScore(final int points) {
    if (points < 0) {
      throw new IllegalArgumentException("Cannot add negative points");
    }
    mScore += points;
  }

  /** Used to record a wizards mass during scoring. */
  private int mMass;

  public int getMass() {
    return mMass;
  }

  public void setMass(final int mass) {
    mMass = mass;
  }

  /** The number of bonus spells this player has to select from */
  private int mBonusCount;
  /** The number of bonus spells this player is to select */
  private int mBonusSelect;

  /**
   * Increase the bonus spell selection statistics for this wizard.
   * @param select number to select from
   * @param count number to be selected
   */
  public void addBonus(final int select, final int count) {
    if (count < 0 || count < select) {
      throw new IllegalArgumentException("Bad arguments to addBonus");
    }
    mBonusCount += count;
    mBonusSelect += select;
  }

  /**
   * Return the number of bonus spells this wizard should receive in the
   * next round.
   * @return number of bonus spells
   */
  public int getBonusCount() {
    return mBonusCount;
  }

  /**
   * Return the number of bonus spells this wizard has to choose between
   * in the next round.
   * @return number of bonus spells
   */
  public int getBonusSelect() {
    return mBonusSelect;
  }

  /** Weight value for wizards. */
  private static final int WIZARD_WEIGHT = 60;

  /**
   * Return the default weight for wizards.
   * @return weight value
   */
  @Override
  public final int getDefaultWeight() {
    return WIZARD_WEIGHT;
  }

  /** This wizard's repertoire of spells. */
  private CastableList mCastableList = null;
  /** Spell selection mechanism used by this wizard. */
  private Selector mSelector = null;

  /**
   * Return the list of spells for this wizard.
   * @return castable list
   */
  public CastableList getCastableList() {
    return mCastableList;
  }

  /**
   * Set the list of spells of this wizard.  This is normally only done
   * once in the life of the wizard.
   * @param list list of spells.
   */
  public void setCastableList(final CastableList list) {
    mCastableList = list;
  }

  /**
   * Set the spell selector used by this wizard.  Setting this to null
   * will prevent all casting.
   * @param selector selector to use
   */
  public void setSelector(final Selector selector) {
    mSelector = selector;
  }

  /**
   * The selector used by this wizard.
   * @return spell selector
   */
  public Selector getSelector() {
    return mSelector;
  }

  /** Spell to be cast at the next opportunity. */
  private Castable mSelectedSpell = null;

  /**
   * Perform the spell selection phase for this wizard.
   * @param texas true if Texas mode is active
   */
  public void select(final boolean texas) {
    mSelectedSpell = null;
    if (mSelector != null) {
      final Castable[] spells;
      if (is(PowerUps.COERCION)) {
        spells = new Castable[0];
      } else {
        Castable[] s = mCastableList.getVisible();
        if (is(PowerUps.NECROPOTENCE)) {
          // Only lets monsters through
          final ArrayList<Castable> retain = new ArrayList<>();
          for (final Castable c : s) {
            if (c instanceof Monster) {
              retain.add(c);
            }
          }
          s = retain.toArray(new Castable[0]);
        }
        if (is(PowerUps.AMNESIA) && s.length > 0) {
          s = new Castable[] {s[0]};
        }
        spells = s;
      }
      final Castable[] chosen = mSelector.select(spells, texas);
      // Handle discard (if any)
      if (chosen[1] != null) {
        mCastableList.use(chosen[1]);
      }
      // Handle selected (if any)
      mSelectedSpell = chosen[0];
      if (mSelectedSpell != null) {
        mCastableList.use(mSelectedSpell);
      }
    }
    if (is(PowerUps.COERCION)) {
      set(PowerUps.COERCION, Random.nextInt(2));
    }
    if (is(PowerUps.AMNESIA)) {
      set(PowerUps.AMNESIA, Random.nextInt(2));
    }
    if (is(PowerUps.NECROPOTENCE)) {
      set(PowerUps.NECROPOTENCE, Random.nextInt(2));
    }
  }

  /**
   * Perform the bonus spell selection phase for this wizard.
   */
  public void bonusSelect() {
    if (mSelector != null) {
      for (final Castable c : mSelector.selectBonus(FrequencyTable.DEFAULT.getBonusChoice(mBonusCount, get(PowerUps.LEVEL)), mBonusSelect)) {
        mCastableList.add(c);
      }
      mBonusCount = 0;
      mBonusSelect = 0;
    }
  }

  /**
   * Return the Castable this Wizard wants to cast.  This should
   * have already been decided by a previous call to select.
   * @return spell to be cast
   */
  @Override
  public Castable getCastable() {
    return mSelectedSpell;
  }

  private String mWizardName = NameUtils.getPersonalName(Wizard.class);

  /**
   * Get the name of this wizard.
   * @return wizard's name
   */
  @Override
  public String getPersonalName() {
    return mWizardName;
  }

  /**
   * Set the name of this wizard.
   * @param wizardName name of the wizard
   * @throws IllegalArgumentException if the wizard name is invalid.
   */
  public void setPersonalName(final String wizardName) {
    if (wizardName == null || wizardName.length() > NameUtils.MAX_NAME_LENGTH) {
      throw new IllegalArgumentException("Illegal wizard name");
    }
    mWizardName = wizardName;
  }

  /**
   * No wizard supported reincarnation.
   * @return null
   */
  @Override
  public Class<? extends Monster> reincarnation() {
    return null;
  }
}
