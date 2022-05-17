package irvine.tile;


/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class RotationEffectTest extends AbstractTileEffectTest {




  @Override
  public TileEffect getEffect() {
    return new RotationEffect(new TileImage(3, 7), 0, 5, 0, false);
  }

  public void testBadCons() {
    try {
      new RotationEffect(null, 0, 1, 0, false);
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
    try {
      new RotationEffect(null, 0, 1, 0, true);
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
  }

  public void test() {
    final TileImage i = new TileImage(3, 7);
    TileEffect re = new RotationEffect(i, 0, 5, 0, false);
    for (int j = 0; j < 5; ++j) {
      assertTrue(i == re.next());
    }
    assertNull(re.next());
    i.setPixel(2, 2, 0x12345678);
    re = new RotationEffect(i, 90, 5, 0, false);
    assertEquals(i.rotate90(), re.next());
    assertEquals(i.rotate180(), re.next());
    assertEquals(i.rotate270(), re.next());
    assertEquals(i, re.next());
    assertEquals(i.rotate90(), re.next());
    assertNull(re.next());
    re = new RotationEffect(i, 90, 0, 0, false);
    assertNull(re.next());
  }

}

