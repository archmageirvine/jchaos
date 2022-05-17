package chaos.common.free;

import chaos.common.Castable;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class LichLordTest extends AbstractFreePowerUpTest {

  @Override
  public Castable getCastable() {
    return new LichLord();
  }

  public void testCumulative() {
    assertTrue(new LichLord().cumulative());
  }
}
