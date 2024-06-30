package chaos.common.monster;

import chaos.common.AbstractListCasterTest;
import chaos.common.Castable;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class PallasTest extends AbstractListCasterTest {

  @Override
  public Castable getCastable() {
    return new Pallas();
  }
}
