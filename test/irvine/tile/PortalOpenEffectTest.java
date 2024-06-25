package irvine.tile;


/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class PortalOpenEffectTest extends AbstractTileEffectTest {

  @Override
  public TileEffect getEffect() {
    return new PortalOpenEffect(new TileImage(2, 3));
  }

  public void testBadCons() {
    try {
      new PortalOpenEffect(null);
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
    final TileEffect ef = new PortalOpenEffect(i);
    for (int j = 0; j <= i.getWidth() / 2; ++j) {
      assertNotNull(ef.next());
    }
    assertNull(ef.next());
  }

}

