package chaos.common.free;

import chaos.common.Castable;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class HelmetTest extends AbstractFreeIncrementTest {

  @Override
  public Castable getCastable() {
    return new Helmet();
  }

}
