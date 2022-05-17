package chaos.common.free;

import chaos.common.Castable;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class FreePowerUpTest extends AbstractFreePowerUpTest {

  @Override
  public Castable getCastable() {
    return new Arborist();
  }

  public void testXX() {
    final Arborist d = new Arborist();
    assertEquals(Castable.CAST_SINGLE, d.getCastFlags());
    assertEquals(1, d.getPowerUpCount());
    assertFalse(d.cumulative());
  }
}
