package irvine.tile;

import java.util.List;

import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public abstract class AbstractTileEffectTest extends TestCase {


  public abstract TileEffect getEffect();

  public void testListMethod() {
    final TileEffect ef2 = getEffect();
    final List<TileImage> list = ef2.list();
    final int[] hashes = new int[list.size()];
    for (int i = 0; i < hashes.length; ++i) {
      hashes[i] = list.get(i).hashCode();
    }
    final TileEffect ef = getEffect();
    int c = 0;
    TileImage i;
    while ((i = ef.next()) != null) {
      assertEquals(hashes[c++], i.hashCode());
    }
    assertEquals(c, hashes.length);
  }

}

