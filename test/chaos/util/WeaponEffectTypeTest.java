package chaos.util;

import irvine.TestUtils;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class WeaponEffectTypeTest extends TestCase {


  public void test() {
    TestUtils.testEnum(WeaponEffectType.class, "[NON_EVENT, MONSTER_CAST_EVENT, TREE_CAST_EVENT, STONE_CAST_EVENT, BRAIN_BEAM_EVENT, RANGED_COMBAT_EVENT, LIGHTNING, KILL_BEAM, ICE_BEAM, LINE, UNLINE, PLASMA, BALL, FIREBALL, SPINNER, THUNDERBOLT, DEMOTION, PROMOTION]");
  }

}
