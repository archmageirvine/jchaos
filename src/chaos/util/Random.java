package chaos.util;

/**
 * Provides a common random number generator for use in all game
 * classes, thereby preventing instantiation of many random
 * number generators.
 *
 * @author Sean A. Irvine
 */
public final class Random {

  /** Prevent instantiation of this class. */
  private Random() { }

  /** Underlying generator. */
  private static final java.util.Random RANDOM = new java.util.Random();

  /**
   * Return a random number in the range 0 &lt;= n &lt; bound
   *
   * @param bound upper bound
   * @return random number
   */
  public static int nextInt(final int bound) {
    return RANDOM.nextInt(bound);
  }

  /**
   * Return a random bit
   *
   * @return random bit
   */
  public static boolean nextBoolean() {
    return RANDOM.nextBoolean();
  }

}
