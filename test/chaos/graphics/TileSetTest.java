package chaos.graphics;

import java.io.IOException;

import irvine.tile.TileSetReader;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class TileSetTest extends TestCase {

  public void testNull() {
    try {
      new TileSet(null, (String) null);
      fail("Accepted null resource");
    } catch (final NullPointerException e) {
      // ok
    } catch (final IOException e) {
      fail("I/O exception should not have happened in this case");
    }
  }

  public void testLion() throws Exception {
    final TileSetReader tsr = new TileSetReader(4, "chaos/graphics/active16/");
    // Lion
    final TileSet ts = new TileSet(tsr, "p       170,171,172     16f");
    assertTrue(ts.isPingPong());
    assertFalse(ts.isRandomSelect());
    assertEquals(1, ts.nextFrameIndex(0));
    assertEquals(2, ts.nextFrameIndex(1));
    assertEquals(3, ts.nextFrameIndex(2));
    assertEquals(0, ts.nextFrameIndex(3));
    assertNotNull(ts.getDeadImage());
    assertNotNull(ts.getSpellImage());
    assertEquals(ts.getSpellImage(), ts.getImage(0));
    assertNotNull(ts.getImage(1));
    assertNotNull(ts.getImage(2));
    assertNotNull(ts.getImage(3));
    assertEquals(16, ts.getImage(1).getWidth());
    assertEquals(16, ts.getDeadImage().getWidth());
    assertEquals(-2052080, ts.getDeadImage().getRGB(8, 8));
    assertEquals(3, ts.getNumberOfImages());
  }

  public void testAirElemental() throws Exception {
    final TileSetReader tsr = new TileSetReader(4, "chaos/graphics/active16/");
    // Air Elemental
    final TileSet ts = new TileSet(tsr, "x       12,13,14");
    assertFalse(ts.isPingPong());
    assertFalse(ts.isRandomSelect());
    assertEquals(1, ts.nextFrameIndex(0));
    assertEquals(2, ts.nextFrameIndex(1));
    assertEquals(0, ts.nextFrameIndex(2));
    assertNull(ts.getDeadImage());
    assertNotNull(ts.getSpellImage());
    assertEquals(ts.getSpellImage(), ts.getImage(0));
    assertNotNull(ts.getImage(1));
    assertNotNull(ts.getImage(2));
    assertEquals(3, ts.getNumberOfImages());
  }

  public void testStatic() throws Exception {
    final TileSetReader tsr = new TileSetReader(4, "chaos/graphics/active16/");
    final TileSet ts = new TileSet(tsr, "s       12,13,14");
    assertFalse(ts.isPingPong());
    assertTrue(ts.isRandomSelect());
    assertEquals(1, ts.nextFrameIndex(0));
    assertEquals(2, ts.nextFrameIndex(1));
    assertEquals(0, ts.nextFrameIndex(2));
    assertEquals(1, ts.nextFrameIndex(3));
    assertEquals(0, ts.nextFrameIndex(-2));
    assertNull(ts.getDeadImage());
    assertNotNull(ts.getSpellImage());
    assertNotNull(ts.getImage(0));
    assertEquals(3, ts.getNumberOfImages());
  }

  public void testUnary() throws Exception {
    final TileSetReader tsr = new TileSetReader(4, "chaos/graphics/active16/");
    final TileSet ts = new TileSet(tsr, "x 0");
    assertFalse(ts.isPingPong());
    assertFalse(ts.isRandomSelect());
    assertEquals(0, ts.nextFrameIndex(-1));
    assertEquals(0, ts.nextFrameIndex(0));
    assertEquals(0, ts.nextFrameIndex(1));
    assertEquals(0, ts.nextFrameIndex(2));
    assertNull(ts.getDeadImage());
    assertNotNull(ts.getSpellImage());
    assertNotNull(ts.getImage(0));
    assertEquals(1, ts.getNumberOfImages());
  }


}
