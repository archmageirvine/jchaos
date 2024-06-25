package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class DarkElfTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new DarkElf();
  }

  public void testExtra() {
    assertTrue(new DarkElf().is(PowerUps.ARCHERY));
    assertTrue(new DarkElf().is(PowerUps.REFLECT));
    assertTrue(new DarkElf().is(PowerUps.ATTACK_ANY_REALM));
  }
}
