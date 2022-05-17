package chaos.common.spell;

import chaos.common.AbstractShieldTest;
import chaos.common.Castable;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class QuickshotTest extends AbstractShieldTest {

  @Override
  public Castable getCastable() {
    return new Quickshot();
  }
}
