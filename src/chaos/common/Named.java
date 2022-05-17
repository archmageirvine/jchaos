package chaos.common;

/**
 * A creature worthy of a name. Such creatures usually also gain experience.
 *
 * @author Sean A. Irvine
 */
public interface Named {

  /**
   * Name of this individual.
   * @return name of this individual
   */
  String getPersonalName();

  /**
   * Name of this individual.
   * @param name of this individual
   */
  void setPersonalName(final String name);
}
