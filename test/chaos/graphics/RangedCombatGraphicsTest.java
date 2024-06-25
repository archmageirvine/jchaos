package chaos.graphics;

import chaos.board.World;
import chaos.common.State;
import chaos.common.dragon.RedDragon;
import chaos.common.monster.Dalek;
import chaos.common.monster.Orc;
import chaos.common.monster.WoodElf;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class RangedCombatGraphicsTest extends TestCase {

  public void test0() {
    final World w = new World(5, 4);
    final MockScreen screen = new MockScreen();
    final TileManager tm = TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE16);
    final Animator a = new Animator(w, screen, tm);
    final RangedCombatGraphics se = new RangedCombatGraphics(w, screen, screen.getGraphics(), a, 4);
    se.rangedCombatEffect(0, 4, new WoodElf());
    assertEquals("fillMain(java.awt.Color[r=0,g=0,b=0])#drawCell(0,0)#drawCell(0,0)#|setColor(java.awt.Color[r=255,g=0,b=0])#L(8,8,72,8)#setColor(java.awt.Color[r=0,g=0,b=0])#L(8,8,72,8)#", screen.toString());
  }

  public void test1() {
    final World w = new World(5, 4);
    final MockScreen screen = new MockScreen();
    final TileManager tm = TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE16);
    final Animator a = new Animator(w, screen, tm);
    final RangedCombatGraphics se = new RangedCombatGraphics(w, screen, screen.getGraphics(), a, 4);
    se.rangedCombatEffect(9, 2, new Dalek());
    assertEquals("fillMain(java.awt.Color[r=0,g=0,b=0])#drawCell(4,1)#|setColor(java.awt.Color[r=255,g=255,b=0])#L(71,23,71,23)#L(70,24,70,24)#L(70,26,70,26)#L(72,25,72,25)#L(73,23,73,23)#L(71,21,71,21)#L(69,22,69,22)#L(68,24,68,24)#setColor(java.awt.Color[r=0,g=0,b=0])#L(71,23,71,23)#L(70,24,70,24)#L(70,26,70,26)#L(72,25,72,25)#L(73,23,73,23)#L(71,21,71,21)#L(69,22,69,22)#L(68,24,68,24)#setColor(java.awt.Color[r=255,g=255,b=0])#L(67,21,67,21)#L(66,22,66,22)#L(66,24,66,24)#L(68,23,68,23)#L(69,21,69,21)#L(67,19,67,19)#L(65,20,65,20)#L(64,22,64,22)#setColor(java.awt.Color[r=0,g=0,b=0])#L(67,21,67,21)#L(66,22,66,22)#L(66,24,66,24)#L(68,23,68,23)#L(69,21,69,21)#L(67,19,67,19)#L(65,20,65,20)#L(64,22,64,22)#setColor(java.awt.Color[r=255,g=255,b=0])#L(63,19,63,19)#L(62,20,62,20)#L(62,22,62,22)#L(64,21,64,21)#L(65,19,65,19)#L(63,17,63,17)#L(61,18,61,18)#L(60,20,60,20)#setColor(java.awt.Color[r=0,g=0,b=0])#L(63,19,63,19)#L(62,20,62,20)#L(62,22,62,22)#L(64,21,64,21)#L(65,19,65,19)#L(63,17,63,17)#L(61,18,61,18)#L(60,20,60,20)#setColor(java.awt.Color[r=255,g=255,b=0])#L(59,17,59,17)#L(58,18,58,18)#L(58,20,58,20)#L(60,19,60,19)#L(61,17,61,17)#L(59,15,59,15)#L(57,16,57,16)#L(56,18,56,18)#setColor(java.awt.Color[r=0,g=0,b=0])#L(59,17,59,17)#L(58,18,58,18)#L(58,20,58,20)#L(60,19,60,19)#L(61,17,61,17)#L(59,15,59,15)#L(57,16,57,16)#L(56,18,56,18)#setColor(java.awt.Color[r=255,g=255,b=0])#L(55,15,55,15)#L(54,16,54,16)#L(54,18,54,18)#L(56,17,56,17)#L(57,15,57,15)#L(55,13,55,13)#L(53,14,53,14)#L(52,16,52,16)#setColor(java.awt.Color[r=0,g=0,b=0])#L(55,15,55,15)#L(54,16,54,16)#L(54,18,54,18)#L(56,17,56,17)#L(57,15,57,15)#L(55,13,55,13)#L(53,14,53,14)#L(52,16,52,16)#setColor(java.awt.Color[r=255,g=255,b=0])#L(51,13,51,13)#L(50,14,50,14)#L(50,16,50,16)#L(52,15,52,15)#L(53,13,53,13)#L(51,11,51,11)#L(49,12,49,12)#L(48,14,48,14)#setColor(java.awt.Color[r=0,g=0,b=0])#L(51,13,51,13)#L(50,14,50,14)#L(50,16,50,16)#L(52,15,52,15)#L(53,13,53,13)#L(51,11,51,11)#L(49,12,49,12)#L(48,14,48,14)#setColor(java.awt.Color[r=255,g=255,b=0])#L(47,11,47,11)#L(46,12,46,12)#L(46,14,46,14)#L(48,13,48,13)#L(49,11,49,11)#L(47,9,47,9)#L(45,10,45,10)#L(44,12,44,12)#setColor(java.awt.Color[r=0,g=0,b=0])#L(47,11,47,11)#L(46,12,46,12)#L(46,14,46,14)#L(48,13,48,13)#L(49,11,49,11)#L(47,9,47,9)#L(45,10,45,10)#L(44,12,44,12)#setColor(java.awt.Color[r=255,g=255,b=0])#L(43,9,43,9)#L(42,10,42,10)#L(42,12,42,12)#L(44,11,44,11)#L(45,9,45,9)#L(43,7,43,7)#L(41,8,41,8)#L(40,10,40,10)#", screen.toString());
  }

  public void test2() {
    final World w = new World(5, 4);
    final MockScreen screen = new MockScreen();
    final TileManager tm = TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE16);
    final Animator a = new Animator(w, screen, tm);
    final RangedCombatGraphics se = new RangedCombatGraphics(w, screen, screen.getGraphics(), a, 4);
    se.rangedCombatEffect(5, 0, new RedDragon());
    final Orc o = new Orc();
    o.setState(State.ASLEEP);
    w.getCell(5).push(o);
    assertEquals("fillMain(java.awt.Color[r=0,g=0,b=0])#drawCell(0,1)#|setColor(java.awt.Color[r=255,g=255,b=0])#L(7,23,7,23)#setColor(java.awt.Color[r=255,g=255,b=0])#L(8,23,8,23)#setColor(java.awt.Color[r=255,g=255,b=0])#L(8,24,8,24)#setColor(java.awt.Color[r=255,g=255,b=0])#L(6,24,6,24)#setColor(java.awt.Color[r=255,g=255,b=0])#L(4,25,4,25)#setColor(java.awt.Color[r=255,g=255,b=0])#L(6,26,6,26)#setColor(java.awt.Color[r=255,g=255,b=0])#L(8,26,8,26)#setColor(java.awt.Color[r=255,g=255,b=0])#L(9,24,9,24)#setColor(java.awt.Color[r=255,g=255,b=0])#L(9,22,9,22)#setColor(java.awt.Color[r=255,g=255,b=0])#L(7,21,7,21)#setColor(java.awt.Color[r=255,g=255,b=0])#L(5,21,5,21)#setColor(java.awt.Color[r=255,g=255,b=0])#L(4,23,4,23)#setColor(java.awt.Color[r=255,g=255,b=0])#L(7,18,7,18)#setColor(java.awt.Color[r=255,g=255,b=0])#L(8,18,8,18)#setColor(java.awt.Color[r=255,g=255,b=0])#L(8,19,8,19)#setColor(java.awt.Color[r=255,g=255,b=0])#L(6,19,6,19)#setColor(java.awt.Color[r=255,g=255,b=0])#L(4,20,4,20)#setColor(java.awt.Color[r=255,g=255,b=0])#L(6,21,6,21)#setColor(java.awt.Color[r=255,g=255,b=0])#L(8,21,8,21)#setColor(java.awt.Color[r=255,g=255,b=0])#L(9,19,9,19)#setColor(java.awt.Color[r=255,g=255,b=0])#L(9,17,9,17)#setColor(java.awt.Color[r=255,g=255,b=0])#L(7,16,7,16)#setColor(java.awt.Color[r=255,g=255,b=0])#L(5,16,5,16)#setColor(java.awt.Color[r=255,g=255,b=0])#L(4,18,4,18)#setColor(java.awt.Color[r=255,g=255,b=0])#L(7,13,7,13)#setColor(java.awt.Color[r=255,g=255,b=0])#L(8,13,8,13)#setColor(java.awt.Color[r=255,g=255,b=0])#L(8,14,8,14)#setColor(java.awt.Color[r=255,g=255,b=0])#L(6,14,6,14)#setColor(java.awt.Color[r=255,g=255,b=0])#L(4,15,4,15)#setColor(java.awt.Color[r=255,g=255,b=0])#L(6,16,6,16)#setColor(java.awt.Color[r=255,g=255,b=0])#L(8,16,8,16)#setColor(java.awt.Color[r=255,g=255,b=0])#L(9,14,9,14)#setColor(java.awt.Color[r=255,g=255,b=0])#L(9,12,9,12)#setColor(java.awt.Color[r=255,g=255,b=0])#L(7,11,7,11)#setColor(java.awt.Color[r=255,g=255,b=0])#L(5,11,5,11)#setColor(java.awt.Color[r=255,g=255,b=0])#L(4,13,4,13)#setColor(java.awt.Color[r=255,g=255,b=0])#L(7,8,7,8)#setColor(java.awt.Color[r=255,g=255,b=0])#L(8,8,8,8)#setColor(java.awt.Color[r=255,g=255,b=0])#L(8,9,8,9)#setColor(java.awt.Color[r=255,g=255,b=0])#L(6,9,6,9)#setColor(java.awt.Color[r=255,g=255,b=0])#L(4,10,4,10)#setColor(java.awt.Color[r=255,g=255,b=0])#L(6,11,6,11)#setColor(java.awt.Color[r=255,g=255,b=0])#L(8,11,8,11)#setColor(java.awt.Color[r=255,g=255,b=0])#L(9,9,9,9)#setColor(java.awt.Color[r=255,g=255,b=0])#L(9,7,9,7)#setColor(java.awt.Color[r=255,g=255,b=0])#L(7,6,7,6)#setColor(java.awt.Color[r=255,g=255,b=0])#L(5,6,5,6)#setColor(java.awt.Color[r=255,g=255,b=0])#L(4,8,4,8)#setColor(java.awt.Color[r=0,g=0,b=0])#L(7,23,7,23)#setColor(java.awt.Color[r=0,g=0,b=0])#L(8,23,8,23)#setColor(java.awt.Color[r=0,g=0,b=0])#L(8,24,8,24)#setColor(java.awt.Color[r=0,g=0,b=0])#L(6,24,6,24)#setColor(java.awt.Color[r=0,g=0,b=0])#L(4,25,4,25)#setColor(java.awt.Color[r=0,g=0,b=0])#L(6,26,6,26)#setColor(java.awt.Color[r=0,g=0,b=0])#L(8,26,8,26)#setColor(java.awt.Color[r=0,g=0,b=0])#L(9,24,9,24)#setColor(java.awt.Color[r=0,g=0,b=0])#L(9,22,9,22)#setColor(java.awt.Color[r=0,g=0,b=0])#L(7,21,7,21)#setColor(java.awt.Color[r=0,g=0,b=0])#L(5,21,5,21)#setColor(java.awt.Color[r=0,g=0,b=0])#L(4,23,4,23)#setColor(java.awt.Color[r=0,g=0,b=0])#L(7,18,7,18)#setColor(java.awt.Color[r=0,g=0,b=0])#L(8,18,8,18)#setColor(java.awt.Color[r=0,g=0,b=0])#L(8,19,8,19)#setColor(java.awt.Color[r=0,g=0,b=0])#L(6,19,6,19)#setColor(java.awt.Color[r=0,g=0,b=0])#L(4,20,4,20)#setColor(java.awt.Color[r=0,g=0,b=0])#L(6,21,6,21)#setColor(java.awt.Color[r=0,g=0,b=0])#L(8,21,8,21)#setColor(java.awt.Color[r=0,g=0,b=0])#L(9,19,9,19)#setColor(java.awt.Color[r=0,g=0,b=0])#L(9,17,9,17)#setColor(java.awt.Color[r=0,g=0,b=0])#L(7,16,7,16)#setColor(java.awt.Color[r=0,g=0,b=0])#L(5,16,5,16)#setColor(java.awt.Color[r=0,g=0,b=0])#L(4,18,4,18)#setColor(java.awt.Color[r=0,g=0,b=0])#L(7,13,7,13)#setColor(java.awt.Color[r=0,g=0,b=0])#L(8,13,8,13)#setColor(java.awt.Color[r=0,g=0,b=0])#L(8,14,8,14)#setColor(java.awt.Color[r=0,g=0,b=0])#L(6,14,6,14)#setColor(java.awt.Color[r=0,g=0,b=0])#L(4,15,4,15)#setColor(java.awt.Color[r=0,g=0,b=0])#L(6,16,6,16)#setColor(java.awt.Color[r=0,g=0,b=0])#L(8,16,8,16)#setColor(java.awt.Color[r=0,g=0,b=0])#L(9,14,9,14)#setColor(java.awt.Color[r=0,g=0,b=0])#L(9,12,9,12)#setColor(java.awt.Color[r=0,g=0,b=0])#L(7,11,7,11)#setColor(java.awt.Color[r=0,g=0,b=0])#L(5,11,5,11)#setColor(java.awt.Color[r=0,g=0,b=0])#L(4,13,4,13)#setColor(java.awt.Color[r=0,g=0,b=0])#L(7,8,7,8)#setColor(java.awt.Color[r=0,g=0,b=0])#L(8,8,8,8)#setColor(java.awt.Color[r=0,g=0,b=0])#L(8,9,8,9)#setColor(java.awt.Color[r=0,g=0,b=0])#L(6,9,6,9)#setColor(java.awt.Color[r=0,g=0,b=0])#L(4,10,4,10)#setColor(java.awt.Color[r=0,g=0,b=0])#L(6,11,6,11)#setColor(java.awt.Color[r=0,g=0,b=0])#L(8,11,8,11)#setColor(java.awt.Color[r=0,g=0,b=0])#L(9,9,9,9)#setColor(java.awt.Color[r=0,g=0,b=0])#L(9,7,9,7)#setColor(java.awt.Color[r=0,g=0,b=0])#L(7,6,7,6)#setColor(java.awt.Color[r=0,g=0,b=0])#L(5,6,5,6)#setColor(java.awt.Color[r=0,g=0,b=0])#L(4,8,4,8)#", screen.toString());
  }

}
