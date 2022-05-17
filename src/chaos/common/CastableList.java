package chaos.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import chaos.common.monster.FireDemon;

/**
 * Maintains a list of castables.  A certain maximum number of castables
 * can be stored in the list, of which only a subset might be visible
 * at any one time.  Castables can be added and removed.  A number of
 * safety guards are built in to detect incorrect use.  This implementation
 * should be fine for spell lists up to a few hundred, beyond that a
 * more sophisticated implementation would be desirable.
 *
 * @author Sean A. Irvine
 */
public class CastableList implements Serializable {

  /** The actual list. */
  private final Castable[] mList;
  /** Visibility constraint. */
  private final int mMaxVisible;

  /**
   * Construct a new CastableList capable of storing <code>maxInList</code>
   * castables of which at most <code>maxVisible</code> will be available at
   * any one time.  The list will be initialized to contain <code>initialCount</code>
   * castables drawn according to usual spell distribution.
   *
   * @param maxInList maximum capacity of the list
   * @param initialCount number of spells to place in list at outset
   * @param maxVisible maximum visible number
   */
  public CastableList(final int maxInList, final int initialCount, final int maxVisible) {
    if (maxVisible > maxInList) {
      throw new IllegalArgumentException("Maximum visible cannot exceed list maximum");
    }
    if (maxInList <= 0 || maxVisible <= 0) {
      throw new IllegalArgumentException("Bad arguments to CastableList");
    }
    if (initialCount < 0 || initialCount > maxInList) {
      throw new IllegalArgumentException("Bad initial count");
    }
    mList = new Castable[maxInList];
    mMaxVisible = maxVisible;
    for (int i = 0; i < initialCount; ++i) {
      mList[i] = FrequencyTable.instantiate(FrequencyTable.DEFAULT.getRandom());
    }
    // To force in a particular spell ...
    //mList[0] = new chaos.common.monster.CatLord();
  }

  /**
   * Return the maximum number of visible castables for this list.
   *
   * @return maximum visible count
   */
  public int getMaximumVisible() {
    return mMaxVisible;
  }

  /**
   * Return an array containing the names of all currently visible
   * castables.
   *
   * @return array of castables.
   */
  public Castable[] getVisible() {
    final ArrayList<Castable> r = new ArrayList<>();
    for (int i = 0; i < mList.length && r.size() < getMaximumVisible(); ++i) {
      if (mList[i] != null) {
        r.add(mList[i]);
      }
    }
    return r.toArray(new Castable[0]);
  }

  /**
   * Return the total number of castables in this list.  This includes
   * those which may not be visible.
   *
   * @return remaining castables.
   */
  public int getCount() {
    int r = 0;
    for (final Castable c : mList) {
      if (c != null) {
        r += 1;
      }
    }
    return r;
  }

  /**
   * Use up the specified castable. Will detect if there really is
   * such a castable available.  If the use is invalid then a
   * RuntimeException is throw.  This must be the exact castable,
   * it is not sufficient to pass another castable of the same class.
   *
   * @param castable castable to use
   */
  public void use(final Castable castable) {
    if (castable == null) {
      throw new NullPointerException("Cannot use null");
    }
    for (int i = 0, j = 0; i < mList.length && j < mMaxVisible; ++i) {
      if (mList[i] == castable) {
        // found it
        mList[i] = null;
        return;
      } else if (mList[i] != null) {
        ++j;
      }
    }
    if (castable instanceof FireDemon) {
      return; // Fire demon easter egg
    }
    throw new RuntimeException("Bad castable use");
  }

  /**
   * Add the specified castable to the list.  It is added in the first
   * available empty slot.  If no empty slots are available then the
   * addition is silently discarded.
   *
   * @param castable the castable to add
   */
  public void add(final Castable castable) {
    if (castable == null) {
      throw new NullPointerException("Cannot add null");
    }
    for (int i = 0; i < mList.length; ++i) {
      if (mList[i] == null) {
        mList[i] = castable;
        return;
      }
    }
  }

  /**
   * Test if the list contains the specified castable.  Depending on
   * the boolean flag either the whole list or only the visible portion
   * is searched.  Note, this checks only that the list contains an
   * object with the same class as the supplied castable.
   *
   * @param castable what to search for
   * @param visibleOnly the visible portion (true) or whole list (false)
   * @return true if list contains specified castable
   */
  public boolean has(final Castable castable, final boolean visibleOnly) {
    if (castable != null) {
      final Class<? extends Castable> castableClass = castable.getClass();
      for (int i = 0, j = 0; i < mList.length && (!visibleOnly || j < getMaximumVisible()); ++i) {
        if (mList[i] != null) {
          if (mList[i].getClass() == castableClass) {
            return true;
          }
          ++j;
        }
      }
    }
    return false;
  }

  /**
   * Replace each castable in this list with a new one selected
   * according to a uniform distribution.  This effectively implements
   * the joker spell.
   *
   */
  public void joker() {
    for (int i = 0; i < mList.length; ++i) {
      if (mList[i] != null) {
        try {
          mList[i] = FrequencyTable.DEFAULT.getUniformRandom().newInstance();
        } catch (final InstantiationException | IllegalAccessException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  /**
   * Clear the entire list.
   */
  public void clear() {
    Arrays.fill(mList, null);
  }

  @Override
  public String toString() {
    return Arrays.toString(mList);
  }
}
