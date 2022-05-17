package chaos.common.dragon;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;

/**
 * Tests this monster.
 *
 * @author Sean A. Irvine
 */
public class EmeraldDragonTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new EmeraldDragon();
  }

}
