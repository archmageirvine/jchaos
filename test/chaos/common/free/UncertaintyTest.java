package chaos.common.free;

import chaos.common.Castable;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class UncertaintyTest extends AbstractFreePowerUpTest {

  @Override
  public Castable getCastable() {
    return new Uncertainty();
  }

  public void testCumulative() {
    assertEquals(6, new Uncertainty().getPowerUpCount());
    assertTrue(new Uncertainty().cumulative());
  }
}
