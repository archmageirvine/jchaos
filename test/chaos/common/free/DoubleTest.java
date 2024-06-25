package chaos.common.free;

import chaos.common.Castable;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class DoubleTest extends AbstractFreePowerUpTest {

  @Override
  public Castable getCastable() {
    return new chaos.common.free.Double();
  }

  public void testCumulative() {
    assertEquals(5, new chaos.common.free.Double().getPowerUpCount());
    assertTrue(new chaos.common.free.Double().cumulative());
  }
}
