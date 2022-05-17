package chaos.common;

import junit.framework.TestCase;
import chaos.common.mythos.MrStrong;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class MythosMonsterTest extends TestCase {

  public void test() {
    final MrStrong o = new MrStrong();
    assertEquals(Realm.MYTHOS, o.getRealm());
    assertEquals(Attribute.LIFE, o.getCombatApply());
    assertEquals(Attribute.LIFE, o.getRangedCombatApply());
    assertEquals(Attribute.LIFE, o.getSpecialCombatApply());
  }
}
