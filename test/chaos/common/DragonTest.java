package chaos.common;

import chaos.common.dragon.RedDragon;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class DragonTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new RedDragon();
  }

}
