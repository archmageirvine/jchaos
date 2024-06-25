package chaos.common.spell;

import chaos.common.Castable;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class DummyPowerUpTest extends AbstractPowerUpTest {

  @Override
  public Castable getCastable() {
    return new FloodShield();
  }

}
