package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 *
 * @author Sean A. Irvine
 */
public class SpectatorTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Spectator();
  }

  public void test() {
    assertTrue(new Spectator().is(PowerUps.FLYING));
    assertEquals(Attribute.AGILITY, new Spectator().getCombatApply());
  }
}
