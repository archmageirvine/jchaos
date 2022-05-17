package chaos.graphics;

import java.awt.Color;
import java.awt.Graphics;

import chaos.common.Realm;

/**
 * Handles the pentagram.
 * @author Sean A. Irvine
 */
final class PentagramHighlighter {

  /** Color sequence used for multiple realm highlighting. */
  private static final Color[] REALM_COLOR_ORDER = {
    Color.RED,
    Color.BLUE,
    Color.GREEN,
  };

  private final Pentagram mPentagram;
  private final Graphics mGraphics;
  private final int mXPentagramOffset;
  private final int mYPentagramOffset;

  PentagramHighlighter(final int width, final Graphics graphics, final int x, final int y) {
    mPentagram = new Pentagram(width);
    mGraphics = graphics;
    mXPentagramOffset = x;
    mYPentagramOffset = y;
  }

  void highlight(final Realm realm, final Color color, final int dy) {
    if (mGraphics != null) {
      mPentagram.highlightPentagram(mGraphics, realm, color, mXPentagramOffset, dy + mYPentagramOffset);
    }
  }

  void reset() {
    mPentagram.highlightPentagram(mGraphics, null, null, mXPentagramOffset, mYPentagramOffset);
  }

  void highlight(final Realm... realm) {
    if (realm == null) {
      highlight(null, null, 0);
    } else {
      for (int i = 0; i < Math.min(realm.length, REALM_COLOR_ORDER.length); ++i) {
        final int dy = i == 0 || realm[i] != realm[i - 1] ? 0 : 4;
        highlight(realm[i], REALM_COLOR_ORDER[i], dy);
      }
    }
  }
}
