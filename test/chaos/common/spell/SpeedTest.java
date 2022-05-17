package chaos.common.spell;

import chaos.common.AbstractShield;
import chaos.common.AbstractShieldTest;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Castable;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class SpeedTest extends AbstractShieldTest {

  @Override
  public Castable getCastable() {
    return new Speed();
  }

  public void testInc() {
    final AbstractShield s = (AbstractShield) getCastable();
    assertEquals(Actor.MAX_CAST_RANGE, s.getCastRange());
    assertEquals(Castable.CAST_LIVING, s.getCastFlags());
    assertEquals(Attribute.MOVEMENT.max(), s.increment());
    assertEquals(3, ((Speed) s).getMultiplicity());
  }
}
