package chaos.common;

import chaos.common.inanimate.StandardWall;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class DummyWallTest extends AbstractActorTest {


  @Override
  public Castable getCastable() {
    return new StandardWall();
  }

  public void test() {
    final StandardWall w = new StandardWall();
    assertEquals(0, w.getDefaultWeight());
    assertEquals(~0L, w.getLosMask());
    assertEquals(Castable.CAST_GROWTH | Castable.CAST_EMPTY | Castable.CAST_LOS, w.getCastFlags());
    w.cast(null, null, null, null);
  }
}
