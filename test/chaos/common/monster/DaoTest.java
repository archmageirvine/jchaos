package chaos.common.monster;

import chaos.common.AbstractPolycasterTest;
import chaos.common.Castable;

/**
 * Tests this monster.
 *
 * @author Sean A. Irvine
 */
public class DaoTest extends AbstractPolycasterTest {


  @Override
  public Castable getCastable() {
    return new Dao();
  }

}
