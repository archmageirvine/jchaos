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
public class WisdomTest extends AbstractShieldTest {

  @Override
  public Castable getCastable() {
    return new Wisdom();
  }

  public void testInc() {
    final AbstractShield s = (AbstractShield) getCastable();
    assertEquals(Castable.MAX_CAST_RANGE, s.getCastRange());
    assertEquals(Attribute.INTELLIGENCE.max(), s.increment());
    assertEquals(Castable.CAST_LIVING, s.getCastFlags());
    assertEquals(3, ((Wisdom) s).getMultiplicity());
  }
}
