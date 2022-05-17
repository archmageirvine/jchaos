package chaos.common;

import junit.framework.TestCase;
import chaos.common.monster.Ghost;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class UndeadMonsterTest extends TestCase {

  public void test() {
    final Ghost o = new Ghost();
    assertEquals(0, o.getLosMask());
    assertEquals(Realm.ETHERIC, o.getRealm());
    assertEquals(Attribute.LIFE, o.getCombatApply());
    assertEquals(Attribute.LIFE, o.getRangedCombatApply());
    assertEquals(Attribute.LIFE, o.getSpecialCombatApply());
  }
}
