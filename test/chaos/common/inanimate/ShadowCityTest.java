package chaos.common.inanimate;

import chaos.common.AbstractActorTest;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Realm;

/**
 * Tests this inanimate.
 * @author Sean A. Irvine
 */
public class ShadowCityTest extends AbstractActorTest {

  @Override
  public Castable getCastable() {
    return new ShadowCity();
  }

  public void testCollapse() {
    final ShadowCity c = new ShadowCity();
    assertTrue(c.freeCollapse());
    assertEquals(63, c.get(Attribute.LIFE));
    assertEquals(100, c.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(4, c.getCastRange());
    assertEquals(Castable.CAST_EMPTY | Castable.CAST_LOS, c.getCastFlags());
    assertEquals(0x4266E77EFFFFE7E7L, c.getLosMask());
    assertEquals(1, c.getDefaultWeight());
    assertEquals(6, c.collapseFactor());
    assertEquals(10, c.getBonus());
    assertEquals(Realm.ETHERIC, c.getRealm());
  }

}
