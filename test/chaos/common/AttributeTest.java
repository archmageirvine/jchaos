package chaos.common;

import java.awt.Color;

import irvine.TestUtils;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class AttributeTest extends TestCase {

  public void testEnum() {
    TestUtils.testEnum(Attribute.class, "[LIFE_RECOVERY, LIFE, MOVEMENT_RECOVERY, MOVEMENT, INTELLIGENCE_RECOVERY, INTELLIGENCE, AGILITY_RECOVERY, AGILITY, MAGICAL_RESISTANCE_RECOVERY, MAGICAL_RESISTANCE, COMBAT_RECOVERY, COMBAT, RANGE_RECOVERY, RANGE, RANGED_COMBAT_RECOVERY, RANGED_COMBAT, SPECIAL_COMBAT_RECOVERY, SPECIAL_COMBAT, SHOTS]");
    assertEquals(Color.RED, Attribute.LIFE.getColor());
    assertEquals(new Color(0x7F0000), Attribute.LIFE_RECOVERY.getColor());
    assertEquals(Color.BLUE, Attribute.MOVEMENT.getColor());
    assertEquals(Color.CYAN, Attribute.AGILITY.getColor());
    assertEquals(new Color(0x007F7F), Attribute.AGILITY_RECOVERY.getColor());
    assertEquals(Color.GREEN, Attribute.RANGED_COMBAT.getColor());
    assertEquals(Color.GREEN, Attribute.RANGE.getColor());
    assertEquals(Color.GREEN, Attribute.SHOTS.getColor());
    assertEquals(new Color(0x7F7F00), Attribute.MAGICAL_RESISTANCE_RECOVERY.getColor());
    assertEquals(Color.YELLOW, Attribute.MAGICAL_RESISTANCE.getColor());
    assertEquals(Color.MAGENTA, Attribute.SPECIAL_COMBAT.getColor());
    assertEquals(new Color(0x7F007F), Attribute.SPECIAL_COMBAT_RECOVERY.getColor());
    assertEquals(new Color(0x007F00), Attribute.COMBAT.getColor());
    assertEquals(new Color(0x007F00), Attribute.RANGE_RECOVERY.getColor());
    assertEquals(new Color(0x007F00), Attribute.RANGED_COMBAT_RECOVERY.getColor());
    assertEquals(new Color(0x003F00), Attribute.COMBAT_RECOVERY.getColor());
    assertEquals(new Color(0xECC089), Attribute.INTELLIGENCE.getColor());
    assertEquals(new Color(0x7C6049), Attribute.INTELLIGENCE_RECOVERY.getColor());
    assertEquals(new Color(0x00007F), Attribute.MOVEMENT_RECOVERY.getColor());
    assertEquals(Attribute.LIFE_RECOVERY, Attribute.LIFE.recovery());
    assertEquals(null, Attribute.LIFE_RECOVERY.recovery());
    assertEquals(Attribute.MOVEMENT_RECOVERY, Attribute.MOVEMENT.recovery());
    assertEquals(null, Attribute.MOVEMENT_RECOVERY.recovery());
    assertEquals(Attribute.INTELLIGENCE_RECOVERY, Attribute.INTELLIGENCE.recovery());
    assertEquals(null, Attribute.INTELLIGENCE_RECOVERY.recovery());
    assertEquals(Attribute.AGILITY_RECOVERY, Attribute.AGILITY.recovery());
    assertEquals(null, Attribute.AGILITY_RECOVERY.recovery());
    assertEquals(Attribute.MAGICAL_RESISTANCE_RECOVERY, Attribute.MAGICAL_RESISTANCE.recovery());
    assertEquals(null, Attribute.MAGICAL_RESISTANCE_RECOVERY.recovery());
    assertEquals(Attribute.COMBAT_RECOVERY, Attribute.COMBAT.recovery());
    assertEquals(null, Attribute.COMBAT_RECOVERY.recovery());
    assertEquals(Attribute.SPECIAL_COMBAT_RECOVERY, Attribute.SPECIAL_COMBAT.recovery());
    assertEquals(null, Attribute.SPECIAL_COMBAT_RECOVERY.recovery());
    assertEquals(Attribute.RANGED_COMBAT_RECOVERY, Attribute.RANGED_COMBAT.recovery());
    assertEquals(null, Attribute.RANGED_COMBAT_RECOVERY.recovery());
    assertEquals(Attribute.RANGE_RECOVERY, Attribute.RANGE.recovery());
    assertEquals(null, Attribute.RANGE_RECOVERY.recovery());
    for (final Attribute a : Attribute.values()) {
      assertTrue(a.min() < a.max());
    }
  }
}
