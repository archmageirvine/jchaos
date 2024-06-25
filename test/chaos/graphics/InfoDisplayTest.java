package chaos.graphics;

import chaos.Chaos;
import chaos.board.WizardManager;
import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.PowerUps;
import chaos.common.Realm;
import chaos.common.State;
import chaos.common.dragon.RedDragon;
import chaos.common.monster.Orc;
import chaos.common.monster.Vampire;
import chaos.common.monster.WoodElf;
import chaos.common.wizard.Wizard1;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class InfoDisplayTest extends TestCase {

  public void test1() {
    final MockScreen screen = new MockScreen();
    final InfoDisplay d = new InfoDisplay(screen, screen.getGraphics(), Chaos.getChaos().getTileManager(), new World(1, 1));
    d.showInfo(null, 0);
    assertEquals("highlight(null)#|I(500,20,null)#I(33,37,null)#", screen.toString());
  }

  public void testArgs() {
    final MockScreen screen = new MockScreen();
    final InfoDisplay d = new InfoDisplay(screen, screen.getGraphics(), Chaos.getChaos().getTileManager(), new World(1, 1));
    try {
      d.showInfo(null, 2);
      fail();
    } catch (final IllegalArgumentException e) {
      // ok
    }
  }

  public void test2() {
    final MockScreen screen = new MockScreen();
    final InfoDisplay d = new InfoDisplay(screen, screen.getGraphics(), Chaos.getChaos().getTileManager(), new World(1, 1));
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.set(PowerUps.ARBORIST, 1);
    wiz.set(PowerUps.LEVEL, 5);
    wiz.setPersonalName("Wizard 1");
    d.showInfo(wiz, 1);
    assertTrue(screen.toString().startsWith("highlight(null)#highlight(material)#|I(500,160,null)#I(33,37,null)#setColor(java.awt.Color[r=255,g=255,b=0])#setFont()#getFontMetrics()"));
  }

  public void test3() {
    final MockScreen screen = new MockScreen();
    final InfoDisplay d = new InfoDisplay(screen, screen.getGraphics(), Chaos.getChaos().getTileManager(), new World(1, 1));
    d.showInfo(new RedDragon(), 0);
    final String s = screen.toString();
    assertTrue(s, s.startsWith("highlight(null)#highlight(dragon)#|I(500,20,null)#I(33,37,null)#setColor(java.awt.Color[r=255,g=255,b=0])#setFont()#getFontMetrics()#drawString(Red Dragon,550,30)#I(501,35,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,35,20,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,35,20,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,39,2,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,39,2,3)#I(501,45,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,45,22,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,45,22,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,49,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,49,0,3)#I(501,55,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,55,44,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,55,44,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,59,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,59,0,3)#I(501,65,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,65,10,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,65,10,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,69,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,69,0,3)#I(501,75,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,75,6,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,75,6,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,79,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,79,0,3)#I(590,75,null)#I(501,85,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,85,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,85,0,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,89,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,89,0,3)#I(501,95,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,95,3,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,95,3,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,99,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,99,0,3)#I(590,95,null)#I(501,105,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,105,3,3)#fillRect(515,105,3,3)#fillRect(520,105,3,3)#fillRect(525,105,3,3)#fillRect(530,105,3,3)#fillRect(535,105,3,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,105,3,3)#fillRect(515,105,3,3)#fillRect(520,105,3,3)#fillRect(525,105,3,3)#fillRect(530,105,3,3)#fillRect(535,105,3,3)#setColor(java.awt.Color[r=0,g=0,b=255])#setColor(java.awt.Color[r=0,g=255,b=0])#I(501,115,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,115,3,3)#fillRect(515,115,3,3)#fillRect(520,115,3,3)#fillRect(525,115,3,3)#fillRect(530,115,3,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,115,3,3)#fillRect(515,115,3,3)#fillRect(520,115,3,3)#fillRect(525,115,3,3)"));
  }

  public void testDead() {
    final MockScreen screen = new MockScreen();
    final InfoDisplay d = new InfoDisplay(screen, screen.getGraphics(), Chaos.getChaos().getTileManager(), new World(1, 1));
    final Orc o = new Orc();
    o.setState(State.DEAD);
    d.showInfo(o, 0);
    assertEquals("highlight(null)#highlight(material)#|I(500,20,null)#I(33,37,null)#setColor(java.awt.Color[r=255,g=0,b=0])#setFont()#getFontMetrics()#drawString(Orc,550,30)#I(501,35,null)#I(592,35,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,35,2,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,35,2,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,39,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,39,0,3)#I(501,45,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,45,29,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,45,29,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,49,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,49,0,3)#I(501,55,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,55,17,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,55,17,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,59,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,59,0,3)#I(501,65,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,65,14,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,65,14,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,69,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,69,0,3)#I(501,75,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,75,1,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,75,1,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,79,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,79,0,3)#I(590,75,null)#I(501,85,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,85,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,85,0,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,89,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,89,0,3)#I(501,95,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,95,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,95,0,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,99,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,99,0,3)#I(501,105,null)#setColor(java.awt.Color[r=0,g=0,b=255])#setColor(java.awt.Color[r=0,g=255,b=0])#setColor(java.awt.Color[r=0,g=0,b=255])#setColor(java.awt.Color[r=0,g=255,b=0])#I(501,115,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,115,3,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,115,3,3)#setColor(java.awt.Color[r=0,g=0,b=255])#setColor(java.awt.Color[r=0,g=255,b=0])#setColor(java.awt.Color[r=255,g=255,b=0])#drawString(DEAD,550,137)#", screen.toString());
  }

  public void testAsleep() {
    final MockScreen screen = new MockScreen();
    final InfoDisplay d = new InfoDisplay(screen, screen.getGraphics(), Chaos.getChaos().getTileManager(), new World(1, 1));
    final Orc o = new Orc();
    o.setState(State.ASLEEP);
    d.showInfo(o, 0);
    assertTrue(screen.toString().contains("ASLEEP"));
  }

  public void testPromo() {
    final MockScreen screen = new MockScreen();
    final World w = Chaos.getChaos().getWorld();
    final InfoDisplay d = new InfoDisplay(screen, screen.getGraphics(), Chaos.getChaos().getTileManager(), w);
    final WoodElf o = new WoodElf();
    o.setRealm(Realm.MYTHOS);
    assertNotNull(w);
    final WizardManager wm = w.getWizardManager();
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setPersonalName("X Y Z");
    wm.setWizard(1, wiz);
    o.setOwner(wiz.getOwner());
    d.showInfo(o, 0);
    final String s = screen.toString();
    assertTrue(s, s.startsWith("highlight(null)#highlight(mythos)#|I(500,20,null)#I(33,37,null)#setColor(java.awt.Color[r=255,g=255,b=0])#setFont()#getFontMetrics()#drawString(Wood Elf,550,30)#I(501,35,null)#I(592,35,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,35,3,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,35,3,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,39,1,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,39,1,3)#I(501,45,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,45,50,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,45,50,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,49,4,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,49,4,3)#I(501,55,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,55,37,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,55,37,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,59,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,59,0,3)#I(501,65,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,65,17,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,65,17,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,69,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,69,0,3)#I(501,75,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,75,1,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,75,1,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,79,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,79,0,3)#I(590,75,null)#I(501,85,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,85,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,85,0,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,89,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,89,0,3)#I(501,95,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,95,2,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,95,2,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,99,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,99,0,3)#I(590,95,null)#I(501,105,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,105,3,3)#fillRect(515,105,3,3)#fillRect(520,105,3,3)#fillRect(525,105,3,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,105,3,3)#fillRect(515,105,3,3)#fillRect(520,105,3,3)#fillRect(525,105,3,3)#setColor(java.awt.Color[r=0,g=0,b=255])#setColor(java.awt.Color[r=0,g=255,b=0])#I(501,115,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,115,3,3)"));
  }

  public void testVampire() {
    final MockScreen screen = new MockScreen();
    final InfoDisplay d = new InfoDisplay(screen, screen.getGraphics(), Chaos.getChaos().getTileManager(), new World(1, 1));
    final Vampire v = new Vampire();
    v.set(Attribute.MOVEMENT, 16);
    v.set(Attribute.MOVEMENT_RECOVERY, -3);
    v.setCombatApply(Attribute.LIFE_RECOVERY);
    v.set(Attribute.RANGE, 13);
    v.set(Attribute.RANGE_RECOVERY, 2);
    v.set(Attribute.RANGED_COMBAT, Attribute.RANGED_COMBAT.max());
    d.showInfo(v, 0);
    final String s = screen.toString();
    assertTrue(s, s.startsWith("highlight(null)#highlight(undead)#|I(500,20,null)#I(33,37,null)#setColor(java.awt.Color[r=255,g=255,b=0])#setFont()#getFontMetrics()#drawString(Vampire,550,30)#I(501,35,null)#I(592,35,null)#I(584,35,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,35,25,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,35,25,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,39,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,39,0,3)#I(501,45,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,45,36,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,45,36,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,49,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,49,0,3)#I(501,55,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,55,45,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,55,45,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,59,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,59,0,3)#I(501,65,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,65,28,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,65,28,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,69,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,69,0,3)#I(501,75,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,75,6,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,75,6,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,79,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,79,0,3)#I(590,75,null)#I(501,85,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,85,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,85,0,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,89,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,89,0,3)#I(501,95,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,95,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,95,50,3)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,99,0,3)#setColor(java.awt.Color[r=0,g=255,b=0])#fillRect(510,99,0,3)#I(590,95,null)#I(501,105,null)#setColor(java.awt.Color[r=0,g=0,b=255])#fillRect(510,105,3,3)#fillRect(514,105,3,3)#fillRect(518,105,3,3)#fillRect(522,105,3,3)#fillRect(526,105,3,3)#fillRect(530,105,3,3)#fillRect(534,105,3,3)"));
  }
}
