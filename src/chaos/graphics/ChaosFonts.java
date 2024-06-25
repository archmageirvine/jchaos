package chaos.graphics;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Fonts.
 * @author Sean A. Irvine
 */
public class ChaosFonts {

  /** Font for phase. */
  private final Font mPhaseFont;
  /** Fonts used for the information display. */
  private final Font mTextFont;
  private final Font mHeadFont;
  private final Font mMonospaceFont;

  ChaosFonts(final int cellWidth) {
    final double scale = cellWidth / 16.0;
    mPhaseFont = new Font("SansSerif", Font.PLAIN, (int) (9 * scale));
    mMonospaceFont = new Font("Monospaced", Font.PLAIN, (int) (20.0 * scale));
    try {
      try (final InputStream is = ChaosFonts.class.getClassLoader().getResourceAsStream("chaos/resources/fonts/info.ttf")) {
        final Font x = Font.createFont(Font.TRUETYPE_FONT, is);
        mTextFont = x.deriveFont((float) (20.0 * scale));
      }
      try (final InputStream is = ChaosFonts.class.getClassLoader().getResourceAsStream("chaos/resources/fonts/gothic.ttf")) {
        final Font x = Font.createFont(Font.TRUETYPE_FONT, is);
        mHeadFont = x.deriveFont((float) (30.0 * scale));
      }
    } catch (final IOException | FontFormatException e) {
      // in theory should never happen unless archive is corrupt
      throw new RuntimeException(e);
    }
  }

  /**
   * Get the font used for writing the phase messages.
   * @return phase font
   */
  public Font getPhaseFont() {
    return mPhaseFont;
  }

  /**
   * Get the font for showing titles.
   * @return title font
   */
  public Font getTitleFont() {
    return mHeadFont;
  }

  /**
   * Get the font for showing text.
   * @return text font
   */
  public Font getTextFont() {
    return mTextFont;
  }

  public Font getMonospaceFont() {
    return mMonospaceFont;
  }
}
