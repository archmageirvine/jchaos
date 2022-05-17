package irvine.tile;


/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class ReverseEffectTest extends AbstractTileEffectTest {




  @Override
  public TileEffect getEffect() {
    return new ReverseEffect(new TwirlEffect(16, 0, ~0, 1, 30));
  }

  public void testBadCons() {
    try {
      new ReverseEffect(null);
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
  }

  public void testSize16() {
    final TileEffect ef = getEffect();
    assertEquals(1048626, ef.next().hashCode());
    assertEquals(1048592, ef.next().hashCode());
    assertEquals(1048592, ef.next().hashCode());
    assertEquals(1048592, ef.next().hashCode());
    assertEquals(1048660, ef.next().hashCode());
    assertEquals(1048592, ef.next().hashCode());
    assertNull(ef.next());
  }

}

