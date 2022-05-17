package chaos.common;

import junit.framework.TestCase;
import chaos.common.inanimate.Pool;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class CoreMonsterTest extends TestCase {

  public void test() {
    final Pool o = new Pool();
    assertEquals(Realm.CORE, o.getRealm());
    assertEquals(Attribute.LIFE, o.getCombatApply());
    assertEquals(Attribute.LIFE, o.getRangedCombatApply());
    assertEquals(Attribute.LIFE, o.getSpecialCombatApply());
  }
}
