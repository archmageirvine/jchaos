package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.Monster;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 *
 * @author Sean A. Irvine
 */
public class CarnivorousApeTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new CarnivorousApe();
  }

  @Override
  public void testReincarnationState() {
    final Monster a = (Monster) getCastable();
    assertTrue(a.is(PowerUps.REINCARNATE));
  }
}
