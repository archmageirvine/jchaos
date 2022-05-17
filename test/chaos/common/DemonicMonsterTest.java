package chaos.common;

import chaos.common.monster.LesserDevil;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class DemonicMonsterTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new LesserDevil();
  }
}
