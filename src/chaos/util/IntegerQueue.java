package chaos.util;

import java.util.Arrays;

/**
 * Maintains a simple queue of integers in which the maximum
 * value is known in advance and the same number never appears
 * more than once in the queue.  Uses an array based
 * implementation for speed.
 * @author Sean A. Irvine
 */
public class IntegerQueue {

  /** Pointers to next item in queue. */
  private final int[] mNext;
  /** Head of queue. */
  private int mHead = -1;
  /** Tail of queue. */
  private int mTail;

  /**
   * Make a queue capable of storing the numbers from 0
   * to size-1.
   * @param size size bound
   * @throws IllegalArgumentException if size &lt; 0.
   */
  public IntegerQueue(final int size) {
    if (size < 0) {
      throw new IllegalArgumentException("Size must be nonnegative");
    }
    mNext = new int[size];
    Arrays.fill(mNext, -1);
  }

  /**
   * Adds n to the end of the queue.
   * @param n number to add
   * @throws IllegalArgumentException if n is out of range or
   * n is already in the queue.
   */
  public void enqueue(final int n) {
    if (n < 0 || n >= mNext.length) {
      throw new IllegalArgumentException("n is out of range");
    }
    if (n == mHead || mNext[n] != -1) {
      throw new IllegalArgumentException("n is already in queue");
    }
    if (mHead == -1) {
      // i.e. mTail == -1 as well
      mHead = n;
    } else {
      mNext[mTail] = n;
    }
    mTail = n;
  }

  /**
   * Remove and return the first item in the queue.  If there is
   * nothing presently in the queue then -1 is returned.
   * @return queue <code>mHead</code> or -1
   */
  public int dequeue() {
    final int r = mHead;
    if (mHead != -1) {
      mHead = mNext[mHead];
      mNext[r] = -1;
    }
    return r;
  }
}
