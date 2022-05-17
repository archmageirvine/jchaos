package chaos.util;

/**
 * Sleeps down to millisecond accuracy.  Circumvents limitations with
 * Thread.sleep().
 *
 * @author Sean A. Irvine
 */
public final class Sleep {

  /** Prevent instantiation of this class. */
  private Sleep() { }

  private static final boolean VERBOSE = false;
  private static final int CALIBRATION_LOOPS = 10;
  private static final int CALIBRATION_LONG_DELAY = 100;
  private static final int SLEEP_THRESHOLD;

  static {
    try {

      // first attempt to find out the overhead of calling sleep, at the
      // end of this loop fudge gives the ms error on any call to sleep
      // compared to call nanoTime
      int fudge = 0;
      for (int i = 0; i < CALIBRATION_LOOPS; ++i) {
        final long s = System.nanoTime();
        Thread.sleep(CALIBRATION_LONG_DELAY);
        final long t = System.nanoTime();
        fudge += (int) (t - s);
      }
      fudge /= CALIBRATION_LOOPS;
      fudge -= CALIBRATION_LONG_DELAY;
      if (fudge < 0) {
        fudge = 0;
      }
      if (VERBOSE) {
        System.err.println("Sleep call overhead: " + fudge + " ns");
      }

      // determine shortest possible ms sleep of JVM. At the end of this
      // loop d is the number of ms actually sleep for with a request
      // to sleep for 1 ms; on most JVMs this will be more than 1.
      int d = 0;
      for (int i = 0; i < CALIBRATION_LOOPS; ++i) {
        final long s = System.nanoTime();
        Thread.sleep(1);
        final long t = System.nanoTime();
        d += (int) (t - s - fudge);
      }
      d /= CALIBRATION_LOOPS;
      if (VERBOSE) {
        System.err.println("Minimum accurate sleep: " + d + " ns");
      }

      // The system presumably sleeps for almost d ns when you call
      // Thread.sleep(0, d), but there is no guarantee yet on the resolution
      // of the sleep counter.  So for out purposes we use twice the
      // value of d as the actual minimum here.
      SLEEP_THRESHOLD = (d << 1) / 1000;
    } catch (final InterruptedException e) {
      throw new RuntimeException("Sleep calibration unexpectedly interrupted.", e);
    }
  }

  /**
   * Try hard to sleep for the specified amount of time.  This method
   * should be more accurate than calling Thread.sleep() in the case
   * of short delays.
   *
   * @param ms time delay in milliseconds.
   */
  public static void sleep(final int ms) {
    /*
    if (ms > 100) {
      Thread.currentThread().dumpStack();
    }
    */
    if (ms > 0) {
      if (ms > SLEEP_THRESHOLD) {
        try {
          Thread.sleep(ms);
        } catch (final InterruptedException e) {
          // ok
        }
      } else {
        final long s = System.nanoTime() + 1000L * ms;
        while (System.nanoTime() < s) {
          Thread.yield();
        }
      }
    }
  }

  /**
   * Sleep for a short period of time. Nominally a tenth of a second.
   */
  public static void shortSleep() {
    final int time = ChaosProperties.properties().getIntProperty("chaos.short.sleep", 100);
    if (time > 0) {
      sleep(time);
    }
  }
}
