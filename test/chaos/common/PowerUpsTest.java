package chaos.common;

import irvine.TestUtils;
import junit.framework.TestCase;
import chaos.common.free.MagicWand;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class PowerUpsTest extends TestCase {

  public void testEnum() {
    TestUtils.testEnum(PowerUps.class, "[INVULNERABLE, BATTLE_CRY, MOVE_IT, UNCERTAINTY, LICH_LORD, BOW, SWORD, DEAD_REVENGE, RIDE, DOUBLE, TRIPLE, ARBORIST, FIRE_SHIELD, FLOOD_SHIELD, EARTHQUAKE_SHIELD, FROZEN, DEPTH, REFLECT, AMNESIA, LEVEL, FLYING, ATTACK_ANY_REALM, COERCION, TORMENT, NECROPOTENCE, CONFIDENCE, WAND, CRYSTAL_BALL, ARCHERY, CLOAKED, HORROR, REINCARNATE, TALISMAN, LIFE_LEECH, NO_GROW, NO_MOUNT]");
    assertTrue(PowerUps.WAND.getCastable() instanceof MagicWand);
  }

}
