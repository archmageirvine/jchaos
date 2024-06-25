package chaos.graphics;

import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class ChaosFontsTest extends TestCase {

  public void test() {
    final ChaosFonts fonts = new ChaosFonts(16);
    assertNotNull(fonts.getPhaseFont());
    assertNotNull(fonts.getTitleFont());
    assertNotNull(fonts.getTextFont());
    assertEquals("SansSerif.plain", fonts.getPhaseFont().getFontName());
    assertEquals("AnglicanText", fonts.getTitleFont().getFontName());
    assertEquals("Carolingia", fonts.getTextFont().getFontName());
    assertEquals(9, fonts.getPhaseFont().getSize());
    assertEquals(30, fonts.getTitleFont().getSize());
    assertEquals(20, fonts.getTextFont().getSize());
  }

}
