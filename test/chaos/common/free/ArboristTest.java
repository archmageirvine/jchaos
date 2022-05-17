package chaos.common.free;

import chaos.common.Castable;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class ArboristTest extends AbstractFreePowerUpTest {

  @Override
  public Castable getCastable() {
    return new Arborist();
  }

}
