package chaos.util;

import irvine.TestUtils;
import junit.framework.TestCase;

/**
 * JUnit tests for the corresponding class.
 * @author Sean A. Irvine
 */
public class CellEffectTypeTest extends TestCase {

  public void test() {
    TestUtils.testEnum(CellEffectType.class, "[NON_EVENT, ATTACK_EVENT, MOVEMENT_EVENT, HIGHLIGHT_EVENT, MONSTER_CAST_EVENT, CORPSE_EXPLODE, TWIRL, FADE_TO_RED, POWERUP, ACQUISITION, WHITE_CIRCLE_EXPLODE, GREEN_CIRCLE_EXPLODE, OWNER_CHANGE, SHIELD_GRANTED, SHIELD_DESTROYED, TEAM_CHANGE, SPUNGER, DEATH, SLEEP, WARP_OUT, WARP_IN, REINCARNATE, CHANGE_REALM, WIZARD_EXPLODE, REDRAW_CELL, RAISE_DEAD, BONUS, BOMB, ICE_BOMB, MEDITATION_COLLAPSE, POISON, EXPERIENCE, EARTHQUAKE, AUDIO, SPELL_FAILED, ORANGE_CIRCLE_EXPLODE, FIREBALL_EXPLODE, RING]");
  }
}
