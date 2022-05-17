package chaos.common.spell;

import chaos.common.AbstractShield;
import chaos.common.AbstractShieldTest;
import chaos.common.Attribute;
import chaos.common.Castable;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class ProtectionTest extends AbstractShieldTest {

  @Override
  public Castable getCastable() {
    return new Protection();
  }

  public void testInc() {
    final AbstractShield s = (AbstractShield) getCastable();
    assertEquals(15, s.getCastRange());
    assertEquals(Attribute.MAGICAL_RESISTANCE.max(), s.increment());
    assertEquals(Castable.CAST_LIVING, s.getCastFlags());
    assertEquals(3, ((Protection) s).getMultiplicity());
  }
}
