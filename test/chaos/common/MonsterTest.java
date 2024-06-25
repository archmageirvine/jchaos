package chaos.common;

import chaos.board.World;
import chaos.common.monster.Ghost;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;

/**
 * Tests general properties of monsters.
 * @author Sean A. Irvine
 */
public class MonsterTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Ghost();
  }

  public void testSpecials() {
    Lion l = new Lion();
    l.setState(State.ACTIVE);
    l.set(Attribute.MOVEMENT_RECOVERY, -4);
    assertFalse(l.update(null, null));
    assertEquals(-4, l.get(Attribute.MOVEMENT_RECOVERY));
    assertEquals(0, l.get(Attribute.MOVEMENT));
    l.set(Attribute.MOVEMENT_RECOVERY, 4);
    assertFalse(l.update(null, null));
    assertEquals(4, l.get(Attribute.MOVEMENT_RECOVERY));
    assertEquals(l.getDefault(Attribute.MOVEMENT), l.get(Attribute.MOVEMENT));
    l.set(Attribute.AGILITY_RECOVERY, -4);
    assertFalse(l.update(null, null));
    assertEquals(-4, l.get(Attribute.AGILITY_RECOVERY));
    assertEquals(46, l.get(Attribute.AGILITY));
    l.set(Attribute.AGILITY_RECOVERY, 4);
    assertFalse(l.update(null, null));
    assertEquals(4, l.get(Attribute.AGILITY_RECOVERY));
    assertEquals(l.getDefault(Attribute.AGILITY), l.get(Attribute.AGILITY));
    l = new Lion();
    l.set(Attribute.MOVEMENT, -98);
    l.set(Attribute.MOVEMENT_RECOVERY, -4);
    assertFalse(l.update(null, null));
    assertEquals(0, l.get(Attribute.MOVEMENT));
    l.set(Attribute.MOVEMENT, Attribute.MOVEMENT.max() - 2);
    l.set(Attribute.MOVEMENT_RECOVERY, 4);
    l.update(null, null);
    assertEquals(Attribute.MOVEMENT.max() - 2, l.get(Attribute.MOVEMENT));
    for (final Attribute a : new Attribute[] {Attribute.AGILITY, Attribute.RANGE, Attribute.COMBAT, Attribute.RANGED_COMBAT, Attribute.SPECIAL_COMBAT}) {
      l = new Lion();
      l.set(a, a.max() - 2);
      l.set(a.recovery(), 4);
      l.update(null, null);
      assertEquals(a.max() - 2, l.get(a));
      l = new Lion();
      l.set(a, a.min() + 2);
      l.set(a.recovery(), -4);
      assertFalse(l.update(null, null));
      assertEquals(0, l.get(a));
      l.set(a, 4);
      l.set(a.recovery(), -4);
      assertFalse(l.update(null, null));
      assertEquals(0, l.get(a));
    }
    l = new Lion();
    l.set(Attribute.INTELLIGENCE, Attribute.INTELLIGENCE.max() - 2);
    l.set(Attribute.INTELLIGENCE_RECOVERY, 4);
    l.update(null, null);
    assertEquals(Attribute.INTELLIGENCE.max() - 2, l.get(Attribute.INTELLIGENCE));
    l = new Lion();
    l.set(Attribute.INTELLIGENCE, 0);
    l.set(Attribute.INTELLIGENCE_RECOVERY, -4);
    assertTrue(l.update(null, null));
    assertEquals(0, l.get(Attribute.INTELLIGENCE));
  }

  private static class MyLion extends Lion {
    {
      setDefault(Attribute.INTELLIGENCE, Attribute.INTELLIGENCE.max());
      setDefault(Attribute.AGILITY, Attribute.AGILITY.max());
      setDefault(Attribute.MOVEMENT, Attribute.MOVEMENT.max());
      setDefault(Attribute.RANGE, Attribute.RANGE.max());
      setDefault(Attribute.SPECIAL_COMBAT, Attribute.SPECIAL_COMBAT.max());
      setDefault(Attribute.RANGED_COMBAT, Attribute.RANGED_COMBAT.max());
      setDefault(Attribute.COMBAT, Attribute.COMBAT.max());
    }
  }

  public void testCombat1() {
    final Lion l = new MyLion();
    for (final Attribute a : new Attribute[] {Attribute.INTELLIGENCE, Attribute.SPECIAL_COMBAT, Attribute.RANGED_COMBAT, Attribute.COMBAT, Attribute.AGILITY, Attribute.MOVEMENT, Attribute.RANGE}) {
      l.set(a, a.max() - 2);
      l.set(a.recovery(), 4);
      assertFalse(l.update(null, null));
      assertEquals(a.max(), l.get(a));
    }
  }

  private static class YourLion extends Lion {
    {
      setDefault(Attribute.INTELLIGENCE, Attribute.INTELLIGENCE.max());
      setDefault(Attribute.AGILITY, Attribute.AGILITY.max());
      setDefault(Attribute.MOVEMENT, Attribute.MOVEMENT.min());
      setDefault(Attribute.RANGE, Attribute.RANGE.max());
      setDefault(Attribute.SPECIAL_COMBAT, Attribute.SPECIAL_COMBAT.min());
      setDefault(Attribute.RANGED_COMBAT, Attribute.RANGED_COMBAT.min());
      setDefault(Attribute.COMBAT, Attribute.COMBAT.min());
    }
  }

  public void testLion1() {
    final Lion l = new YourLion();
    l.set(Attribute.SPECIAL_COMBAT, Attribute.SPECIAL_COMBAT.min() + 2);
    l.set(Attribute.SPECIAL_COMBAT_RECOVERY, 4);
    l.set(Attribute.RANGED_COMBAT, Attribute.RANGED_COMBAT.min() + 2);
    l.set(Attribute.RANGED_COMBAT_RECOVERY, 4);
    l.set(Attribute.COMBAT, Attribute.COMBAT.min() + 2);
    l.set(Attribute.COMBAT_RECOVERY, 4);
    l.set(Attribute.AGILITY, 2);
    l.set(Attribute.AGILITY_RECOVERY, -4);
    l.set(Attribute.RANGE, Attribute.RANGE.min() + 2);
    l.set(Attribute.RANGE_RECOVERY, -4);
    l.set(Attribute.INTELLIGENCE_RECOVERY, -4);
    l.set(Attribute.INTELLIGENCE, Attribute.INTELLIGENCE.min() + 2);
    l.update(null, null);
    assertEquals(0, l.get(Attribute.AGILITY));
    assertEquals(0, l.get(Attribute.RANGE));
    assertEquals(Attribute.RANGED_COMBAT.min(), l.get(Attribute.RANGED_COMBAT));
    assertEquals(Attribute.SPECIAL_COMBAT.min(), l.get(Attribute.SPECIAL_COMBAT));
    assertEquals(Attribute.COMBAT.min(), l.get(Attribute.COMBAT));
    assertEquals(0, l.get(Attribute.INTELLIGENCE));
  }

  private static class HisLion extends Lion {
    {
      setDefault(Attribute.INTELLIGENCE, Attribute.INTELLIGENCE.max());
      setDefault(Attribute.AGILITY, Attribute.AGILITY.min());
      setDefault(Attribute.MOVEMENT, Attribute.MOVEMENT.min());
      setDefault(Attribute.RANGE, Attribute.RANGE.min());
      setDefault(Attribute.SPECIAL_COMBAT, Attribute.SPECIAL_COMBAT.min());
      setDefault(Attribute.RANGED_COMBAT, Attribute.RANGED_COMBAT.min());
      setDefault(Attribute.COMBAT, Attribute.COMBAT.min());
    }
  }

  public void testCombat2() {
    final Lion l = new HisLion();
    l.set(Attribute.SPECIAL_COMBAT, Attribute.SPECIAL_COMBAT.min());
    l.set(Attribute.SPECIAL_COMBAT_RECOVERY, -4);
    l.set(Attribute.RANGED_COMBAT, Attribute.RANGED_COMBAT.min());
    l.set(Attribute.RANGED_COMBAT_RECOVERY, -4);
    l.set(Attribute.COMBAT, Attribute.COMBAT.min());
    l.set(Attribute.COMBAT_RECOVERY, -4);
    l.update(null, null);
    assertEquals(Attribute.RANGED_COMBAT.min() + 4, l.get(Attribute.RANGED_COMBAT));
    assertEquals(Attribute.SPECIAL_COMBAT.min() + 4, l.get(Attribute.SPECIAL_COMBAT));
    assertEquals(Attribute.COMBAT.min() + 4, l.get(Attribute.COMBAT));
    l.set(Attribute.SPECIAL_COMBAT, Attribute.SPECIAL_COMBAT.max());
    l.set(Attribute.SPECIAL_COMBAT_RECOVERY, -4);
    l.set(Attribute.RANGED_COMBAT, Attribute.RANGED_COMBAT.max());
    l.set(Attribute.RANGED_COMBAT_RECOVERY, -4);
    l.set(Attribute.COMBAT, Attribute.COMBAT.max());
    l.set(Attribute.COMBAT_RECOVERY, -4);
    l.update(null, null);
    assertEquals(Attribute.RANGED_COMBAT.max(), l.get(Attribute.RANGED_COMBAT));
    assertEquals(Attribute.SPECIAL_COMBAT.max(), l.get(Attribute.SPECIAL_COMBAT));
    assertEquals(Attribute.COMBAT.max(), l.get(Attribute.COMBAT));
  }

  private static class MyMonster extends Monster {
    @Override
    public Class<? extends Monster> reincarnation() {
      return null;
    }

    @Override
    public long getLosMask() {
      return 0;
    }
  }

  public void testDefaultsA() {
    final Monster m = new MyMonster();
    for (final Attribute a : Attribute.values()) {
      assertEquals(0, m.getDefault(a));
    }
    assertFalse(m.is(PowerUps.FLYING));
    assertFalse(m.is(PowerUps.ARCHERY));
    m.set(PowerUps.REINCARNATE, m.reincarnation() != null ? 1 : 0);
    assertFalse(m.is(PowerUps.REINCARNATE));
    m.cast(null, null, null, null);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(3);
    wiz.set(PowerUps.TORMENT, 1);
    m.set(Attribute.LIFE, 20);
    m.cast(null, wiz, null, null);
    assertEquals(3, m.getOwner());
    assertEquals(4, m.get(Attribute.LIFE));
    m.set(Attribute.LIFE, 2);
    m.cast(null, wiz, null, null);
    assertEquals(3, m.getOwner());
    assertEquals(2, m.get(Attribute.LIFE));
    final World w = new World(1, 1);
    w.getCell(0).push(wiz);
    m.cast(null, wiz, null, w.getCell(0));
  }

  public void testIncrement() {
    final Lion w = new Lion();
    w.increment((Attribute) null, 5);
    for (final Attribute a : new Attribute[] {Attribute.MOVEMENT, Attribute.INTELLIGENCE, Attribute.AGILITY, Attribute.COMBAT, Attribute.RANGED_COMBAT, Attribute.SPECIAL_COMBAT, Attribute.RANGE}) {
      final int m = w.get(a);
      w.increment(a, 5);
      assertEquals(m + 5, w.get(a));
      assertEquals(m + 5, w.get(a));
      assertFalse(w.decrement(a, 5));
      assertEquals(m, w.get(a));
      assertEquals(m, w.get(a));
      assertEquals(w.getDefault(a), w.getDefault(a));
      w.set(a, 2);
      if (a == Attribute.MAGICAL_RESISTANCE || a == Attribute.INTELLIGENCE) {
        assertTrue(w.decrement(a, 5));
      } else {
        assertFalse(w.decrement(a, 5));
      }
      assertEquals(0, w.get(a));
      final Attribute ar = a.recovery();
      final int mr = w.get(ar);
      w.increment(ar, 5);
      assertEquals(mr + 5, w.get(ar));
      assertEquals(mr + 5, w.get(ar));
      assertFalse(w.decrement(ar, 5));
      assertEquals(mr, w.get(ar));
      assertEquals(mr, w.get(ar));
      assertEquals(w.getDefault(ar), w.getDefault(ar));
      w.set(ar, ar.min());
      assertFalse(w.decrement(ar, 5));
      assertEquals(ar.min(), w.get(ar));
    }
  }
}
