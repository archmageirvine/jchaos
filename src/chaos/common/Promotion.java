package chaos.common;

/**
 * This interface indicates the Actor can be promoted after the
 * specified number of kills.
 * @author Sean A. Irvine
 */
public interface Promotion {

  /**
   * The Actor this Actor will promote to.
   * @return class of promotion
   */
  Class<? extends Actor> promotion();

  /**
   * Number of kills needed to achieve promotion.
   * @return kill count
   */
  int promotionCount();
}
