package chaos.common;

/**
 * Implemented by actors which can be animated by the animate spell.
 * @author Sean A. Irvine
 */
public interface Animateable {

  /**
   * Return the actor that results from the animate spell applied to this
   * actor.
   * @return animate replacement
   */
  Actor getAnimatedForm();
}
