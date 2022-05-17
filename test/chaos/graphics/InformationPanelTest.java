package chaos.graphics;

import junit.framework.TestCase;
import chaos.common.free.Arborist;
import chaos.common.free.IrvinesInvulnerability;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class InformationPanelTest extends TestCase {

  public void testEmpty() {
    final MockScreen s = new MockScreen();
    InformationPanel.informationDisplay(s, null, s.getGraphics());
    assertEquals("", s.toString());
  }

  public void testArborist() {
    final MockScreen s = new MockScreen();
    InformationPanel.informationDisplay(s, new Arborist(), s.getGraphics());
    final String x = s.toString();
    assertTrue(x.startsWith("|I(0,0,null)#I("));
    assertTrue(x.contains("#setFont()#getFontMetrics()#drawString(Special,"));
    assertTrue(x.contains("#drawString(cultivation,"));
  }

  public void testII() {
    final MockScreen s = new MockScreen();
    InformationPanel.informationDisplay(s, new IrvinesInvulnerability(), s.getGraphics());
    final String x = s.toString();
    assertTrue(x.startsWith("|I(0,0,null)#I("));
    assertTrue(x.contains("#setColor(java.awt.Color[r=149,g=16,b=16])#setFont()#getFontMetrics()#drawString(Irvine's Invulnerability,"));
    assertTrue(x.contains(")#drawString(nothingness.,"));
  }
}
