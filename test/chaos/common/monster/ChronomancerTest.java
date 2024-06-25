package chaos.common.monster;

import chaos.common.AbstractUnicasterTest;
import chaos.common.Castable;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class ChronomancerTest extends AbstractUnicasterTest {


  @Override
  public Castable getCastable() {
    return new Chronomancer();
  }

}
