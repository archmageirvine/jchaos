package chaos.common.dragon;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class GoldenDragonTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new GoldenDragon();
  }

}
