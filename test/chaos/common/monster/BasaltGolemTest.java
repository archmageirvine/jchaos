package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Attribute;
import chaos.common.Castable;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class BasaltGolemTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new BasaltGolem();
  }

  public void test() {
    assertEquals(Attribute.MOVEMENT, new BasaltGolem().getRangedCombatApply());
  }
}
