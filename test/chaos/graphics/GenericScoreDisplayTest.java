package chaos.graphics;

import chaos.Configuration;
import chaos.board.WizardManager;
import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.State;
import chaos.common.wizard.Wizard;
import chaos.util.ChaosProperties;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class GenericScoreDisplayTest extends TestCase {

  private static class MyMockGraphics extends MockGraphics {
    @Override
    public void fillPolygon(final int[] xPoints, final int[] yPoints, final int nPoints) {
      mHistory.append("fillPolygon(").append(nPoints).append(")[");
      for (int k = 0; k < nPoints; ++k) {
        mHistory.append('(').append(xPoints[k]).append(',').append(yPoints[k]).append(')');
      }
      mHistory.append(']');
    }
  }

  public void test1() {
    ChaosProperties.properties().setProperty("chaos.scoredisplay.nohuman.delay", "10");
    final World w = new World(4, 4);
    final MockGraphics mg = new MyMockGraphics();
    final MockScreen screen = new MockScreen(mg);
    final WizardManager wm = w.getWizardManager();
    int k = 100;
    for (int j = 1; j <= 10; ++j) {
      final Wizard z = wm.getWizard(j);
      z.setPersonalName("Wiz" + k);
      z.setState(State.ACTIVE);
      z.set(Attribute.LIFE, k);
      k -= 10;
      w.getCell(j).push(z);
    }
    final Wizard indp = wm.getIndependent();
    indp.setState(State.ACTIVE);
    //    indp.setPersonalName("Independents");
    final Configuration config = new Configuration();
    final GenericScoreDisplay d = new GenericScoreDisplay(config, w, screen);
    d.showScores(42);
    for (int j = 1; j <= 10; ++j) {
      final Wizard z = wm.getWizard(j);
      z.set(Attribute.LIFE, k);
      k += 10;
    }
    d.showScores(43);
    final String x = screen.toString();
    assertTrue(x, x.startsWith("addMouseListener(.)#addKeyListener(.)#removeKeyListener(.)#removeMouseListener(.)#addMouseListener(.)#addKeyListener(.)#removeKeyListener(.)#removeMouseListener(.)#|I(0,0,null)#setFont()#getFontMetrics()#drawString(Current Standings"));
    assertTrue(x.contains("#drawString(Wiz50"));
    assertTrue(x.contains("#setColor(java.awt.Color[r=0,g=255,b=0])#getFontMetrics()#drawString(80"));
    assertTrue(x.contains("#fillPolygon(3)["));
  }
}
