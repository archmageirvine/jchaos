package chaos.common;

import chaos.common.inanimate.Web;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard;
import chaos.common.wizard.Wizard1;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class ActorTest extends AbstractActorTest {

  @Override
  public Castable getCastable() {
    return new Lion();
  }

  public void testAgainstSource() {
    AbstractMonsterTest.checkAgainstSource(getActor());
  }

  /** Test statistical behaviour of sleepers. */
  public void testAsleepUpdate() {
    final int[] c = new int[200];
    for (int i = 0; i < 10000; ++i) {
      final Lion l = new Lion();
      // should give mean of 20 turns
      l.set(Attribute.LIFE, 59);
      l.setState(State.ASLEEP);
      int j = 0;
      while (++j < c.length - 1 && !l.update(null, null)) {
        // do nothing
      }
      c[j]++;
    }
    // compute expectation
    int e = 0;
    int d = 0;
    for (int i = 0; i < c.length; ++i) {
      e += i * c[i];
      if (c[i] != 0) {
        ++d;
      }
    }
    e /= 10000;
    // in the limit e should be 20
    assertTrue(e > 15);
    assertTrue(e < 25);
    assertTrue(d > 50);
  }

  public void testKillCount() {
    final Castable c = getCastable();
    if (c instanceof Actor) {
      final Actor a = (Actor) c;
      assertEquals(0, a.getKillCount());
      assertEquals(1, a.incrementKillCount());
      assertEquals(1, a.getKillCount());
      assertEquals(2, a.incrementKillCount());
      assertEquals(2, a.getKillCount());
    }
  }

  public void testSpecials() {
    final Web w = new Web();
    assertEquals(Actor.OWNER_NONE, w.getOwner());
    assertEquals(0, w.getDefaultWeight());
    assertEquals(0, w.getDefault(Attribute.LIFE_RECOVERY));
    assertEquals(0, w.getDefault(Attribute.MAGICAL_RESISTANCE));
    assertEquals(0, w.getDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY));
    assertFalse(w.update(null, null));
    w.set(Attribute.LIFE_RECOVERY, -1);
    w.set(Attribute.LIFE, Attribute.LIFE.min());
    assertTrue(w.update(null, null));
    Lion l = new Lion();
    assertEquals(30, l.getDefaultWeight());
    l.setState(State.ACTIVE);
    assertEquals(l.getDefault(Attribute.LIFE), l.getDefaultWeight());
    l.set(Attribute.MAGICAL_RESISTANCE_RECOVERY, -4);
    assertFalse(l.update(null, null));
    assertEquals(-4, l.get(Attribute.MAGICAL_RESISTANCE_RECOVERY));
    assertEquals(34, l.get(Attribute.MAGICAL_RESISTANCE));
    l.set(Attribute.MAGICAL_RESISTANCE_RECOVERY, 4);
    assertFalse(l.update(null, null));
    assertEquals(4, l.get(Attribute.MAGICAL_RESISTANCE_RECOVERY));
    assertEquals(l.getDefault(Attribute.MAGICAL_RESISTANCE), l.get(Attribute.MAGICAL_RESISTANCE));
    l.set(Attribute.LIFE_RECOVERY, -4);
    assertFalse(l.update(null, null));
    assertEquals(-4, l.get(Attribute.LIFE_RECOVERY));
    assertEquals(26, l.get(Attribute.LIFE));
    l.set(Attribute.LIFE_RECOVERY, 4);
    assertFalse(l.update(null, null));
    assertEquals(4, l.get(Attribute.LIFE_RECOVERY));
    assertEquals(l.getDefault(Attribute.LIFE), l.get(Attribute.LIFE));
    l = new Lion();
    l.set(Attribute.MAGICAL_RESISTANCE, Attribute.MAGICAL_RESISTANCE.min());
    l.set(Attribute.MAGICAL_RESISTANCE_RECOVERY, -4);
    assertTrue(l.update(null, null));
    assertEquals(0, l.get(Attribute.MAGICAL_RESISTANCE));
    l.set(Attribute.MAGICAL_RESISTANCE, Attribute.MAGICAL_RESISTANCE.max() - 2);
    l.set(Attribute.MAGICAL_RESISTANCE_RECOVERY, 4);
    l.update(null, null);
    assertEquals(Attribute.MAGICAL_RESISTANCE.max() - 2, l.get(Attribute.MAGICAL_RESISTANCE));
    l = new Lion();
    l.set(Attribute.LIFE, Attribute.LIFE.max() - 2);
    l.set(Attribute.LIFE_RECOVERY, 4);
    l.update(null, null);
    assertEquals(Attribute.LIFE.max() - 2, l.get(Attribute.LIFE));
  }

  public void testSleepDeathRate() {
    int sumc = 0;
    for (int i = 0; i < 100; ++i) {
      final Web w = new Web();
      w.setState(State.ASLEEP);
      int c = 0;
      while (!w.update(null, null)) {
        ++c;
      }
      sumc += c;
    }
    assertTrue(sumc > 10);
    assertTrue(sumc < 4000);
  }

  public void testSleepDeathRate2() {
    for (int i = 0; i < 10; ++i) {
      final Web w = new Web();
      w.set(Attribute.LIFE, 0);
      w.setState(State.ASLEEP);
      assertTrue(w.update(null, null));
    }
  }

  public void testIncrementDecrement() {
    final Web w = new Web();
    w.increment((Attribute) null, 5);
    assertFalse(w.decrement(null, 5));
    final int t = w.get(Attribute.LIFE);
    assertEquals(t, w.get(Attribute.LIFE));
    w.increment(Attribute.LIFE, 5);
    assertEquals(t + 5, w.get(Attribute.LIFE));
    assertEquals(t + 5, w.get(Attribute.LIFE));
    assertFalse(w.decrement(Attribute.LIFE, 5));
    assertEquals(t, w.get(Attribute.LIFE));
    assertEquals(t, w.get(Attribute.LIFE));
    w.increment(Attribute.LIFE, Attribute.LIFE.max());
    assertEquals(Attribute.LIFE.max(), w.get(Attribute.LIFE));
    w.increment(Attribute.SPECIAL_COMBAT, 1);
    w.set(Attribute.LIFE, 2);
    assertTrue(w.decrement(Attribute.LIFE, 2));
    assertEquals(0, w.get(Attribute.LIFE));

    int tr = w.get(Attribute.LIFE_RECOVERY);
    w.increment(Attribute.LIFE_RECOVERY, 5);
    assertEquals(tr + 5, w.get(Attribute.LIFE_RECOVERY));
    assertEquals(tr + 5, w.get(Attribute.LIFE_RECOVERY));
    assertFalse(w.decrement(Attribute.LIFE_RECOVERY, 5));
    assertEquals(tr, w.get(Attribute.LIFE_RECOVERY));
    assertEquals(tr, w.get(Attribute.LIFE_RECOVERY));
    assertEquals(w.getDefault(Attribute.LIFE), w.getDefault(Attribute.LIFE));
    assertEquals(w.getDefault(Attribute.LIFE_RECOVERY), w.getDefault(Attribute.LIFE_RECOVERY));
    w.set(Attribute.LIFE_RECOVERY, Attribute.LIFE_RECOVERY.min());
    assertFalse(w.decrement(Attribute.LIFE_RECOVERY, 5));
    assertEquals(Attribute.LIFE_RECOVERY.min(), w.get(Attribute.LIFE_RECOVERY));

    tr = w.get(Attribute.MAGICAL_RESISTANCE);
    w.increment(Attribute.MAGICAL_RESISTANCE, 5);
    assertEquals(tr + 5, w.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(tr + 5, w.get(Attribute.MAGICAL_RESISTANCE));
    assertFalse(w.decrement(Attribute.MAGICAL_RESISTANCE, 5));
    assertEquals(tr, w.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(tr, w.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(w.getDefault(Attribute.MAGICAL_RESISTANCE), w.getDefault(Attribute.MAGICAL_RESISTANCE));
    w.set(Attribute.MAGICAL_RESISTANCE, 1);
    assertTrue(w.decrement(Attribute.MAGICAL_RESISTANCE, 2));
    assertEquals(0, w.get(Attribute.MAGICAL_RESISTANCE));

    tr = w.get(Attribute.MAGICAL_RESISTANCE_RECOVERY);
    w.increment(Attribute.MAGICAL_RESISTANCE_RECOVERY, 5);
    assertEquals(tr + 5, w.get(Attribute.MAGICAL_RESISTANCE_RECOVERY));
    assertEquals(tr + 5, w.get(Attribute.MAGICAL_RESISTANCE_RECOVERY));
    assertFalse(w.decrement(Attribute.MAGICAL_RESISTANCE_RECOVERY, 5));
    assertEquals(tr, w.get(Attribute.MAGICAL_RESISTANCE_RECOVERY));
    assertEquals(tr, w.get(Attribute.MAGICAL_RESISTANCE_RECOVERY));
    assertEquals(w.getDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY), w.getDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY));
    w.set(Attribute.MAGICAL_RESISTANCE_RECOVERY, Attribute.MAGICAL_RESISTANCE_RECOVERY.min());
    assertFalse(w.decrement(Attribute.MAGICAL_RESISTANCE_RECOVERY, 5));
    assertEquals(Attribute.MAGICAL_RESISTANCE_RECOVERY.min(), w.get(Attribute.MAGICAL_RESISTANCE_RECOVERY));
  }

  public void testPowerUpManipulation() {
    final Wizard w = new Wizard1();
    w.setCastableList(new CastableList(100, 100, 24));
    w.set(PowerUps.NECROPOTENCE, 1);
    w.increment(PowerUps.NECROPOTENCE);
    assertEquals(2, w.get(PowerUps.NECROPOTENCE));
    w.decrement(PowerUps.NECROPOTENCE);
    assertEquals(1, w.get(PowerUps.NECROPOTENCE));
    w.decrement(PowerUps.NECROPOTENCE);
    assertEquals(0, w.get(PowerUps.NECROPOTENCE));
    w.decrement(PowerUps.NECROPOTENCE);
    assertEquals(0, w.get(PowerUps.NECROPOTENCE));
  }
}
