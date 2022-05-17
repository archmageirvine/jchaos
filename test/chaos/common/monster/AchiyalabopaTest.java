package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 *
 * @author Sean A. Irvine
 */
public class AchiyalabopaTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Achiyalabopa();
  }

  public void test() {
    assertTrue(new Achiyalabopa().is(PowerUps.FLYING));
  }
}
