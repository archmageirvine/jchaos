package chaos.selector;

import java.io.Serializable;

import chaos.common.Castable;

/**
 * The interface used by a wizard to perform spell selection.
 * Defines a method which is called with the set of
 * castables from which the selection is to be made.
 * @author Sean A. Irvine
 */
public interface Selector extends Serializable {

  /**
   * Select bonus castables.  At most <code>count</code> castables are to
   * selected from <code>castables</code>.  If no castables are selected
   * an array of length 0 should be returned.  The supplied <code>castables</code>
   * should all be non-null.  The supplied array must be at least the
   * same length as count.  The <code>count</code> parameter should be
   * nonnegative.
   * @param castables castables to select from
   * @param count maximum number of castables to select
   * @return selected castables
   */
  Castable[] selectBonus(Castable[] castables, int count);

  /**
   * Select one or two castables. The result is always an
   * array of length 2, but one or other entry can be null.  If a
   * result is non-null then it must corresponding to one
   * of the entries in the supplied array.  All the array entries
   * may be assumed to be non-null.  Implementations may throw a
   * RuntimeException is the array contains more entries than the
   * implementation is prepared to deal with.
   * @param castables castables to choose from
   * @param texas true if Texas mode is active
   * @return chosen castables
   */
  Castable[] select(Castable[] castables, boolean texas);
}
