package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Attribute;
import chaos.common.Castable;

/**
 * Tests this monster.
 *
 * @author Sean A. Irvine
 */
public class MindFlayerTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new MindFlayer();
  }

  public void test() {
    final MindFlayer a = new MindFlayer();
    assertEquals(Attribute.COMBAT, a.getCombatApply());
    assertEquals(Attribute.INTELLIGENCE, a.getRangedCombatApply());
    assertEquals(Attribute.INTELLIGENCE, a.getSpecialCombatApply());
  }

}
