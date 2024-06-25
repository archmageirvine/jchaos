package chaos.common.free;

import chaos.common.Castable;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class MagicShieldTest extends AbstractFreeIncrementTest {

  @Override
  public Castable getCastable() {
    return new MagicShield();
  }
}
