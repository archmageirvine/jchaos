package chaos.common.free;

import chaos.common.Castable;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class BlessTest extends AbstractIncrementTest {

  @Override
  public Castable getCastable() {
    return new Bless();
  }
}
