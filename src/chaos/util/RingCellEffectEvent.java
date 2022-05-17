package chaos.util;

import java.awt.Color;

/**
 * Ring centered on a cell.
 * @author Sean A. Irvine
 */
public class RingCellEffectEvent extends CellEffectEvent {

  private final int mRadius;

  private final Color mColor;

  /**
   * Construct a new attack cell effect event.
   *
   * @param cell the cell this event is for
   * @param radius the radius of the effect in cells
   * @param color color to use
   */
  public RingCellEffectEvent(final int cell, final int radius, final Color color) {
    super(cell, CellEffectType.RING);
    mRadius = radius;
    mColor = color;
  }

  /**
   * The radius of the ring in cells.
   * @return radius
   */
  public int getRadius() {
    return mRadius;
  }

  /**
   * The colour of the ring.
   * @return color
   */
  public Color getColor() {
    return mColor;
  }

}
