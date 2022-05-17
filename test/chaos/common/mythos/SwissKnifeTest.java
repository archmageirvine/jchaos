package chaos.common.mythos;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class SwissKnifeTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new SwissKnife();
  }
}
