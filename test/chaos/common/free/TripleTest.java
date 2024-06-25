package chaos.common.free;

import chaos.common.Castable;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class TripleTest extends AbstractFreePowerUpTest {

  @Override
  public Castable getCastable() {
    return new Triple();
  }

  public void testCumulative() {
    assertEquals(5, new Triple().getPowerUpCount());
    assertTrue(new Triple().cumulative());
  }
}
