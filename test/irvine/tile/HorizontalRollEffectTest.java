package irvine.tile;


/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class HorizontalRollEffectTest extends AbstractTileEffectTest {

  @Override
  public TileEffect getEffect() {
    return new HorizontalRollEffect(new TileImage(2, 3), false);
  }

  public void testBadCons() {
    try {
      new HorizontalRollEffect(null, false);
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
  }

  public void testSize16() {
    final TileImage i = new TileImage(5, 4);
    for (int y = 0, c = 0; y < i.getHeight(); ++y) {
      for (int x = 0; x < i.getWidth(); ++x) {
        i.setPixel(x, y, c++);
      }
    }
    TileEffect ef = new HorizontalRollEffect(i, false);
    for (int j = 1; j <= i.getWidth(); ++j) {
      assertEquals(i, ef.next().hroll(j));
    }
    assertNull(ef.next());
    ef = new HorizontalRollEffect(i, true);
    for (int j = 1; j <= i.getWidth(); ++j) {
      assertEquals(i, ef.next().hroll(-j));
    }
    assertNull(ef.next());
  }

}

