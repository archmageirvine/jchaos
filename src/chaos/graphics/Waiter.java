package chaos.graphics;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

/**
 * Provides a simple waiting mechanism for the screen.
 * @author Sean A. Irvine
 */
final class Waiter {

  private Waiter() {
  }

  private static class MyRunnable implements Runnable {
    @Override
    public void run() {
    }
  }

  static void waitUntilDisplayIsReady() {
    try {
      SwingUtilities.invokeAndWait(new MyRunnable());
    } catch (final InterruptedException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

}
