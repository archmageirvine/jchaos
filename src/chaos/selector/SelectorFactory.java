package chaos.selector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import chaos.board.CastMaster;
import chaos.board.World;
import chaos.common.wizard.Wizard;

/**
 * Construct spell selectors using reflection.
 *
 * @author Sean A. Irvine
 */
public final class SelectorFactory {

  private SelectorFactory() { }

  /**
   * Construct a new selector.  Since different selectors require different parameters,
   * this method looks at the constructors to choose an appropriate one.
   *
   * @param className selector name
   * @param wiz wizard
   * @param world the world
   * @param castMaster casting rules
   * @return a spell selector
   */
  public static Selector createSelector(final String className, final Wizard wiz, final World world, final CastMaster castMaster) {
    try {
      final Class<? extends Selector> clazz = Class.forName(className).asSubclass(Selector.class);
      try {
        final Constructor<? extends Selector> c1 = clazz.getConstructor(Wizard.class, World.class, CastMaster.class);
        return c1.newInstance(wiz, world, castMaster);
      } catch (final NoSuchMethodException e) {
        // try next one
      }
      // fall back to zero arg constructor
      return clazz.getConstructor().newInstance();
    } catch (final ClassNotFoundException | InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  private static final Random RANDOM = new Random();

  private static final String[] SELECTORS = {
      "chaos.selector.Ranker",
      "chaos.selector.Strategiser",
      "chaos.selector.OrdinarySelector",
      "chaos.selector.Creaturologist",
      "chaos.selector.RandomAiSelector",
      "chaos.selector.ContraryRanker",
      "chaos.selector.WeakCreaturologist",
      "chaos.selector.Ranker", // One extra case just in case we fall off end in search
  };

  private static final double[] PROBABILITY = {
    0.4,
    0.35,
    0.05, // OrdinarySelector is always inferior to the Strategiser
    0.15,
    0.02,
    0.02,
    0.01,
  };


  /**
   * Construct a new selector, choosing at random among the various AI selectors.
   * Some are made more common than others.
   *
   * @param wiz wizard
   * @param world the world
   * @param castMaster casting rules
   * @return a spell selector
   */
  public static Selector randomSelector(final Wizard wiz, final World world, final CastMaster castMaster) {
    double p = RANDOM.nextDouble();
    int s = 0;
    while (p > PROBABILITY[s]) {
      p -= PROBABILITY[s];
      ++s;
    }
    return createSelector(SELECTORS[s], wiz, world, castMaster);
  }
}
