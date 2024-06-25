package chaos.graphics;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.monster.Lion;
import chaos.sound.Sound;
import irvine.tile.TileImage;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class SpungerEffectTest extends TestCase {

  public void testNoSound() {
    final World w = new World(5, 4);
    final SpungerEffect se = new SpungerEffect(TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE16), w, Sound.SOUND_ALL);
    final MockScreen screen = new MockScreen();
    se.performEffect(screen, screen.getGraphics(), (Cell) null, 16);
    assertEquals("", screen.toString());
    final HashSet<Cell> c = new HashSet<>();
    c.add(w.getCell(0));
    c.add(w.getCell(19));
    w.getCell(19).push(new Lion());
    se.performEffect(screen, screen.getGraphics(), c, 16);
    final String[] dc = screen.toString().split("#");
    assertEquals(31, dc.length);
    for (final String s : dc) {
      assertEquals("drawCell(4,3)", s);
    }
  }

  public void testBump1() {
    final TileImage im = new TileImage(2, 2);
    im.setPixel(0, 0, 0xCF345678);
    final TileImage old = im.copy();
    SpungerEffect.randomBump(im, 1);
    assertTrue(old.getPixel(0, 0) != im.getPixel(0, 0));
    assertEquals(old.getPixel(0, 1), im.getPixel(0, 1));
    assertEquals(old.getPixel(1, 0), im.getPixel(1, 0));
    assertEquals(old.getPixel(1, 1), im.getPixel(1, 1));
    assertEquals(0xCF000000, im.getPixel(0, 0) & 0xFF000000);
  }

  public void testBump2() {
    final TileImage im = new TileImage(2, 3);
    im.fill(0x12345678);
    final TileImage old = im.copy();
    SpungerEffect.randomBump(im, 42);
    assertTrue(old.getPixel(0, 0) != im.getPixel(0, 0));
    assertTrue(old.getPixel(0, 1) != im.getPixel(0, 1));
    assertTrue(old.getPixel(1, 0) != im.getPixel(1, 0));
    assertTrue(old.getPixel(1, 1) != im.getPixel(1, 1));
    assertTrue(old.getPixel(2, 0) != im.getPixel(2, 0));
    assertTrue(old.getPixel(2, 1) != im.getPixel(2, 1));
  }

  public void testBump3() {
    int hit = 0;
    for (int i = 0; i < 1000; ++i) {
      final TileImage im = new TileImage(20, 1);
      im.setPixel(0, 0, 0xFF);
      SpungerEffect.randomBump(im, 1);
      if (im.getPixel(0, 0) != 0xFF) {
        ++hit;
      }
    }
    // expect to have changed it 65.15% of the time
    assertTrue("Hit: " + hit, hit > 550 && hit < 750);
  }

  public void testBump4() {
    final TileImage im = new TileImage(2000, 1);
    im.fill(~0);
    SpungerEffect.randomBump(im, 2000);
    final boolean[] good = new boolean[24];
    for (int k = 0; k < 2000; ++k) {
      int p = im.getPixel(k, 0);
      assertEquals(0xFF000000, p & 0xFF000000);
      for (int j = 0; j < 24; ++j) {
        good[j] |= (p & 1) == 0;
        p >>>= 1;
      }
    }
    for (final boolean b : good) {
      assertTrue(b);
    }
  }

}
