package chaos.common;

import chaos.common.monster.Elephant;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class MaterialMonsterRideTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new Elephant();
  }
}
