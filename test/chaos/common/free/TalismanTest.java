package chaos.common.free;

import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class TalismanTest extends AbstractFreeIncrementTest {

  @Override
  public Castable getCastable() {
    return new Talisman();
  }

  public void testPowerUpIncrements() {
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    assertEquals(0, w.get(Attribute.SPECIAL_COMBAT));
    new Talisman().cast(null, w, null);
    assertEquals(1, w.get(PowerUps.TALISMAN));
    assertEquals(5, w.get(Attribute.SPECIAL_COMBAT));
    new Talisman().cast(null, w, null);
    assertEquals(2, w.get(PowerUps.TALISMAN));
    assertEquals(5, w.get(Attribute.SPECIAL_COMBAT));
  }
}
