package chaos.common.spell;

import chaos.common.Castable;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class ReflectorTest extends AbstractPowerUpTest {

  @Override
  public Castable getCastable() {
    return new Reflector();
  }
}
