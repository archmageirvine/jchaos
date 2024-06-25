package chaos.common.monster;

import chaos.common.AbstractElementalTest;
import chaos.common.Castable;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class FireElementalTest extends AbstractElementalTest {


  @Override
  public Castable getCastable() {
    return new FireElemental();
  }

}
