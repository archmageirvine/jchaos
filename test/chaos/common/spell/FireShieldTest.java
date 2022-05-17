package chaos.common.spell;

import chaos.common.Castable;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class FireShieldTest extends AbstractPowerUpTest {

  @Override
  public Castable getCastable() {
    return new FireShield();
  }

}
