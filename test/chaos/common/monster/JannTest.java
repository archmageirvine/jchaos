package chaos.common.monster;

import chaos.common.AbstractPolycasterTest;
import chaos.common.Castable;

/**
 * Tests this monster.
 *
 * @author Sean A. Irvine
 */
public class JannTest extends AbstractPolycasterTest {


  @Override
  public Castable getCastable() {
    return new Jann();
  }

}
