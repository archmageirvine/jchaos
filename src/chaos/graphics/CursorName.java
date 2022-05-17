package chaos.graphics;

import java.awt.Component;

/**
 * Cursor names.
 *
 * @author Sean A. Irvine
 */
public enum CursorName {
  /** Blank cursor. */
  BLANK,
  /** Ranged combat cursor. */
  SHOOT,
  /** Flying movement cursor. */
  WINGS,
  /** Spell casting cursor. */
  CAST,
  /** Movement and selection cursor. */
  CROSS,
  /** Dismounting cursor. */
  DISMOUNT;

  /**
   * Set the cursor for this component.
   * @param c component
   */
  public void setCursor(final Component c) {
    switch (this) {
    case WINGS:
      Cursors.setWingsCursor(c);
      break;
    case CROSS:
      Cursors.setCrossCursor(c);
      break;
    case SHOOT:
      Cursors.setShootCursor(c);
      break;
    case BLANK:
      Cursors.setBlankCursor(c);
      break;
    case CAST:
      Cursors.setCastCursor(c);
      break;
    case DISMOUNT:
      Cursors.setDismountCursor(c);
      break;
    default:
      break;
    }
  }
}
