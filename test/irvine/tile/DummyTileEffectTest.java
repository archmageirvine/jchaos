package irvine.tile;

import java.util.List;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class DummyTileEffectTest extends AbstractTileEffectTest {




  @Override
  public TileEffect getEffect() {
    return new ExplodingCircleEffect(16, 23, 57);
  }

  public void testSize16all() {
    final TileEffect ef = getEffect();
    final List<TileImage> list = ef.list();
    assertEquals(15, list.size());
  }

  public void testSize16b() {
    final TileEffect ef = getEffect();
    for (int i = 0; i < 7; ++i) {
      assertNotNull(ef.next());
    }
    assertEquals(15 - 7, ef.list().size());
  }

}

