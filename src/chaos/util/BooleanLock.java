package chaos.util;

import java.io.Serializable;

/**
 * <code>BooleanLock</code> handles simple inter-thread locking and
 * synchronization.
 *
 * @author Len Trigg
 * @author Sean A. Irvine
 */
public class BooleanLock implements Serializable {

  private final Object mLock = new Object();

  /** The lock state. */
  private boolean mValue;

  /**
   * Creates a new <code>BooleanLock</code> with a default state of
   * false.
   */
  public BooleanLock() {
    this(false);
  }

  /**
   * Creates a new <code>BooleanLock</code>.
   *
   * @param initialValue a <code>boolean</code> giving the initial
   * state.
   */
  public BooleanLock(final boolean initialValue) {
    mValue = initialValue;
  }

  /**
   * Test if the lock is currently true.
   *
   * @return true if lock is true
   */
  public boolean isTrue() {
    synchronized (mLock) {
      return mValue;
    }
  }

  /**
   * Test if the lock is currently false.
   *
   * @return true if lock is false
   */
  public boolean isFalse() {
    synchronized (mLock) {
      return !mValue;
    }
  }

  /**
   * Set the value of the lock to the specified value.
   *
   * @param newValue new lock value
   */
  public void setValue(final boolean newValue) {
    synchronized (mLock) {
      if (newValue != mValue) {
        mValue = newValue;
        mLock.notifyAll();
      }
    }
  }

  /**
   * Wait on the lock to become false then set it to true.
   *
   * @param msTimeout timeout in milliseconds
   * @return true if waiting was successful
   * @exception InterruptedException if wait was interrupted
   */
  public boolean waitToSetTrue(final long msTimeout) throws InterruptedException {
    synchronized (mLock) {
      final boolean success = waitUntilFalse(msTimeout);
      if (success) {
        setValue(true);
      }
      return success;
    }
  }

  /**
   * Wait on the lock to become true then set it to false.
   *
   * @param msTimeout timeout in milliseconds
   * @return true if waiting was successful
   * @exception InterruptedException if wait was interrupted
   */
  public boolean waitToSetFalse(final long msTimeout) throws InterruptedException {
    synchronized (mLock) {
      final boolean success = waitUntilTrue(msTimeout);
      if (success) {
        setValue(false);
      }
      return success;
    }
  }

  /**
   * Wait until the lock becomes true.
   *
   * @param msTimeout timeout in milliseconds
   * @return true if wait was successful
   * @exception InterruptedException if the wait is interrupted
   */
  public boolean waitUntilTrue(final long msTimeout) throws InterruptedException {
    return waitUntilStateIs(true, msTimeout);
  }

  /**
   * Wait until the lock becomes false.
   *
   * @param msTimeout timeout in milliseconds
   * @return true if wait was successful
   * @exception InterruptedException if the wait is interrupted
   */
  public boolean waitUntilFalse(final long msTimeout) throws InterruptedException {
    return waitUntilStateIs(false, msTimeout);
  }

  /**
   * Wait until the lock enters the specified state.
   *
   * @param state desired lock state
   * @param msTimeout timeout in milliseconds
   * @return true if wait was successful
   * @exception InterruptedException if the wait is interrupted
   */
  public boolean waitUntilStateIs(final boolean state, long msTimeout) throws InterruptedException {
    synchronized (mLock) {
      if (msTimeout == 0L) {
        while (mValue != state) {
          mLock.wait();
        }
        return true;
      }
      final long endTime = System.currentTimeMillis() + msTimeout;
      while (mValue != state && msTimeout > 0L) {
        mLock.wait(msTimeout);
        msTimeout = endTime - System.currentTimeMillis();
      }
      return mValue == state;
    }
  }
}
