package chaos.common.spell;

import chaos.common.AbstractShield;
import chaos.common.AbstractShieldTest;
import chaos.common.Castable;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class SanctifyTest extends AbstractShieldTest {

  @Override
  public Castable getCastable() {
    return new Sanctify();
  }

  public void testInc() {
    final AbstractShield s = (AbstractShield) getCastable();
    assertEquals(12, s.getCastRange());
    assertEquals(7, s.increment());
    assertEquals(Castable.CAST_LIVING | Castable.CAST_GROWTH, s.getCastFlags());
  }
}
