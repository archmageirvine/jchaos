package chaos.common.free;

import chaos.common.Castable;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class CommandTest extends AbstractIncrementTest {

  @Override
  public Castable getCastable() {
    return new Command();
  }
}
