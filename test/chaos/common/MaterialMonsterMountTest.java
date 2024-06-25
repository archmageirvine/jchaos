package chaos.common;

import chaos.common.monster.Horse;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class MaterialMonsterMountTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new Horse();
  }
}
