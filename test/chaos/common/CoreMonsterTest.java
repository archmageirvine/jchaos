package chaos.common;

import chaos.common.inanimate.Pool;
import junit.framework.TestCase;

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
