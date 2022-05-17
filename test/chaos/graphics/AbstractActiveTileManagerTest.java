package chaos.graphics;

import java.awt.image.BufferedImage;

import chaos.common.Attribute;
import chaos.common.PowerUps;
import chaos.common.Realm;
import chaos.common.State;
import chaos.common.free.Arborist;
import chaos.common.growth.GooeyBlob;
import chaos.common.monster.Gollop;
import chaos.common.monster.Horse;
import chaos.common.monster.Manticore;
import chaos.common.wizard.Wizard1;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public abstract class AbstractActiveTileManagerTest extends AbstractTileManagerTest {

  protected abstract int getWidth();

  public void testCloaking() {
    // We have had crashes with this before
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    w.set(PowerUps.CLOAKED, 1);
    final TileManager tm = getTileManager();
    tm.getTile(w, 0, 0, 0);
    final Manticore m = new Manticore();
    tm.getTile(m, 0, 0, 0);
    tm.getTile(m, 0, 0, 0);
    tm.getTile(m, 0, 0, 0);
    final BufferedImage cl = tm.getTile(w, 0, 0, 0);
    final BufferedImage sp = tm.getSpellTile(w);
    assertEquals(getWidth(), cl.getWidth(null));
    assertEquals(getWidth(), sp.getWidth(null));
    assertEquals(getWidth(), cl.getHeight(null));
    assertEquals(getWidth(), sp.getHeight(null));
    int darker = 0;
    for (int y = 0; y < getWidth(); ++y) {
      for (int x = 0; x < getWidth(); ++x) {
        final int a = cl.getRGB(x, y);
        final int b = sp.getRGB(x, y);
        assertTrue(a <= b);
        assertTrue((a & 0xFFFF) <= (b & 0xFFFF));
        assertTrue((a & 0xFF) <= (b & 0xFF));
        if (a < b) {
          ++darker;
        }
      }
    }
    assertTrue(darker > 10);
  }

  public void testMounted() {
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    final Horse h = new Horse();
    final TileManager tm = getTileManager();
    final BufferedImage im = tm.getSpellTile(h);
    assertEquals(im, tm.getSpellTile(h));
    h.setMount(w);
    assertFalse(im.equals(tm.getSpellTile(h)));
  }

  private static class MyArborist extends Arborist { }

  public void testUnknown() {
    final TileManager tm = getTileManager();
    assertNotNull(tm.getSpellTile(new Arborist()));
    assertEquals(tm.getSpellTile(new Arborist()), tm.getTile(new Arborist(), 0, 0, 0));
    assertNotNull(tm.getSpellTile(new MyArborist()));
    assertEquals(tm.getSpellTile(new MyArborist()), tm.getTile(new MyArborist(), 0, 0, 0));
    assertNull(tm.getSpellTile(null));
  }

  public void testPoisoned() {
    final TileManager tm = getTileManager();
    final GooeyBlob m = new GooeyBlob();
    final BufferedImage t1 = tm.getTile(m, 0, 0, 0);
    assertNotNull(t1);
    m.set(PowerUps.NO_GROW, 1);
    final BufferedImage p1 = tm.getTile(m, 0, 0, 0);
    final BufferedImage p2 = tm.getTile(m, 0, 0, 0);
    final BufferedImage p3 = tm.getTile(m, 0, 0, 0);
    assertTrue(t1 != p1);
    assertTrue(t1 != p2);
    assertTrue(t1 != p3);
    assertNotNull(p1);
    assertNotNull(p2);
    assertNotNull(p3);
    assertEquals(getWidth(), p1.getWidth(null));
    for (int y = 0; y < getWidth(); ++y) {
      for (int x = 0; x < getWidth(); ++x) {
        final int a = p1.getRGB(x, y);
        assertEquals(a & 0xFF, (a >> 8) & 0xFF);
        assertEquals(a & 0xFF, (a >> 16) & 0xFF);
      }
    }
  }

  private int countBlack(final BufferedImage im) {
    int c = 0;
    for (int y = 0; y < getWidth(); ++y) {
      for (int x = 0; x < getWidth(); ++x) {
        if ((im.getRGB(x, y) & 0xFFFFFF) == 0) {
          ++c;
        }
      }
    }
    return c;
  }

  public void testGollop() {
    final TileManager tm = getTileManager();
    final Gollop m = new Gollop();
    m.set(Attribute.LIFE, 1);
    final BufferedImage t1 = tm.getTile(m, 0, 0, 0);
    assertNotNull(t1);
    final int blk1 = countBlack(t1);
    m.set(Attribute.LIFE, 21);
    final BufferedImage t21 = tm.getTile(m, 0, 0, 0);
    assertNotNull(t21);
    final int blk21 = countBlack(t21);
    assertTrue(blk21 < blk1);
    m.set(Attribute.LIFE, 29);
    final BufferedImage t29 = tm.getTile(m, 0, 0, 0);
    assertNotNull(t29);
    final int blk29 = countBlack(t29);
    assertEquals(blk21, blk29);
    m.set(Attribute.LIFE, 100);
    final BufferedImage t100 = tm.getTile(m, 0, 0, 0);
    assertNotNull(t100);
    final int blk100 = countBlack(t100);
    assertTrue(blk100 < blk21);
  }

  public void testHyadic() {
    final TileManager tm = getTileManager();
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    final BufferedImage tsNormal = tm.getTile(w, 0, 0, 0);
    w.setRealm(Realm.HYADIC);
    final BufferedImage tsHyadic = tm.getTile(w, 0, 0, 0);
    assertFalse(tsNormal.equals(tsHyadic));
  }
}
