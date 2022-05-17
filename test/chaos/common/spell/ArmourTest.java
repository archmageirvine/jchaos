package chaos.common.spell;

import chaos.common.AbstractShield;
import chaos.common.AbstractShieldTest;
import chaos.common.Castable;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class ArmourTest extends AbstractShieldTest {

  @Override
  public Castable getCastable() {
    return new Armour();
  }

  public void testInc() {
    final AbstractShield s = (AbstractShield) getCastable();
    assertEquals(Castable.MAX_CAST_RANGE, s.getCastRange());
    assertEquals(50, s.increment());
    assertEquals(Castable.CAST_LIVING | Castable.CAST_GROWTH, s.getCastFlags());
  }
}
