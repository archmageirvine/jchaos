package irvine.tile;


/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class TwirlEffectTest extends AbstractTileEffectTest {




  @Override
  public TileEffect getEffect() {
    return new TwirlEffect(16, 0, ~0, 1, 30);
  }
  public void testBadCons() {
    try {
      new TwirlEffect(0, 0, 0, 1, 10);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Width must be positive power of 2.", e.getMessage());
    }
    try {
      new TwirlEffect(-2, 0, 0, 1, 10);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Width must be positive power of 2.", e.getMessage());
    }
    try {
      new TwirlEffect(3, 0, 0, 1, 10);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Width must be positive power of 2.", e.getMessage());
    }
    try {
      new TwirlEffect(212, 0, 0, 1, 10);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Width must be positive power of 2.", e.getMessage());
    }
    try {
      new TwirlEffect(4, 0, 0, 0, 10);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Illegal shrink.", e.getMessage());
    }
  }

  public void testSize16() {
    final TileEffect ef = getEffect();
    assertEquals(1048592, ef.next().hashCode());
    assertEquals(1048660, ef.next().hashCode());
    assertEquals(1048592, ef.next().hashCode());
    assertEquals(1048592, ef.next().hashCode());
    assertEquals(1048592, ef.next().hashCode());
    assertEquals(1048626, ef.next().hashCode());
    assertNull(ef.next());
  }

  public void testSize2() {
    final TileEffect ef = new TwirlEffect(2, 0x88888888, 0xDEADBEEF, 1, 30);
    final TileImage i = ef.next();
    assertEquals(2, i.getWidth());
    assertEquals(2, i.getHeight());
    assertEquals(0x88888888, i.getPixel(0, 0));
    assertEquals(0x88888888, i.getPixel(0, 1));
    assertEquals(0x88888888, i.getPixel(1, 0));
    assertEquals(0x88888888, i.getPixel(1, 1));
    assertNull(ef.next());
  }

  public void testSize2S() {
    final TileEffect ef = new TwirlEffect(2, 0x88888888, 0xDEADBEEF, 2, 30);
    assertNotNull(ef.next());
    assertNotNull(ef.next());
    assertNull(ef.next());
  }

  public void testSize8() {
    final TileEffect ef = new TwirlEffect(8, 0x88888888, 0xDEADBEEF, 1, -17);
    ef.next();
    final TileImage i = ef.next();
    assertEquals(8, i.getWidth());
    assertEquals(8, i.getHeight());
    assertEquals(0x88888888, i.getPixel(3, 0));
    assertEquals(0x88888888, i.getPixel(3, 1));
    assertEquals(0x88888888, i.getPixel(3, 2));
    assertEquals(0x88888888, i.getPixel(3, 3));
    assertEquals(0x88888888, i.getPixel(3, 4));
    assertEquals(0xDEADBEEF, i.getPixel(3, 5));
    assertEquals(0x88888888, i.getPixel(3, 6));
    assertEquals(0x88888888, i.getPixel(3, 7));
    ef.next();
    assertNull(ef.next());
  }

  public void testSize16count() {
    final TileEffect ef = new TwirlEffect(16, 0x88888888, 0xDEADBEEF, 2, 17);
    assertEquals(12, ef.list().size());
  }

}

