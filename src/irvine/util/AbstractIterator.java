package irvine.util;

import java.util.Iterator;

/**
 * Implement the annoying <code>remove()</code> method on <code>Iterator</code>.
 * @param <T> type
 * @author Sean A. Irvine
 */
public abstract class AbstractIterator<T> implements Iterator<T> {

  /** Not supported. */
  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
