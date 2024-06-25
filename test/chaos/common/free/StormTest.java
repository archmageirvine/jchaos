package chaos.common.free;

import chaos.common.Castable;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class StormTest extends PyrotechnicsTest {

  @Override
  public Castable getCastable() {
    return new Storm();
  }

  @Override
  public void testNasty() {
  }
}
