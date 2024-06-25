package chaos.common.free;

import chaos.common.Castable;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class MagicWandTest extends AbstractFreePowerUpTest {

  @Override
  public Castable getCastable() {
    return new MagicWand();
  }

  public void testCumulative() {
    assertEquals(2, new MagicWand().getPowerUpCount());
    assertTrue(new MagicWand().cumulative());
  }

}
