package chaos.graphics;

import static chaos.graphics.ShieldEffect.SHIELD_SOUND;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Comparator;
import java.util.TreeSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.monster.Lion;
import irvine.tile.TileImage;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class ShieldEffectTest extends TestCase {

  public void test1() {
    final World w = new World(5, 4);
    final ShieldEffect se = new ShieldEffect(w, Attribute.LIFE_RECOVERY, null);
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
    final ShieldEffect se = new ShieldEffect(w, Attribute.LIFE, null);
    final MockScreen screen = new MockScreen();
    se.performEffect(screen, screen.getGraphics(), c, 16);
    assertEquals("drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#drawCell(0,0)#drawCell(4,3)#", screen.toString());
  }

  public void testEmpty() {
    final World w = new World(5, 4);
    final TreeSet<Cell> c = new TreeSet<>();
    final ShieldEffect se = new ShieldEffect(w, Attribute.LIFE, null);
    final MockScreen screen = new MockScreen();
    final long t = System.currentTimeMillis();
    se.performEffect(screen, screen.getGraphics(), c, 16);
    assertTrue(System.currentTimeMillis() - t < 50);
    assertEquals("", screen.toString());
  }

  public void testMap() {
    assertEquals(19, SHIELD_SOUND.size());
    assertTrue("chaos/resources/sound/casting/Smash".equals(SHIELD_SOUND.get(Attribute.AGILITY)));
    assertTrue("chaos/resources/sound/casting/Smash".equals(SHIELD_SOUND.get(Attribute.AGILITY_RECOVERY)));
    assertTrue("chaos/resources/sound/casting/Laugh".equals(SHIELD_SOUND.get(Attribute.COMBAT)));
    assertTrue("chaos/resources/sound/casting/Laugh".equals(SHIELD_SOUND.get(Attribute.COMBAT_RECOVERY)));
    assertTrue("chaos/resources/sound/casting/Wisdom".equals(SHIELD_SOUND.get(Attribute.INTELLIGENCE)));
    assertTrue("chaos/resources/sound/casting/Wisdom".equals(SHIELD_SOUND.get(Attribute.INTELLIGENCE_RECOVERY)));
    assertTrue("chaos/resources/sound/casting/Bless".equals(SHIELD_SOUND.get(Attribute.LIFE)));
    assertTrue("chaos/resources/sound/casting/Bless".equals(SHIELD_SOUND.get(Attribute.LIFE_RECOVERY)));
    assertTrue("chaos/resources/sound/words/Protection".equals(SHIELD_SOUND.get(Attribute.MAGICAL_RESISTANCE)));
    assertTrue("chaos/resources/sound/words/Protection".equals(SHIELD_SOUND.get(Attribute.MAGICAL_RESISTANCE_RECOVERY)));
    assertTrue("chaos/resources/sound/casting/Speed".equals(SHIELD_SOUND.get(Attribute.MOVEMENT)));
    assertTrue("chaos/resources/sound/casting/Speed".equals(SHIELD_SOUND.get(Attribute.MOVEMENT_RECOVERY)));
    assertTrue("chaos/resources/sound/casting/Thunk".equals(SHIELD_SOUND.get(Attribute.RANGE)));
    assertTrue("chaos/resources/sound/casting/Thunk".equals(SHIELD_SOUND.get(Attribute.RANGE_RECOVERY)));
    assertTrue("chaos/resources/sound/casting/Thunk".equals(SHIELD_SOUND.get(Attribute.RANGED_COMBAT)));
    assertTrue("chaos/resources/sound/casting/Thunk".equals(SHIELD_SOUND.get(Attribute.RANGED_COMBAT_RECOVERY)));
    assertTrue("chaos/resources/sound/casting/Magnum".equals(SHIELD_SOUND.get(Attribute.SPECIAL_COMBAT)));
    assertTrue("chaos/resources/sound/casting/Magnum".equals(SHIELD_SOUND.get(Attribute.SPECIAL_COMBAT_RECOVERY)));
    assertTrue("chaos/resources/sound/casting/quickshot".equals(SHIELD_SOUND.get(Attribute.SHOTS)));
  }

  public void testGetArray() {
    final BufferedImage[] a = ShieldEffect.getShieldArray(16, Color.RED);
    assertEquals(8, a.length);
    assertEquals(16, a[0].getWidth());
    assertEquals(16, a[0].getHeight());
    final TileImage i = new TileImage(a[0]);
    int sum = 0;
    for (int y = 0; y < i.getHeight(); ++y) {
      for (int x = 0; x < i.getWidth(); ++x) {
        final int p = i.getPixel(x, y);
        sum += p & 0xFFFFFF;
        assertEquals(0, p & 0xFFFF);
      }
    }
    assertTrue(sum != 0);
  }

}
