package chaos.common.free;

import chaos.common.Castable;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class BattleCryTest extends AbstractFreePowerUpTest {

  @Override
  public Castable getCastable() {
    return new BattleCry();
  }
}
