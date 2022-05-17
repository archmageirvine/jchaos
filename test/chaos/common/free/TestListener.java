package chaos.common.free;

import java.util.Arrays;

import junit.framework.Assert;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Event;
import chaos.util.EventListener;
import chaos.util.PolycellEffectEvent;

/**
 * Listener that expects to see particular event types.
 *
 * @author Sean A. Irvine
 */
class TestListener implements EventListener {

  private final CellEffectType[] mTypes;
  private final int[] mCounts;
  private final int mMin;
  private final int mMax;
  private final int mTotal;
  private final StringBuilder mUnexpected = new StringBuilder();


  /**
   * Construct a listener expecting events with specified minimum and
   * maximum counts.
   *
   * @param min minimum count for each event
   * @param max maximum count for each event
   * @param total total number of events expected (-1 to disable).
   * @param types event types
   */
  TestListener(final int min, final int max, final int total, final CellEffectType... types) {
    mTypes = types;
    mCounts = new int[mTypes.length + 1];
    mMin = min;
    mMax = max;
    mTotal = total;
  }

  /**
   * Construct a listener expecting exactly one each of the listed event types.
   * @param types event types
   */
  TestListener(final CellEffectType... types) {
    this(1, 1, -1, types);
  }

  private void update(final CellEffectType t) {
    boolean seen = false;
    for (int k = 0; k < mTypes.length; ++k) {
      if (mTypes[k] == t) {
        mCounts[k]++;
        seen = true;
        break;
      }
    }
    if (!seen) {
      mUnexpected.append(' ').append(t);
      mCounts[mTypes.length]++;
    }
  }

  @Override
  public void update(final Event e) {
    if (e instanceof CellEffectEvent) {
      update(((CellEffectEvent) e).getEventType());
    } else if (e instanceof PolycellEffectEvent) {
      update(((PolycellEffectEvent) e).getEventType());
    }
  }

  /**
   * Check observed events match specified constraints.
   */
  public void checkAndReset() {
    int s = 0;
    for (int k = 0; k < mTypes.length; ++k) {
      final int c = mCounts[k];
      s += c;
      if (c < mMin || c > mMax) {
        Assert.fail("Expected event " + mTypes[k] + " occurred " + c + " times");
      }
    }
    s += mCounts[mTypes.length];
    if (mCounts[mTypes.length] != 0 && mTotal != -2) {
      Assert.fail(mCounts[mTypes.length] + " unexpected events occurred: " + mUnexpected.toString());
    }
    if (mTotal >= 0 && s != mTotal) {
      Assert.fail("Expected " + mTotal + " events but saw " + s);
    }
    Arrays.fill(mCounts, 0);
    mUnexpected.setLength(0);
  }
}
