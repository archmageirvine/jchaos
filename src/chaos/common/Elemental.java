package chaos.common;

/**
 * An interface implemented by elementals.  Elementals do not follow the normal
 * death sequence, but instead transform into an inanimate type on death.  For
 * example, the air elemental becomes a tempest.  This interface defines a
 * single method which gives the replacement object on death.
 *
 * @author Sean A. Irvine
 */
public interface Elemental {

  /**
   * Return an actor that can be used as a substitute for this elemental when
   * the elemental is killed.
   *
   * @return replacement for the elemental
   */
  Actor getElementalReplacement();
}
