package chaos.graphics;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.State;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class WizardExplodeEffectTest extends TestCase {

  public void test1() {
    final World w = new World(5, 4);
    final TileManager tm = TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE16);
    final MockScreen screen = new MockScreen();
    final Animator a = new Animator(w, screen, tm);
    final WizardExplodeEffect se = new WizardExplodeEffect(w, a, tm);
    se.performEffect(screen, screen.getGraphics(), (Cell) null, 16);
    assertEquals("fillMain(java.awt.Color[r=0,g=0,b=0])#", screen.toString());
    se.performEffect(screen, screen.getGraphics(), w.getCell(7), 16);
    assertEquals("fillMain(java.awt.Color[r=0,g=0,b=0])#", screen.toString());
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    w.getCell(7).push(wiz);
    final Lion l = new Lion();
    l.setState(State.ASLEEP);
    w.getCell(8).push(l);
    w.getCell(9).push(new Lion());
    se.performEffect(screen, screen.getGraphics(), w.getCell(7), 16);
    final String str = screen.toString();
    assertTrue(str, str.startsWith("fillMain(java.awt.Color[r=0,g=0,b=0])#drawCell(1,1)#drawCell(3,1)#drawCell(2,0)#drawCell(2,2)#drawCell(0,1)#drawCell(4,1)#drawCell(4,1)#drawCell(2,3)#|I(24,16,null)#I(40,16,null)#I(32,8,null)#I(32,24,null)#I(16,16,null)#I(48,16,null)#I(32,0,null)#I(32,32,null)#I(8,16,null)#I(56,16,null)#I(32,40,null)#I(0,16,null)#I(64,16,null)#I(32,48,null)"));
  }

}
