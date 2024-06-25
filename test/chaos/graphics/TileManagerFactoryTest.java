package chaos.graphics;

import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class TileManagerFactoryTest extends TestCase {

  public void test() {
    try {
      TileManagerFactory.getTileManager("nosuchmanager");
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Unknown tile manager: nosuchmanager", e.getMessage());
    }
    assertNotNull(TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE16));
    assertNotNull(TileManagerFactory.getTileManager(TileManagerFactory.ACTIVE32));
  }
}
