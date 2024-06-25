package chaos.util;

import chaos.common.Attribute;
import chaos.common.State;
import chaos.common.monster.Lion;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class RestoreTest extends TestCase {

  public void testRestore() {
    Restore.restore(null);
    final Lion l = new Lion();
    l.set(Attribute.LIFE, 1);
    l.setState(State.ASLEEP);
    l.setOwner(3);
    l.set(Attribute.LIFE_RECOVERY, 42);
    l.set(Attribute.MAGICAL_RESISTANCE_RECOVERY, 43);
    l.set(Attribute.MAGICAL_RESISTANCE, 44);
    l.set(Attribute.AGILITY, 11);
    l.set(Attribute.AGILITY_RECOVERY, 12);
    l.set(Attribute.INTELLIGENCE, 13);
    l.set(Attribute.INTELLIGENCE_RECOVERY, 14);
    l.set(Attribute.COMBAT, 15);
    l.set(Attribute.COMBAT_RECOVERY, 16);
    l.set(Attribute.RANGE, 17);
    l.set(Attribute.RANGE_RECOVERY, 18);
    l.set(Attribute.MOVEMENT, 19);
    l.set(Attribute.MOVEMENT_RECOVERY, 20);
    l.set(Attribute.RANGED_COMBAT, 21);
    l.set(Attribute.RANGED_COMBAT_RECOVERY, 22);
    l.set(Attribute.SPECIAL_COMBAT, 23);
    l.set(Attribute.SPECIAL_COMBAT_RECOVERY, 24);
    Restore.restore(l);
    for (final Attribute a : Attribute.values()) {
      assertEquals(l.getDefault(a), l.get(a));
    }
    assertEquals(State.ACTIVE, l.getState());
    assertEquals(3, l.getOwner());
  }
}
