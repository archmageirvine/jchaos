package chaos.common;

import chaos.common.monster.Ogre;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class MaterialMonsterTest extends TestCase {

  public void test() {
    final Ogre o = new Ogre();
    assertEquals(Realm.MATERIAL, o.getRealm());
    assertEquals(Attribute.LIFE, o.getCombatApply());
    assertEquals(Attribute.LIFE, o.getRangedCombatApply());
    assertEquals(Attribute.LIFE, o.getSpecialCombatApply());
  }
}
