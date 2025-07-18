package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class TriffidTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Triffid();
  }
}
