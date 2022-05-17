package chaos.common.inanimate;

import chaos.common.AbstractActorTest;
import chaos.common.Attribute;
import chaos.common.Castable;

/**
 * Tests this inanimate.
 *
 * @author Sean A. Irvine
 */
public class DarkCitadelTest extends AbstractActorTest {


  @Override
  public Castable getCastable() {
    return new DarkCitadel();
  }

  public void testCollapse() {
    final DarkCitadel c = new DarkCitadel();
    assertTrue(c.freeCollapse());
    assertEquals(63, c.get(Attribute.LIFE));
    assertEquals(100, c.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(8, c.getCastRange());
    assertEquals(Castable.CAST_DEAD | Castable.CAST_EMPTY | Castable.CAST_LOS, c.getCastFlags());
    assertEquals(0xFFFFFFFFFFFFFFFFL, c.getLosMask());
    assertEquals(1, c.getDefaultWeight());
    assertEquals(6, c.collapseFactor());
  }

}
