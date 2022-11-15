package chaos.common;

import static chaos.util.RankingTable.getRanking;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import chaos.board.Cell;
import chaos.common.wizard.Wizard;
import chaos.util.CastUtils;
import chaos.util.ChaosProperties;
import chaos.util.Random;
import irvine.util.io.IOUtils;

/**
 * Hold a frequency distribution for castables. This is used in the construction
 * of spell lists for wizards, selection of bonus spells, and for other special
 * casting situations like the deities.
 * @author Sean A. Irvine
 */
public final class FrequencyTable {

  /** The standard wizard spell frequency table. */
  public static final FrequencyTable DEFAULT = new FrequencyTable(ChaosProperties.properties().getProperty("chaos.frequency.file", "chaos/resources/frequency.txt"), false);
  /** All castables, including 0 frequency items. */
  public static final FrequencyTable ALL = new FrequencyTable(ChaosProperties.properties().getProperty("chaos.frequency.file", "chaos/resources/frequency.txt"), true);

  private final HashMap<Class<? extends Castable>, Integer> mCastables = new HashMap<>();
  private final int mGrandTotal;
  private final int mPositiveTotal;
  private int mMeanScore = -1;

  /**
   * Construct a spell frequency table from a given resource.
   * @param resource location of file describing table
   * @param includeZeroFrequency include castables with 0 frequency
   */
  public FrequencyTable(final String resource, final boolean includeZeroFrequency) {
    try {
      try (BufferedReader is = IOUtils.reader(resource)) {
        String line;
        int grandTotal = 0;
        int positiveTotal = 0;
        while ((line = is.readLine()) != null) {
          if (!line.isEmpty() && line.charAt(0) != '#') {
            final StringTokenizer st = new StringTokenizer(line);
            if (st.hasMoreTokens()) {
              final Class<? extends Castable> clazz;
              final String c = st.nextToken();
              try {
                clazz = Class.forName(c).asSubclass(Castable.class);
              } catch (final Exception e) {
                throw new RuntimeException(e);
              }
              if (!st.hasMoreTokens()) {
                throw new RuntimeException("Missing frequency.");
              }
              final int f;
              try {
                f = Integer.parseInt(st.nextToken());
              } catch (final NumberFormatException e) {
                throw new RuntimeException(e);
              }
              if (!includeZeroFrequency && f == 0) {
                continue; // no need to record 0 probability spells
              }
              if (f < 0 || f > 100) {
                throw new RuntimeException("Frequency out of range [0,100]: " + f);
              }
              final char flag = st.hasMoreTokens() ? Character.toUpperCase(st.nextToken().charAt(0)) : ' ';
              mCastables.put(clazz, flag == 'B' ? -f : f);
              grandTotal += f;
              if (flag != 'B') {
                positiveTotal += f;
              }
            }
          }
        }
        mGrandTotal = grandTotal;
        mPositiveTotal = positiveTotal;
        assert mGrandTotal >= mPositiveTotal;
      }
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Return a set of all classes corresponding to castables with nonzero
   * probability.
   *
   * @return set of classes
   */
  public Set<Class<? extends Castable>> getCastableClasses() {
    return mCastables.keySet();
  }

  /**
   * Instantiate a castable from its name.  Assumes the supplied class is
   * valid.
   *
   * @param clazz class to instantiate
   * @return instance of the spell
   * @exception RuntimeException if an instantiation problem occurs.
   */
  public static Castable instantiate(final Class<? extends Castable> clazz) {
    try {
      return clazz.getDeclaredConstructor().newInstance();
    } catch (final InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Return the total number of known spells.
   *
   * @return number of spells
   */
  public int getNumberOfSpells() {
    return mCastables.size();
  }

  /**
   * Return the frequency count for a given spell.  If the spell is not
   * known then 0 is returned.  The result is in the range [-100,100]
   * and is negative for bonus spells.
   *
   * @param clazz spell to get frequency of
   * @return the frequency
   */
  public int getFrequency(final Class<? extends Castable> clazz) {
    final Integer i = mCastables.get(clazz);
    return i == null ? 0 : i;
  }

  /**
   * Return the frequency as a probability across all ordinary spells.
   *
   * @param clazz spell to get probability of
   * @return the probability
   */
  float getProbability(final Class<? extends Castable> clazz) {
    final float r = (float) getFrequency(clazz) / (float) mPositiveTotal;
    return r < 0.0F ? 0.0F : r;
  }

  /**
   * Return the frequency as a probability across all spells.
   *
   * @param clazz spell to get probability of
   * @return the probability
   */
  float getBonusProbability(final Class<? extends Castable> clazz) {
    final float r = (float) getFrequency(clazz) / (float) mGrandTotal;
    return r < 0.0F ? -r : r;
  }

  /**
   * Select a random spell from the standard set of spell according to
   * the spell frequency table.  That is, bonus spells are excluded.
   *
   * @return the selected castable
   */
  public Class<? extends Castable> getRandom() {
    int target = Random.nextInt(mPositiveTotal);
    for (final Map.Entry<Class<? extends Castable>, Integer> entry : mCastables.entrySet()) {
      final int v = entry.getValue();
      if (v > 0) {
        target -= v;
        if (target < 0) {
          return entry.getKey();
        }
      }
    }
    // this should never happen
    throw new RuntimeException("Internal error getting spell");
  }

  /**
   * Select a random spell from the set of all spells according to
   * the spell frequency table.  That is, bonus spells are included.
   *
   * @return the selected castable
   */
  public Class<? extends Castable> getBonusRandom() {
    int target = Random.nextInt(mGrandTotal);
    for (final Map.Entry<Class<? extends Castable>, Integer> entry : mCastables.entrySet()) {
      final int v = Math.abs(entry.getValue());
      target -= v;
      if (target < 0) {
        return entry.getKey();
      }
    }
    // this should never happen
    throw new RuntimeException("Internal error getting spell");
  }

  /** How much more likely to get better spells with each level cast. */
  private static final int LEVEL_SCALE = ChaosProperties.properties().getIntProperty("chaos.level.scale", 1);

  /**
   * Return an array of bonus spells.  The resulting array contains <code>count</code>
   * entries selected according to the bonus probability distribution for a wizard
   * of the specified level.  If the level is negative then the array will have null
   * entries.
   *
   * @param count number of spells
   * @param level wizard level
   * @return array of spells
   * @exception NegativeArraySizeException if <code>count</code> is negative.
   */
  public Castable[] getBonusChoice(final int count, final int level) {
    final Castable[] res = new Castable[count];
    for (int i = 0; i < count; ++i) {
      int bestRanking = -1;
      for (int j = 0; j <= level * LEVEL_SCALE; ++j) {
        final Castable c = instantiate(getBonusRandom());
        final int r = getRanking(c);
        if (r > bestRanking) {
          bestRanking = r;
          res[i] = c;
        }
      }
    }
    return res;
  }

  /**
   * Select a random spell from the set of all spells according to
   * the uniform distribution.  This is used to implement the joker.
   *
   * @return the selected castable
   */
  public Class<? extends Castable> getUniformRandom() {
    // this could probably be made faster
    int target = Random.nextInt(getNumberOfSpells());
    for (final Class<? extends Castable> clazz : mCastables.keySet()) {
      if (--target < 0) {
        return clazz;
      }
    }
    // this should never happen
    throw new RuntimeException("Internal error getting spell");
  }

  /**
   * Construct a new actor according to a uniform random distribution.
   *
   * @return the created actor
   */
  public Actor getUniformRandomActor() {
    while (true) {
      try {
        final Castable c = getUniformRandom().getConstructor().newInstance();
        if (c instanceof Actor && !(c instanceof Wizard)) {
          return (Actor) c;
        }
      } catch (final InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private boolean checkAllowedMonster(final Castable c) {
    return c instanceof Monster && !(c instanceof Wizard) && !(c instanceof Inanimate) && !(c instanceof Growth);
  }

  /**
   * Construct a new actor according to a uniform random distribution.
   *
   * @return the created actor
   */
  public Monster getUniformRandomMonster() {
    while (true) {
      try {
        final Castable c = getUniformRandom().getDeclaredConstructor().newInstance();
        if (checkAllowedMonster(c)) {
          return (Monster) c;
        }
      } catch (final InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    }
  }

  /**
   * Select a random monster from the set of all spells according to
   * the frequency table.  That is, bonus spells are included.
   *
   * @return the selected monster class
   */
  public Monster getRandomMonster() {
    while (true) {
      int target = Random.nextInt(mGrandTotal);
      for (final Map.Entry<Class<? extends Castable>, Integer> entry : mCastables.entrySet()) {
        final int v = Math.abs(entry.getValue());
        target -= v;
        if (target < 0) {
          final Castable c = instantiate(entry.getKey());
          if (checkAllowedMonster(c)) {
            return (Monster) c;
          } else {
            break; // try another selected
          }
        }
      }
    }
  }

  /**
   * Get an instance of a castable based on a partial class name.  If multiple classes
   * match, then some attempt is made to return the best match.  If there is no match then null
   * is returned.
   * @param name partial class name of castable
   * @return an instance of the castable or null
   */
  public Castable getByPartialName(final String name) {
    Castable bestMatch = null;
    for (final Class<? extends Castable> c : mCastables.keySet()) {
      if (c.getName().endsWith(name)) {
        if (bestMatch == null || c.getName().endsWith("." + name)) {
          try {
            bestMatch = c.getDeclaredConstructor().newInstance();
          } catch (final InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
          }
        }
      }
    }
    return bestMatch;
  }

  /**
   * Return the mean score of items in this list.
   * @return mean score
   */
  public int getMeanScore() {
    if (mMeanScore == -1) {
      // Compute mean score used to decide the target.
      int sum = 0;
      int k = 0;
      final Cell cell = new Cell(0);
      for (final Class<? extends Castable> c : getCastableClasses()) {
        final Castable cc = FrequencyTable.instantiate(c);
        if (cc instanceof Actor) {
          cell.push((Actor) cc);
          sum += CastUtils.score(cell);
          ++k;
        }
      }
      mMeanScore = (sum + k - 1) / k;
    }
    return mMeanScore;
  }
}
