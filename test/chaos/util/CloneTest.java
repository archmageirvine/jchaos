package chaos.util;

import chaos.common.Attribute;
import chaos.common.PowerUps;
import chaos.common.Realm;
import chaos.common.State;
import chaos.common.monster.Lion;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class CloneTest extends TestCase {

  public void testClone() {
    final Lion l = new Lion();
    l.set(PowerUps.REINCARNATE, l.reincarnation() != null ? 1 : 0);
    l.set(PowerUps.HORROR, 1);
    l.set(Attribute.LIFE, 1);
    l.setState(State.ASLEEP);
    l.setOwner(3);
    l.set(PowerUps.CLOAKED, 1);
    l.set(PowerUps.FLYING, 1);
    l.set(Attribute.LIFE_RECOVERY, 42);
    l.set(Attribute.MAGICAL_RESISTANCE_RECOVERY, 43);
    l.set(Attribute.MAGICAL_RESISTANCE, 44);
    l.set(PowerUps.BATTLE_CRY, 1);
    l.setRealm(Realm.DRACONIC);
    final Lion m = (Lion) Clone.clone(l);
    assertEquals(m.getState(), l.getState());
    assertEquals(m.getOwner(), l.getOwner());
    assertEquals(m.getRealm(), l.getRealm());
    assertEquals(m.get(Attribute.LIFE_RECOVERY), l.get(Attribute.LIFE_RECOVERY));
    assertEquals(m.get(Attribute.LIFE), l.get(Attribute.LIFE));
    assertEquals(m.get(Attribute.INTELLIGENCE_RECOVERY), l.get(Attribute.INTELLIGENCE_RECOVERY));
    assertEquals(m.get(Attribute.INTELLIGENCE), l.get(Attribute.INTELLIGENCE));
    assertEquals(m.get(Attribute.RANGE_RECOVERY), l.get(Attribute.RANGE_RECOVERY));
    assertEquals(m.get(Attribute.RANGE), l.get(Attribute.RANGE));
    assertEquals(m.get(Attribute.AGILITY_RECOVERY), l.get(Attribute.AGILITY_RECOVERY));
    assertEquals(m.get(Attribute.AGILITY), l.get(Attribute.AGILITY));
    assertEquals(m.get(Attribute.COMBAT_RECOVERY), l.get(Attribute.COMBAT_RECOVERY));
    assertEquals(m.get(Attribute.COMBAT), l.get(Attribute.COMBAT));
    assertEquals(m.get(Attribute.RANGED_COMBAT_RECOVERY), l.get(Attribute.RANGED_COMBAT_RECOVERY));
    assertEquals(m.get(Attribute.RANGED_COMBAT), l.get(Attribute.RANGED_COMBAT));
    assertEquals(m.get(Attribute.SPECIAL_COMBAT_RECOVERY), l.get(Attribute.SPECIAL_COMBAT_RECOVERY));
    assertEquals(m.get(Attribute.SPECIAL_COMBAT), l.get(Attribute.SPECIAL_COMBAT));
    assertEquals(m.get(Attribute.MAGICAL_RESISTANCE_RECOVERY), l.get(Attribute.MAGICAL_RESISTANCE_RECOVERY));
    assertEquals(m.get(Attribute.MAGICAL_RESISTANCE), l.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(m.get(PowerUps.HORROR), l.get(PowerUps.HORROR));
    assertEquals(m.is(PowerUps.CLOAKED), l.is(PowerUps.CLOAKED));
    assertEquals(m.is(PowerUps.FLYING), l.is(PowerUps.FLYING));
    assertEquals(1, l.get(PowerUps.BATTLE_CRY));
    assertEquals(m.is(PowerUps.REINCARNATE), l.is(PowerUps.REINCARNATE));
    assertNull(Clone.clone(null));
  }

}
