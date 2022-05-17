package chaos.common;

/**
 * This interface indicates the Actor is a meditation.
 *
 * @author Sean A. Irvine
 */
public interface Meditation extends Conveyance {

  /**
   * True if this meditation can collapse without being occupied.
   *
   * @return true for free collapsing
   */
  boolean freeCollapse();

  /**
   * The higher the result of this function the less likely the
   * meditation will collapse.  Must be positive.
   *
   * @return collapse factor
   */
  int collapseFactor();
}
