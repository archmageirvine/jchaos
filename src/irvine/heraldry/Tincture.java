package irvine.heraldry;

import java.awt.Color;

/**
 * Tinctures.
 * @author Sean A. Irvine
 */
public enum Tincture {

  /** Or (gold). */
  OR(new Color(255, 215, 0)),
  /** Argent (white). */
  ARGENT(Color.WHITE),
  /** Azure (blue). */
  AZURE(Color.BLUE),
  /** Gules (red). */
  GULES(Color.RED),
  /** Purpure (purple). */
  PURPURE(new Color(160, 32, 240)),
  /** Sable (black). */
  SABLE(Color.BLACK),
  /** Vert (green). */
  VERT(Color.GREEN),
  /** Murrey (blood-red). */
  MURREY(new Color(188, 143, 143)),
  /** Tenne (tawny-orange). */
  TENNE(new Color(255, 140, 0));

  private final Color mColor;

  Tincture(final Color c) {
    mColor = c;
  }

  /**
   * Color associated with this tincture.
   * @return color
   */
  public Color color() {
    return mColor;
  }
}
