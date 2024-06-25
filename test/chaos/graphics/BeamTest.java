package chaos.graphics;

import java.awt.Color;

import chaos.board.World;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class BeamTest extends TestCase {

  public void testTribeamD() {
    final World w = new World(2, 2);
    final MockScreen screen = new MockScreen();
    final Beam d = new Beam(w, screen, screen.getGraphics(), 4);
    d.tribeam(0, w.size() - 1, Color.CYAN, false);
    assertEquals("|setColor(java.awt.Color[r=0,g=255,b=255])#L(-10,-11,-11,-11)#L(-10,-9,-11,-9)#L(8,8,8,8)#L(8,7,7,7)#L(8,9,7,9)#L(9,9,9,9)#L(9,8,8,8)#L(9,10,8,10)#L(10,10,10,10)#L(10,9,9,9)#L(10,11,9,11)#L(11,11,11,11)#L(11,10,10,10)#L(11,12,10,12)#L(12,12,12,12)#L(12,11,11,11)#L(12,13,11,13)#L(13,13,13,13)#L(13,12,12,12)#L(13,14,12,14)#L(14,14,14,14)#L(14,13,13,13)#L(14,15,13,15)#L(15,15,15,15)#L(15,14,14,14)#L(15,16,14,16)#L(16,16,16,16)#L(16,15,15,15)#L(16,17,15,17)#L(17,17,17,17)#L(17,16,16,16)#L(17,18,16,18)#L(18,18,18,18)#L(18,17,17,17)#L(18,19,17,19)#L(19,19,19,19)#L(19,18,18,18)#L(19,20,18,20)#L(20,20,20,20)#L(20,19,19,19)#L(20,21,19,21)#L(21,21,21,21)#L(21,20,20,20)#L(21,22,20,22)#L(22,22,22,22)#L(22,21,21,21)#L(22,23,21,23)#L(23,23,23,23)#setColor(java.awt.Color[r=0,g=0,b=0])#L(23,22,22,22)#L(23,24,22,24)#L(8,8,8,8)#L(8,7,7,7)#L(8,9,7,9)#L(9,9,9,9)#L(9,8,8,8)#L(9,10,8,10)#L(10,10,10,10)#L(10,9,9,9)#L(10,11,9,11)#L(11,11,11,11)#L(11,10,10,10)#L(11,12,10,12)#L(12,12,12,12)#L(12,11,11,11)#L(12,13,11,13)#L(13,13,13,13)#L(13,12,12,12)#L(13,14,12,14)#L(14,14,14,14)#L(14,13,13,13)#L(14,15,13,15)#L(15,15,15,15)#L(15,14,14,14)#L(15,16,14,16)#L(16,16,16,16)#L(16,15,15,15)#L(16,17,15,17)#L(17,17,17,17)#L(17,16,16,16)#L(17,18,16,18)#L(18,18,18,18)#L(18,17,17,17)#L(18,19,17,19)#L(19,19,19,19)#L(19,18,18,18)#L(19,20,18,20)#L(20,20,20,20)#L(20,19,19,19)#L(20,21,19,21)#L(21,21,21,21)#L(21,20,20,20)#L(21,22,20,22)#L(22,22,22,22)#L(22,21,21,21)#L(22,23,21,23)#L(23,23,23,23)#", screen.toString());
  }

  public void testTribeamH() {
    final World w = new World(2, 2);
    final MockScreen screen = new MockScreen();
    final Beam d = new Beam(w, screen, screen.getGraphics(), 4);
    d.tribeam(1, 0, Color.CYAN, false);
    assertEquals("|setColor(java.awt.Color[r=0,g=255,b=255])#L(-10,-11,-9,-11)#L(-10,-9,-9,-9)#L(23,7,23,7)#L(23,6,24,6)#L(23,8,24,8)#L(22,7,22,7)#L(22,6,23,6)#L(22,8,23,8)#L(21,7,21,7)#L(21,6,22,6)#L(21,8,22,8)#L(20,7,20,7)#L(20,6,21,6)#L(20,8,21,8)#L(19,7,19,7)#L(19,6,20,6)#L(19,8,20,8)#L(18,7,18,7)#L(18,6,19,6)#L(18,8,19,8)#L(17,7,17,7)#L(17,6,18,6)#L(17,8,18,8)#L(16,7,16,7)#L(16,6,17,6)#L(16,8,17,8)#L(15,7,15,7)#L(15,6,16,6)#L(15,8,16,8)#L(14,7,14,7)#L(14,6,15,6)#L(14,8,15,8)#L(13,7,13,7)#L(13,6,14,6)#L(13,8,14,8)#L(12,7,12,7)#L(12,6,13,6)#L(12,8,13,8)#L(11,7,11,7)#L(11,6,12,6)#L(11,8,12,8)#L(10,7,10,7)#L(10,6,11,6)#L(10,8,11,8)#L(9,7,9,7)#L(9,6,10,6)#L(9,8,10,8)#L(8,7,8,7)#setColor(java.awt.Color[r=0,g=0,b=0])#L(8,6,9,6)#L(8,8,9,8)#L(23,7,23,7)#L(23,6,24,6)#L(23,8,24,8)#L(22,7,22,7)#L(22,6,23,6)#L(22,8,23,8)#L(21,7,21,7)#L(21,6,22,6)#L(21,8,22,8)#L(20,7,20,7)#L(20,6,21,6)#L(20,8,21,8)#L(19,7,19,7)#L(19,6,20,6)#L(19,8,20,8)#L(18,7,18,7)#L(18,6,19,6)#L(18,8,19,8)#L(17,7,17,7)#L(17,6,18,6)#L(17,8,18,8)#L(16,7,16,7)#L(16,6,17,6)#L(16,8,17,8)#L(15,7,15,7)#L(15,6,16,6)#L(15,8,16,8)#L(14,7,14,7)#L(14,6,15,6)#L(14,8,15,8)#L(13,7,13,7)#L(13,6,14,6)#L(13,8,14,8)#L(12,7,12,7)#L(12,6,13,6)#L(12,8,13,8)#L(11,7,11,7)#L(11,6,12,6)#L(11,8,12,8)#L(10,7,10,7)#L(10,6,11,6)#L(10,8,11,8)#L(9,7,9,7)#L(9,6,10,6)#L(9,8,10,8)#L(8,7,8,7)#", screen.toString());
  }

  public void testTribeamV() {
    final World w = new World(2, 2);
    final MockScreen screen = new MockScreen();
    final Beam d = new Beam(w, screen, screen.getGraphics(), 4);
    d.tribeam(2, 0, Color.CYAN, false);
    assertEquals("|setColor(java.awt.Color[r=0,g=255,b=255])#L(-9,-10,-9,-9)#L(-11,-10,-11,-9)#L(7,23,7,23)#L(8,23,8,24)#L(6,23,6,24)#L(7,22,7,22)#L(8,22,8,23)#L(6,22,6,23)#L(7,21,7,21)#L(8,21,8,22)#L(6,21,6,22)#L(7,20,7,20)#L(8,20,8,21)#L(6,20,6,21)#L(7,19,7,19)#L(8,19,8,20)#L(6,19,6,20)#L(7,18,7,18)#L(8,18,8,19)#L(6,18,6,19)#L(7,17,7,17)#L(8,17,8,18)#L(6,17,6,18)#L(7,16,7,16)#L(8,16,8,17)#L(6,16,6,17)#L(7,15,7,15)#L(8,15,8,16)#L(6,15,6,16)#L(7,14,7,14)#L(8,14,8,15)#L(6,14,6,15)#L(7,13,7,13)#L(8,13,8,14)#L(6,13,6,14)#L(7,12,7,12)#L(8,12,8,13)#L(6,12,6,13)#L(7,11,7,11)#L(8,11,8,12)#L(6,11,6,12)#L(7,10,7,10)#L(8,10,8,11)#L(6,10,6,11)#L(7,9,7,9)#L(8,9,8,10)#L(6,9,6,10)#L(7,8,7,8)#setColor(java.awt.Color[r=0,g=0,b=0])#L(8,8,8,9)#L(6,8,6,9)#L(7,23,7,23)#L(8,23,8,24)#L(6,23,6,24)#L(7,22,7,22)#L(8,22,8,23)#L(6,22,6,23)#L(7,21,7,21)#L(8,21,8,22)#L(6,21,6,22)#L(7,20,7,20)#L(8,20,8,21)#L(6,20,6,21)#L(7,19,7,19)#L(8,19,8,20)#L(6,19,6,20)#L(7,18,7,18)#L(8,18,8,19)#L(6,18,6,19)#L(7,17,7,17)#L(8,17,8,18)#L(6,17,6,18)#L(7,16,7,16)#L(8,16,8,17)#L(6,16,6,17)#L(7,15,7,15)#L(8,15,8,16)#L(6,15,6,16)#L(7,14,7,14)#L(8,14,8,15)#L(6,14,6,15)#L(7,13,7,13)#L(8,13,8,14)#L(6,13,6,14)#L(7,12,7,12)#L(8,12,8,13)#L(6,12,6,13)#L(7,11,7,11)#L(8,11,8,12)#L(6,11,6,12)#L(7,10,7,10)#L(8,10,8,11)#L(6,10,6,11)#L(7,9,7,9)#L(8,9,8,10)#L(6,9,6,10)#L(7,8,7,8)#", screen.toString());
  }

  public void testTribeamSkinny() {
    final World w = new World(2, 4);
    final MockScreen screen = new MockScreen();
    final Beam d = new Beam(w, screen, screen.getGraphics(), 1);
    d.tribeam(2, w.size() - 1, Color.CYAN, false);
    assertEquals("|setColor(java.awt.Color[r=0,g=255,b=255])#L(-9,-10,-10,-10)#L(-11,-10,-10,-10)#L(1,3,1,3)#L(2,3,1,3)#L(0,3,1,3)#L(1,4,1,4)#L(2,4,1,4)#L(0,4,1,4)#L(2,5,2,5)#L(3,5,2,5)#L(1,5,2,5)#L(2,6,2,6)#setColor(java.awt.Color[r=0,g=0,b=0])#L(3,6,2,6)#L(1,6,2,6)#L(1,3,1,3)#L(2,3,1,3)#L(0,3,1,3)#L(1,4,1,4)#L(2,4,1,4)#L(0,4,1,4)#L(2,5,2,5)#L(3,5,2,5)#L(1,5,2,5)#L(2,6,2,6)#", screen.toString());
  }

  public void testBreathWeapon() {
    final World w = new World(2, 2);
    final MockScreen screen = new MockScreen();
    final Beam d = new Beam(w, screen, screen.getGraphics(), 4);
    d.breathWeapon(0, w.size() - 1, Color.RED);
    final String s = screen.toString();
    assertEquals("|setColor(java.awt.Color[r=255,g=0,b=0])#L(8,8,8,8)#setColor(java.awt.Color[r=255,g=0,b=0])#L(9,8,9,8)#setColor(java.awt.Color[r=255,g=0,b=0])#L(9,9,9,9)#setColor(java.awt.Color[r=255,g=0,b=0])#L(7,9,7,9)#setColor(java.awt.Color[r=255,g=0,b=0])#L(5,10,5,10)#setColor(java.awt.Color[r=255,g=0,b=0])#L(7,11,7,11)#setColor(java.awt.Color[r=255,g=0,b=0])#L(9,11,9,11)#setColor(java.awt.Color[r=255,g=0,b=0])#L(10,9,10,9)#setColor(java.awt.Color[r=255,g=0,b=0])#L(10,7,10,7)#setColor(java.awt.Color[r=255,g=0,b=0])#L(8,6,8,6)#setColor(java.awt.Color[r=255,g=0,b=0])#L(6,6,6,6)#setColor(java.awt.Color[r=255,g=0,b=0])#L(5,8,5,8)#setColor(java.awt.Color[r=255,g=0,b=0])#L(13,13,13,13)#setColor(java.awt.Color[r=255,g=0,b=0])#L(14,13,14,13)#setColor(java.awt.Color[r=255,g=0,b=0])#L(14,14,14,14)#setColor(java.awt.Color[r=255,g=0,b=0])#L(12,14,12,14)#setColor(java.awt.Color[r=255,g=0,b=0])#L(10,15,10,15)#setColor(java.awt.Color[r=255,g=0,b=0])#L(12,16,12,16)#setColor(java.awt.Color[r=255,g=0,b=0])#L(14,16,14,16)#setColor(java.awt.Color[r=255,g=0,b=0])#L(15,14,15,14)#setColor(java.awt.Color[r=255,g=0,b=0])#L(15,12,15,12)#setColor(java.awt.Color[r=255,g=0,b=0])#L(13,11,13,11)#setColor(java.awt.Color[r=255,g=0,b=0])#L(11,11,11,11)#setColor(java.awt.Color[r=255,g=0,b=0])#L(10,13,10,13)#setColor(java.awt.Color[r=255,g=0,b=0])#L(18,18,18,18)#setColor(java.awt.Color[r=255,g=0,b=0])#L(19,18,19,18)#setColor(java.awt.Color[r=255,g=0,b=0])#L(19,19,19,19)#setColor(java.awt.Color[r=255,g=0,b=0])#L(17,19,17,19)#setColor(java.awt.Color[r=255,g=0,b=0])#L(15,20,15,20)#setColor(java.awt.Color[r=255,g=0,b=0])#L(17,21,17,21)#setColor(java.awt.Color[r=255,g=0,b=0])#L(19,21,19,21)#setColor(java.awt.Color[r=255,g=0,b=0])#L(20,19,20,19)#setColor(java.awt.Color[r=255,g=0,b=0])#L(20,17,20,17)#setColor(java.awt.Color[r=255,g=0,b=0])#L(18,16,18,16)#setColor(java.awt.Color[r=255,g=0,b=0])#L(16,16,16,16)#setColor(java.awt.Color[r=255,g=0,b=0])#L(15,18,15,18)#setColor(java.awt.Color[r=255,g=0,b=0])#L(23,23,23,23)#setColor(java.awt.Color[r=255,g=0,b=0])#L(24,23,24,23)#setColor(java.awt.Color[r=255,g=0,b=0])#L(24,24,24,24)#setColor(java.awt.Color[r=255,g=0,b=0])#L(22,24,22,24)#setColor(java.awt.Color[r=255,g=0,b=0])#L(20,25,20,25)#setColor(java.awt.Color[r=255,g=0,b=0])#L(22,26,22,26)#setColor(java.awt.Color[r=255,g=0,b=0])#L(24,26,24,26)#setColor(java.awt.Color[r=255,g=0,b=0])#L(25,24,25,24)#setColor(java.awt.Color[r=255,g=0,b=0])#L(25,22,25,22)#setColor(java.awt.Color[r=255,g=0,b=0])#L(23,21,23,21)#setColor(java.awt.Color[r=255,g=0,b=0])#L(21,21,21,21)#setColor(java.awt.Color[r=255,g=0,b=0])#L(20,23,20,23)#", s);
  }

  public void testBoltWeapon() {
    final World w = new World(2, 2);
    final MockScreen screen = new MockScreen();
    final Beam d = new Beam(w, screen, screen.getGraphics(), 4);
    d.boltWeapon(0, w.size() - 1, Color.RED);
    assertEquals("|setColor(java.awt.Color[r=255,g=0,b=0])#L(8,8,8,8)#L(7,9,7,9)#L(7,11,7,11)#L(9,10,9,10)#L(10,8,10,8)#L(8,6,8,6)#L(6,7,6,7)#L(5,9,5,9)#setColor(java.awt.Color[r=0,g=0,b=0])#L(8,8,8,8)#L(7,9,7,9)#L(7,11,7,11)#L(9,10,9,10)#L(10,8,10,8)#L(8,6,8,6)#L(6,7,6,7)#L(5,9,5,9)#setColor(java.awt.Color[r=255,g=0,b=0])#L(12,12,12,12)#L(11,13,11,13)#L(11,15,11,15)#L(13,14,13,14)#L(14,12,14,12)#L(12,10,12,10)#L(10,11,10,11)#L(9,13,9,13)#setColor(java.awt.Color[r=0,g=0,b=0])#L(12,12,12,12)#L(11,13,11,13)#L(11,15,11,15)#L(13,14,13,14)#L(14,12,14,12)#L(12,10,12,10)#L(10,11,10,11)#L(9,13,9,13)#setColor(java.awt.Color[r=255,g=0,b=0])#L(16,16,16,16)#L(15,17,15,17)#L(15,19,15,19)#L(17,18,17,18)#L(18,16,18,16)#L(16,14,16,14)#L(14,15,14,15)#L(13,17,13,17)#setColor(java.awt.Color[r=0,g=0,b=0])#L(16,16,16,16)#L(15,17,15,17)#L(15,19,15,19)#L(17,18,17,18)#L(18,16,18,16)#L(16,14,16,14)#L(14,15,14,15)#L(13,17,13,17)#setColor(java.awt.Color[r=255,g=0,b=0])#L(20,20,20,20)#L(19,21,19,21)#L(19,23,19,23)#L(21,22,21,22)#L(22,20,22,20)#L(20,18,20,18)#L(18,19,18,19)#L(17,21,17,21)#", screen.toString());
  }

  public void testBirdShit() {
    final World w = new World(2, 2);
    final MockScreen screen = new MockScreen();
    final Beam d = new Beam(w, screen, screen.getGraphics(), 4);
    d.birdShit(0, w.size() - 1, Color.RED);
    assertEquals("|setColor(java.awt.Color[r=255,g=0,b=0])#fillOval(8,8,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(8,8,6,6)#setColor(java.awt.Color[r=255,g=0,b=0])#fillOval(9,9,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(9,9,6,6)#setColor(java.awt.Color[r=255,g=0,b=0])#fillOval(10,10,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(10,10,6,6)#setColor(java.awt.Color[r=255,g=0,b=0])#fillOval(11,11,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(11,11,6,6)#setColor(java.awt.Color[r=255,g=0,b=0])#fillOval(12,12,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(12,12,6,6)#setColor(java.awt.Color[r=255,g=0,b=0])#fillOval(13,13,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(13,13,6,6)#setColor(java.awt.Color[r=255,g=0,b=0])#fillOval(14,14,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(14,14,6,6)#setColor(java.awt.Color[r=255,g=0,b=0])#fillOval(15,15,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(15,15,6,6)#setColor(java.awt.Color[r=255,g=0,b=0])#fillOval(16,16,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(16,16,6,6)#setColor(java.awt.Color[r=255,g=0,b=0])#fillOval(17,17,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(17,17,6,6)#setColor(java.awt.Color[r=255,g=0,b=0])#fillOval(18,18,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(18,18,6,6)#setColor(java.awt.Color[r=255,g=0,b=0])#fillOval(19,19,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(19,19,6,6)#setColor(java.awt.Color[r=255,g=0,b=0])#fillOval(20,20,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(20,20,6,6)#setColor(java.awt.Color[r=255,g=0,b=0])#fillOval(21,21,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(21,21,6,6)#setColor(java.awt.Color[r=255,g=0,b=0])#fillOval(22,22,6,6)#setColor(java.awt.Color[r=0,g=0,b=0])#fillOval(22,22,6,6)#setColor(java.awt.Color[r=255,g=0,b=0])#fillOval(23,23,6,6)#", screen.toString());
  }

}
