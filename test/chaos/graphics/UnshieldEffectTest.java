package chaos.graphics;

import static chaos.graphics.UnshieldEffect.UNSHIELD_SOUND;

import java.io.Serializable;
import java.util.Comparator;
import java.util.TreeSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.monster.Lion;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class UnshieldEffectTest extends TestCase {

  public void test1() {
    final World w = new World(5, 4);
    final UnshieldEffect se = new UnshieldEffect(w, Attribute.LIFE_RECOVERY, null);
    final MockScreen screen = new MockScreen();
    se.performEffect(screen, screen.getGraphics(), (Cell) null, 16);
    assertEquals("", screen.toString());
    se.performEffect(screen, screen.getGraphics(), w.getCell(7), 16);
    assertEquals("drawCell(2,1)#drawCell(2,1)#drawCell(2,1)#drawCell(2,1)#drawCell(2,1)#drawCell(2,1)#drawCell(2,1)#drawCell(2,1)#", screen.toString());
  }

  private static class CellComparator implements Comparator<Cell>, Serializable {
    @Override
    public int compare(final Cell a, final Cell b) {
      return a.getCellNumber() - b.getCellNumber();
    }
  }

  public void test2() {
    final World w = new World(5, 4);
    final TreeSet<Cell> c = new TreeSet<>(new CellComparator());
    c.add(w.getCell(0));
    c.add(w.getCell(19));
    w.getCell(19).push(new Lion());
    final UnshieldEffect se = new UnshieldEffect(w, Attribute.LIFE, null);
    final MockScreen screen = new MockScreen();
    se.performEffect(screen, screen.getGraphics(), c, 16);
    assertEquals("drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#", screen.toString());
  }

  public void testEmpty() {
    final World w = new World(5, 4);
    final TreeSet<Cell> c = new TreeSet<>();
    final UnshieldEffect se = new UnshieldEffect(w, Attribute.LIFE, null);
    final MockScreen screen = new MockScreen();
    final long t = System.currentTimeMillis();
    se.performEffect(screen, screen.getGraphics(), c, 16);
    assertTrue(System.currentTimeMillis() - t < 40);
    assertEquals("", screen.toString());
  }

  public void testMap() {
    assertEquals(18, UNSHIELD_SOUND.size());
    assertTrue("chaos/resources/sound/casting/Clumsy".equals(UNSHIELD_SOUND.get(Attribute.AGILITY)));
    assertTrue("chaos/resources/sound/casting/Clumsy".equals(UNSHIELD_SOUND.get(Attribute.AGILITY_RECOVERY)));
    assertTrue("chaos/resources/sound/casting/Touch".equals(UNSHIELD_SOUND.get(Attribute.COMBAT)));
    assertTrue("chaos/resources/sound/casting/Touch".equals(UNSHIELD_SOUND.get(Attribute.COMBAT_RECOVERY)));
    assertTrue("chaos/resources/sound/casting/Idiocy".equals(UNSHIELD_SOUND.get(Attribute.INTELLIGENCE)));
    assertTrue("chaos/resources/sound/casting/Idiocy".equals(UNSHIELD_SOUND.get(Attribute.INTELLIGENCE_RECOVERY)));
    assertTrue("chaos/resources/sound/casting/Ouch".equals(UNSHIELD_SOUND.get(Attribute.LIFE)));
    assertTrue("chaos/resources/sound/casting/Disease".equals(UNSHIELD_SOUND.get(Attribute.LIFE_RECOVERY)));
    assertTrue("chaos/resources/sound/words/Bolt".equals(UNSHIELD_SOUND.get(Attribute.MAGICAL_RESISTANCE)));
    assertTrue("chaos/resources/sound/words/Bolt".equals(UNSHIELD_SOUND.get(Attribute.MAGICAL_RESISTANCE_RECOVERY)));
    assertTrue("chaos/resources/sound/casting/Stop".equals(UNSHIELD_SOUND.get(Attribute.MOVEMENT)));
    assertTrue("chaos/resources/sound/casting/Stop".equals(UNSHIELD_SOUND.get(Attribute.MOVEMENT_RECOVERY)));
    assertTrue("chaos/resources/sound/casting/Thunk".equals(UNSHIELD_SOUND.get(Attribute.RANGE)));
    assertTrue("chaos/resources/sound/casting/Thunk".equals(UNSHIELD_SOUND.get(Attribute.RANGE_RECOVERY)));
    assertTrue("chaos/resources/sound/casting/Thunk".equals(UNSHIELD_SOUND.get(Attribute.RANGED_COMBAT)));
    assertTrue("chaos/resources/sound/casting/Thunk".equals(UNSHIELD_SOUND.get(Attribute.RANGED_COMBAT_RECOVERY)));
    assertTrue("chaos/resources/sound/casting/Nullify".equals(UNSHIELD_SOUND.get(Attribute.SPECIAL_COMBAT)));
    assertTrue("chaos/resources/sound/casting/Nullify".equals(UNSHIELD_SOUND.get(Attribute.SPECIAL_COMBAT_RECOVERY)));
  }
}
