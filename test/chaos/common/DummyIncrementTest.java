package chaos.common;

import chaos.common.free.AbstractIncrementTest;
import chaos.common.free.Bless;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class DummyIncrementTest extends AbstractIncrementTest {

  @Override
  public Castable getCastable() {
    return new Bless();
  }
}
