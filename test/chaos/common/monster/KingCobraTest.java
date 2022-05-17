package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Attribute;
import chaos.common.Castable;

/**
 * Tests this monster.
 *
 * @author Sean A. Irvine
 */
public class KingCobraTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new KingCobra();
  }

  public void test() {
    assertEquals(Attribute.LIFE_RECOVERY, new KingCobra().getCombatApply());
    assertEquals(Attribute.LIFE_RECOVERY, new KingCobra().getRangedCombatApply());
  }
}
