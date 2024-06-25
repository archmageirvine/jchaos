package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class IridiumTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Iridium();
  }
}
