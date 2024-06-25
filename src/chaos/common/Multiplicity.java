package chaos.common;

/**
 * Indicates multiplicity in casting.
 * @author Sean A. Irvine
 */
public interface Multiplicity {

  /**
   * Return the multiplicity for this spell. This is the number of instances
   * which should be granted.
   * @return multiplicity
   */
  int getMultiplicity();
}
