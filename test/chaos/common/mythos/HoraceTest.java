package chaos.common.mythos;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class HoraceTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Horace();
  }

  @Override
  public void testHorrorState() {
    assertEquals(2, new Horace().get(PowerUps.HORROR));
  }
}
