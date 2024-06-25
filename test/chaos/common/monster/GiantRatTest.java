package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Attribute;
import chaos.common.Castable;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class GiantRatTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new GiantRat();
  }

  public void test() {
    assertEquals(Attribute.LIFE_RECOVERY, new GiantRat().getCombatApply());
  }
}
