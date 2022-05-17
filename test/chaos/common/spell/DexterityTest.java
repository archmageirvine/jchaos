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
public class DexterityTest extends AbstractShieldTest {

  @Override
  public Castable getCastable() {
    return new Dexterity();
  }

  public void testInc() {
    final AbstractShield s = (AbstractShield) getCastable();
    assertEquals(Actor.MAX_CAST_RANGE, s.getCastRange());
    assertEquals(Castable.CAST_LIVING, s.getCastFlags());
    assertEquals(Attribute.AGILITY.max(), s.increment());
    assertEquals(3, ((Dexterity) s).getMultiplicity());
  }
}
