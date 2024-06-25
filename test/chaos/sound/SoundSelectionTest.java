package chaos.sound;

import chaos.common.Attribute;
import chaos.common.State;
import chaos.common.inanimate.MagicGlass;
import chaos.common.inanimate.StandardWall;
import chaos.common.monster.Halfling;
import chaos.common.monster.Lion;
import chaos.common.monster.MindFlayer;
import chaos.common.monster.Ogre;
import chaos.common.monster.Robot;
import chaos.common.monster.Skeleton;
import chaos.common.monster.StoneGolem;
import chaos.common.mythos.Orange;
import chaos.common.wizard.Wizard1;
import junit.framework.TestCase;

/**
 * Test the corresponding class.
 * @author Sean A. Irvine
 */
public class SoundSelectionTest extends TestCase {

  public void test() {
    assertTrue(SoundSelection.getSpecialCombatSound(new MindFlayer()).startsWith("chaos/resources/sound/special/slurp"));
    assertEquals("chaos/resources/sound/special/squirt", SoundSelection.getSpecialCombatSound(new Orange()));
    assertTrue(SoundSelection.getCombatSound(new Lion()).startsWith("chaos/resources/sound/combat/lion"));
    assertEquals("chaos/resources/sound/combat/skeleton", SoundSelection.getCombatSound(new Skeleton()));
    assertNull(SoundSelection.getDeathSound(new Lion()));
    assertEquals("chaos/resources/sound/death/glass", SoundSelection.getDeathSound(new MagicGlass()));
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.set(Attribute.SPECIAL_COMBAT, 5);
    assertTrue(SoundSelection.getSpecialCombatSound(wiz).startsWith("chaos/resources/sound/special/shocker"));
  }

  public void testBattleSounds1() {
    final String[] b = SoundSelection.getBattleSounds(new Ogre(), new Lion(), 30);
    assertEquals(2, b.length);
    assertEquals("chaos/resources/sound/combat/ogre", b[0]);
    assertTrue(b[1].startsWith("chaos/resources/sound/combat/lion"));
  }

  public void testBattleSounds2() {
    final String[] b = SoundSelection.getBattleSounds(new Ogre(), new Robot(), 11);
    assertEquals(2, b.length);
    assertEquals("chaos/resources/sound/combat/ogre", b[0]);
    assertTrue(b[1].startsWith("chaos/resources/sound/combat/robot"));
  }

  public void testBattleSounds3() {
    final String[] b = SoundSelection.getBattleSounds(new Ogre(), new Robot(), 10);
    assertEquals(2, b.length);
    assertEquals("chaos/resources/sound/combat/ogre", b[0]);
    assertTrue(b[1].startsWith("chaos/resources/sound/combat/robot"));
  }

  public void testBattleSounds4() {
    final String[] b = SoundSelection.getBattleSounds(new Halfling(), new Halfling(), 10);
    assertEquals(4, b.length);
    assertTrue(b[0].startsWith("chaos/resources/sound/hattack/attack"));
    assertTrue(b[1].startsWith("chaos/resources/sound/hdefend/defend"));
    assertTrue(b[2].startsWith("chaos/resources/sound/hattack/attack"));
    assertTrue(b[3].startsWith("chaos/resources/sound/hdefend/defend"));
  }

  public void testBattleSounds5() {
    final String[] b = SoundSelection.getBattleSounds(new Halfling(), new StoneGolem(), 10);
    assertEquals(2, b.length);
    assertTrue(b[0].startsWith("chaos/resources/sound/hattack/attack"));
    assertTrue(b[1].startsWith("chaos/resources/sound/combat/donk"));
  }

  public void testBattleSounds6() {
    final String[] b = SoundSelection.getBattleSounds(new Halfling(), new StandardWall(), 11);
    assertEquals(2, b.length);
    assertTrue(b[0].startsWith("chaos/resources/sound/hattack/attack"));
    assertTrue(b[1].startsWith("chaos/resources/sound/genericcombat/SwooshThud"));
  }

  public void testBattleSounds7() {
    final String[] b = SoundSelection.getBattleSounds(new Halfling(), new StandardWall(), 12);
    assertEquals(2, b.length);
    assertTrue(b[0].startsWith("chaos/resources/sound/hattack/attack"));
    assertTrue(b[1].startsWith("chaos/resources/sound/genericcombat/BigSwooshThud"));
  }

  public void testBattleSounds8() {
    final String[] b = SoundSelection.getBattleSounds(new StandardWall(), new StandardWall(), 12);
    //    System.out.println(Arrays.toString(b));
    assertEquals(2, b.length);
    assertTrue(b[0].startsWith("chaos/resources/sound/genericcombat/SwooshUgh"));
    assertTrue(b[1].startsWith("chaos/resources/sound/genericcombat/BigSwooshThud"));
  }

}
