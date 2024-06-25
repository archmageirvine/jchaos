package chaos.graphics;

import java.awt.Color;

import chaos.board.World;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class ParabolaTest extends TestCase {


  public void testHorizontal1() {
    final World w = new World(5, 4);
    final MockScreen screen = new MockScreen();
    final Parabola p = new Parabola(w, screen, screen.getGraphics(), 4);
    p.parabola(0, 4, Color.YELLOW);
    assertEquals("|setColor(java.awt.Color[r=255,g=255,b=0])#L(8,8,72,8)#", screen.toString());
  }

  public void testHorizontal2() {
    final World w = new World(5, 4);
    final MockScreen screen = new MockScreen();
    final Parabola p = new Parabola(w, screen, screen.getGraphics(), 4);
    p.parabola(4, 0, Color.YELLOW);
    assertEquals("|setColor(java.awt.Color[r=255,g=255,b=0])#L(72,8,8,8)#", screen.toString());
  }

  public void testHorizontal3() {
    final World w = new World(5, 4);
    final MockScreen screen = new MockScreen();
    final Parabola p = new Parabola(w, screen, screen.getGraphics(), 4);
    p.parabola(19, 17, Color.YELLOW);
    assertEquals("|setColor(java.awt.Color[r=255,g=255,b=0])#L(72,56,72,56)#L(72,56,71,53)#L(71,53,70,51)#L(70,51,69,49)#L(69,49,68,47)#L(68,47,67,45)#L(67,45,66,44)#L(66,44,65,43)#L(65,43,64,41)#L(64,41,63,40)#L(63,40,62,39)#L(62,39,61,38)#L(61,38,60,38)#L(60,38,59,37)#L(59,37,58,37)#L(58,37,57,37)#L(57,37,56,37)#L(56,37,55,37)#L(55,37,54,37)#L(54,37,53,37)#L(53,37,52,38)#L(52,38,51,38)#L(51,38,50,39)#L(50,39,49,40)#L(49,40,48,41)#L(48,41,47,43)#L(47,43,46,44)#L(46,44,45,45)#L(45,45,44,47)#L(44,47,43,49)#L(43,49,42,51)#L(42,51,41,53)#L(41,53,41,56)#", screen.toString());
  }

  public void testVertical1() {
    final World w = new World(5, 4);
    final MockScreen screen = new MockScreen();
    final Parabola p = new Parabola(w, screen, screen.getGraphics(), 4);
    p.parabola(0, 15, Color.YELLOW);
    assertEquals("|setColor(java.awt.Color[r=255,g=255,b=0])#L(8,8,8,56)#", screen.toString());
  }

  public void testVertical2() {
    final World w = new World(5, 4);
    final MockScreen screen = new MockScreen();
    final Parabola p = new Parabola(w, screen, screen.getGraphics(), 4);
    p.parabola(4, 19, Color.YELLOW);
    assertEquals("|setColor(java.awt.Color[r=255,g=255,b=0])#L(72,8,72,56)#", screen.toString());
  }

  public void testDiagonal1() {
    final World w = new World(5, 4);
    final MockScreen screen = new MockScreen();
    final Parabola p = new Parabola(w, screen, screen.getGraphics(), 4);
    p.parabola(0, 19, Color.YELLOW);
    assertEquals("|setColor(java.awt.Color[r=255,g=255,b=0])#L(8,8,8,8)#L(8,8,9,5)#L(9,5,10,3)#L(10,3,11,1)#L(11,1,12,0)#L(12,0,13,0)#L(51,0,52,0)#L(52,0,53,1)#L(53,1,54,3)#L(54,3,55,5)#L(55,5,56,8)#L(56,8,57,10)#L(57,10,58,12)#L(58,12,59,15)#L(59,15,60,17)#L(60,17,61,20)#L(61,20,62,23)#L(62,23,63,26)#L(63,26,64,29)#L(64,29,65,32)#L(65,32,66,35)#L(66,35,67,38)#L(67,38,68,41)#L(68,41,69,45)#L(69,45,70,48)#L(70,48,71,52)#L(71,52,71,56)#", screen.toString());
  }

  public void testDiagonal2() {
    final World w = new World(5, 4);
    final MockScreen screen = new MockScreen();
    final Parabola p = new Parabola(w, screen, screen.getGraphics(), 4);
    p.parabola(19, 0, Color.YELLOW);
    assertEquals("|setColor(java.awt.Color[r=255,g=255,b=0])#L(72,56,72,56)#L(72,56,71,52)#L(71,52,70,48)#L(70,48,69,45)#L(69,45,68,41)#L(68,41,67,38)#L(67,38,66,35)#L(66,35,65,32)#L(65,32,64,29)#L(64,29,63,26)#L(63,26,62,23)#L(62,23,61,20)#L(61,20,60,17)#L(60,17,59,15)#L(59,15,58,12)#L(58,12,57,10)#L(57,10,56,8)#L(56,8,55,5)#L(55,5,54,3)#L(54,3,53,1)#L(53,1,52,0)#L(52,0,51,0)#L(13,0,12,0)#L(12,0,11,1)#L(11,1,10,3)#L(10,3,9,5)#L(9,5,9,8)#", screen.toString());
  }

  public void testDiagonal3() {
    final World w = new World(5, 4);
    final MockScreen screen = new MockScreen();
    final Parabola p = new Parabola(w, screen, screen.getGraphics(), 4);
    p.parabola(15, 4, Color.YELLOW);
    assertEquals("|setColor(java.awt.Color[r=255,g=255,b=0])#L(8,56,8,56)#L(8,56,9,52)#L(9,52,10,48)#L(10,48,11,45)#L(11,45,12,41)#L(12,41,13,38)#L(13,38,14,35)#L(14,35,15,32)#L(15,32,16,29)#L(16,29,17,26)#L(17,26,18,23)#L(18,23,19,20)#L(19,20,20,17)#L(20,17,21,15)#L(21,15,22,12)#L(22,12,23,10)#L(23,10,24,8)#L(24,8,25,5)#L(25,5,26,3)#L(26,3,27,1)#L(27,1,28,0)#L(28,0,29,0)#L(67,0,68,0)#L(68,0,69,1)#L(69,1,70,3)#L(70,3,71,5)#L(71,5,71,8)#", screen.toString());
  }

  public void testNoLine() {
    final World w = new World(5, 4);
    final MockScreen screen = new MockScreen();
    final Parabola p = new Parabola(w, screen, screen.getGraphics(), 4);
    p.parabola(0, 0, Color.YELLOW);
    assertEquals("", screen.toString());
  }


  public void testLongSkinny() {
    final World w = new World(2, 30);
    final MockScreen screen = new MockScreen();
    final Parabola p = new Parabola(w, screen, screen.getGraphics(), 4);
    p.parabola(5, 58, Color.YELLOW);
    assertEquals("|setColor(java.awt.Color[r=255,g=255,b=0])#L(24,40,24,40)#L(24,40,23,6)#L(23,6,22,0)#L(16,0,15,28)#L(15,28,14,67)#L(14,67,13,114)#L(13,114,12,169)#L(12,169,11,233)#L(11,233,10,304)#L(10,304,9,384)#L(9,384,9,472)#", screen.toString());
  }

}
