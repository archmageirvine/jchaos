package chaos.common.free;

import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class AmnesiaTest extends TormentTest {

  @Override
  public Castable getCastable() {
    return new Amnesia();
  }

  @Override
  public void testCast() {
    check(new Amnesia(), PowerUps.AMNESIA);
  }
}
