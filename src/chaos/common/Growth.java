package chaos.common;

import chaos.board.World;

/**
 * The growths in Chaos are modelled on what happens to bacteria in a
 * petri dish with an abundant food source.  In such situations the
 * bacteria exhibit exponentially growth, doubling in size for each unit
 * of time.<p>
 *
 * All expansion of growths occurs during an update phase.  Each growth
 * cell is examined in normal reading order.  There is a certain chance
 * that a given growth will expand.  If it does expand then it will do so
 * into at most one adjacent cell.  If the growth fails to expand then
 * there is a chance that the growth will spontaneously die (unless the
 * growth is fungi, earthquake, or a growable creature which never
 * spontaneously die).<p>
 *
 * Different growths expand at different rates.  Orange jelly is
 * particularly voracious expanding at nearly every opportunity.  Fungi
 * is very reserved only rarely expanding.  The rate associated with a
 * growth is not exactly the probability it will expand, but rather the
 * probability that it will attempt to expand.  Expansion might still
 * fail for a number of reasons: the chosen cell to expand into might be
 * off the side of the board, might contain something which cannot be
 * grown over, or the cell contents might defend against the growth
 * either in combat or because of a special shield.<p>
 *
 * Several different growth strategies have been coded over the years.
 * The system used in Total Chaos is complicated mesh of numbers which
 * don't correspond to much in the way of real probabilities, but which
 * nevertheless yield a satisfactory system.  Part of difficulty in Total
 * Chaos is that the probability a growth dies is roughly (1-p)/2 where p
 * is the probability it will expand.  This arises because Total Chaos
 * kills a cell half the time when it does not expand.  This means that
 * if p is low (&lt;50%) to start with then growths have a high probability
 * of dying out altogether.  To compensate Total Chaos makes a
 * complicated adjustment to the probability based on the number of
 * growth cells on the screen.<p>
 *
 * The original idea was that cells should die at roughly half the rate
 * which they expand; but the math in Total Chaos is broken for small p.
 * By simply changing the death probability from 1/2 to p/2 the
 * computation can be corrected to work for all p.  This one change
 * essentially eliminates the need for any corrective factors based on
 * the number of growths currently alive, thereby simplifying the code
 * and saving time.<p>
 *
 * In Chaos the growth rate is specified as a true probability.  For any
 * growth <code>g</code> the value <code>g.growthRate()/100.0</code> is
 * the probability that <code>g</code> will
 * attempt to expand each turn.  Thus, choosing the growth rate for each
 * type of growth is an important consideration.  The following graph is
 * useful to determine a suitable growth rate.  It shows the expected
 * number of turns required to at least half fill the board with growths.
 * Each point is based on 1000 trials with the initial growth placed in a
 * random cell.<p>
 *
 * <img src="doc-files/coverage.gif" alt="coverage graph"><p>
 *
 * The other important number is how often the growth will die out:<p>
 *
 * <img src="doc-files/death.gif" alt="death graph"><p>
 *
 * The final graph shows how the number of growth cells varies with time
 * for a few different p assuming the growth has not died out.<p>
 *
 * <img src="doc-files/count.gif" alt="count graph"><p>
 *
 * Actual growing is performed by the Grower class.
 *
 * @author Sean A. Irvine
 */
public interface Growth extends TargetFilter {

  /** Growth which will cover other objects and nothing will check its expansion. */
  int GROW_OVER = 0;
  /** Growth which expands by combat. */
  int GROW_BY_COMBAT = 1;
  /** Growth which expands only in four directions (like the Earthquake). */
  int GROW_FOUR_WAY = 2;

  /**
   * Grow this growth (if possible).
   * @param cell the cell containing this growth
   * @param world world to grow in
   */
  void grow(int cell, World world);

  /**
   * Return the type of this growth, either GROW_OVER, GROW_BY_COMBAT, or GROW_FOUR_WAY.
   * @return growth type
   */
  int getGrowthType();

  /**
   * Return the rate of growth.  The rate must be in the range 1 to 100
   * and should generally be bigger than about 10.  The rate is the
   * percentage chance that a growth will expand on a given turn.  A value
   * of 100 means the growth will attempt expansion every turn, a value of
   * 50 once every two turns, and a value of 25 once every four turns
   * on average.
   *
   * @return growth rate
   */
  int growthRate();

  /**
   * What type of growth comes from this growth (usually the same type).
   * @return the sprout type
   */
  Class<? extends Actor> sproutClass();
}
