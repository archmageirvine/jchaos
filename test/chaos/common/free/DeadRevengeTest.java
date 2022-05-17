package chaos.common.free;

import chaos.common.Castable;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class DeadRevengeTest extends AbstractFreePowerUpTest {

  @Override
  public Castable getCastable() {
    return new DeadRevenge();
  }

}
