package chaos.common;

import chaos.common.free.AbstractFreeIncrementTest;
import chaos.common.free.Helmet;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class DummyFreeIncrementTest extends AbstractFreeIncrementTest {

  @Override
  public Castable getCastable() {
    return new Helmet();
  }
}
