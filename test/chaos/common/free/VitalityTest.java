package chaos.common.free;

import chaos.common.Castable;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class VitalityTest extends AbstractIncrementTest {

  @Override
  public Castable getCastable() {
    return new Vitality();
  }
}
