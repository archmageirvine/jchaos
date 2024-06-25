package irvine.tile;


/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class FadeEffectTest extends AbstractTileEffectTest {


  @Override
  public TileEffect getEffect() {
    final TileImage i = new TileImage(3, 3);
    i.fill(~0);
    return new FadeEffect(i, 0xDEADBEEF);
  }

  public void testBadCons() {
    try {
      new FadeEffect(null, new TileImage(3, 4));
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
    try {
      new FadeEffect(null, 0);
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
    try {
      new FadeEffect(new TileImage(3, 4), null);
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
    try {
      new FadeEffect(new TileImage(3, 4), new TileImage(4, 3));
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Size mismatch", e.getMessage());
    }
  }

  public void testSize3() {
    final TileEffect ef = new FadeEffect(new TileImage(3, 3), new TileImage(3, 3));
    assertEquals(196611, ef.next().hashCode());
    assertEquals(196611, ef.next().hashCode());
    assertEquals(196611, ef.next().hashCode());
    assertEquals(196611, ef.next().hashCode());
    assertEquals(196611, ef.next().hashCode());
    assertEquals(196611, ef.next().hashCode());
    assertEquals(196611, ef.next().hashCode());
    assertEquals(196611, ef.next().hashCode());
    assertNull(ef.next());
  }

  public void testSize3w() {
    final TileEffect ef = new FadeEffect(new TileImage(3, 3), 0xDEADBEEF);
    assertEquals(-1547300834, ef.next().hashCode());
    assertEquals(-1284231936, ef.next().hashCode());
    assertEquals(591626666, ef.next().hashCode());
    assertEquals(1777164140, ef.next().hashCode());
    assertEquals(-460123066, ef.next().hashCode());
    assertEquals(1183122296, ef.next().hashCode());
    assertEquals(-661370158, ef.next().hashCode());
    assertEquals(-745881884, ef.next().hashCode());
    assertNull(ef.next());
  }

  public void testSize3ww() {
    final TileEffect ef = getEffect();
    assertEquals(-890247426, ef.next().hashCode());
    assertEquals(-432410944, ef.next().hashCode());
    assertEquals(-1309960502, ef.next().hashCode());
    assertEquals(-481829140, ef.next().hashCode());
    assertEquals(1036601062, ef.next().hashCode());
    assertEquals(-1999483208, ef.next().hashCode());
    assertEquals(-432605582, ef.next().hashCode());
    assertEquals(-745881884, ef.next().hashCode());
    assertNull(ef.next());
  }

  public void testSize3ag() {
    final TileImage i = new TileImage(2, 2);
    i.fill(0xFF00FF00);
    final TileEffect ef = new FadeEffect(i, i);
    assertEquals(131074, ef.next().hashCode());
    assertEquals(131074, ef.next().hashCode());
    assertEquals(131074, ef.next().hashCode());
    assertEquals(131074, ef.next().hashCode());
    assertEquals(131074, ef.next().hashCode());
    assertEquals(131074, ef.next().hashCode());
    assertEquals(131074, ef.next().hashCode());
    assertEquals(131074, ef.next().hashCode());
    assertNull(ef.next());
  }

}

