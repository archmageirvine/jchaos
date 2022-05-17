package chaos.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

import chaos.common.growth.Fire;
import chaos.common.monster.Dalek;
import chaos.common.monster.Lion;
import chaos.common.monster.MightyOrc;
import chaos.common.monster.Orc;
import chaos.common.monster.Wolverine;
import chaos.common.wizard.Wizard;
import chaos.common.wizard.Wizard1;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class FrequencyTableTest extends TestCase {

  public void testEntriesAndFrequencyLogic() {
    assertEquals(0, FrequencyTable.DEFAULT.getFrequency(Monster.class));
    assertEquals(0, FrequencyTable.DEFAULT.getFrequency(Wizard1.class));
    assertEquals(50, FrequencyTable.DEFAULT.getFrequency(Lion.class));
    assertEquals(13, FrequencyTable.DEFAULT.getFrequency(Fire.class));
    assertEquals(8, FrequencyTable.DEFAULT.getFrequency(Dalek.class));
    assertEquals(-52, FrequencyTable.DEFAULT.getFrequency(Wolverine.class));
    assertEquals(0.0F, FrequencyTable.DEFAULT.getProbability(Wolverine.class), 1.0E-6);
    assertTrue(FrequencyTable.DEFAULT.getBonusProbability(Wolverine.class) > 0.0F);
    assertTrue(FrequencyTable.DEFAULT.getProbability(Lion.class) > 0.0F);
    assertTrue(FrequencyTable.DEFAULT.getProbability(Fire.class) > 0.0F);
    assertTrue(FrequencyTable.DEFAULT.getProbability(Lion.class) < 1.0F);
    assertTrue(FrequencyTable.DEFAULT.getProbability(Fire.class) < 1.0F);
    assertTrue(FrequencyTable.DEFAULT.getProbability(Lion.class) > FrequencyTable.DEFAULT.getProbability(Fire.class));
    assertTrue(FrequencyTable.DEFAULT.getProbability(Lion.class) > FrequencyTable.DEFAULT.getBonusProbability(Lion.class));
    assertTrue(FrequencyTable.DEFAULT.getProbability(Fire.class) > FrequencyTable.DEFAULT.getBonusProbability(Fire.class));
  }

  public void testNSpells() {
    assertTrue(FrequencyTable.DEFAULT.getNumberOfSpells() > 10);
    assertTrue(FrequencyTable.DEFAULT.getNumberOfSpells() < 1000);
  }

  private static final int LOOP_LIMIT = 500000;

  public void testGetRandom() {
    final Map<Class<? extends Castable>, Integer> s = new HashMap<>();
    for (int i = 0; i < LOOP_LIMIT; ++i) {
      final Class<? extends Castable> c = FrequencyTable.DEFAULT.getRandom();
      assertNotNull(c);
      assertTrue(FrequencyTable.DEFAULT.getFrequency(c) > 0);
      s.merge(c, 1, (x, y) -> x + y);
    }
    for (final Map.Entry<Class<? extends Castable>, Integer> e : s.entrySet()) {
      final float p = FrequencyTable.DEFAULT.getProbability(e.getKey());
      assertTrue(p > 0.0F);
      assertTrue(p < 1.0F);
      final double ratio = e.getValue().floatValue() / (LOOP_LIMIT * p);
      assertTrue(ratio < 2.0);
      assertTrue(ratio > 0.5);
    }
  }

  public void testGetBonusRandom() {
    final Map<Class<? extends Castable>, Integer> s = new HashMap<>();
    for (int i = 0; i < LOOP_LIMIT; ++i) {
      final Class<? extends Castable> c = FrequencyTable.DEFAULT.getBonusRandom();
      assertNotNull(c);
      s.merge(c, 1, (x, y) -> x + y);
    }
    for (final Map.Entry<Class<? extends Castable>, Integer> e : s.entrySet()) {
      final float p = FrequencyTable.DEFAULT.getBonusProbability(e.getKey());
      assertTrue(p > 0.0F);
      assertTrue(p < 1.0F);
      final double ratio = e.getValue().floatValue() / (LOOP_LIMIT * p);
      assertTrue(ratio + " for " + e.getKey(), ratio < 4.0);
      assertTrue(ratio + " for " + e.getKey(), ratio > 0.25);
    }
  }

  public void testGetUniformRandom() {
    final HashSet<Class<? extends Castable>> s = new HashSet<>();
    for (int i = 0; i < LOOP_LIMIT; ++i) {
      final Class<? extends Castable> c = FrequencyTable.DEFAULT.getUniformRandom();
      assertNotNull(c);
      s.add(c);
    }
    assertEquals(FrequencyTable.DEFAULT.getNumberOfSpells(), s.size());
  }

  public void testGetUniformRandomActor() {
    final HashSet<Actor> s = new HashSet<>();
    for (int i = 0; i < 1000; ++i) {
      final Actor c = FrequencyTable.DEFAULT.getUniformRandomActor();
      assertNotNull(c);
      assertFalse(c instanceof Wizard);
      s.add(c);
    }
    assertTrue(s.size() > 20);
  }

  public void testGetUniformRandomMonster() {
    final HashSet<Monster> s = new HashSet<>();
    for (int i = 0; i < 1000; ++i) {
      final Monster c = FrequencyTable.DEFAULT.getUniformRandomMonster();
      assertNotNull(c);
      assertFalse(c instanceof Wizard);
      assertFalse(c instanceof Inanimate);
      s.add(c);
    }
    assertTrue(s.size() > 20);
  }

  public void testGetRandomMonster() {
    final HashSet<Monster> s = new HashSet<>();
    for (int i = 0; i < 1000; ++i) {
      final Monster c = FrequencyTable.DEFAULT.getRandomMonster();
      assertNotNull(c);
      assertFalse(c instanceof Wizard);
      assertFalse(c instanceof Inanimate);
      s.add(c);
    }
    assertTrue(s.size() > 20);
  }

  public void testInstantiate() {
    assertTrue(FrequencyTable.instantiate(Orc.class) instanceof Orc);
  }

  public void testGetBonusChoice() {
    final Random r = new Random();
    for (int i = 0; i < 30; ++i) {
      final Castable[] c = FrequencyTable.DEFAULT.getBonusChoice(i, r.nextInt(10));
      assertEquals(i, c.length);
      for (final Castable j : c) {
        assertNotNull(j);
      }
    }
    try {
      FrequencyTable.DEFAULT.getBonusChoice(-1, 1);
      fail();
    } catch (final NegativeArraySizeException e) {
      // ok
    }
    assertNull(FrequencyTable.DEFAULT.getBonusChoice(1, -1)[0]);
  }

  public void testGetByPartialName() {
    assertEquals(Lion.class, FrequencyTable.DEFAULT.getByPartialName("Lion").getClass());
    assertEquals(Lion.class, FrequencyTable.DEFAULT.getByPartialName("r.Lion").getClass());
    assertEquals(Lion.class, FrequencyTable.DEFAULT.getByPartialName("monster.Lion").getClass());
    assertNull(FrequencyTable.DEFAULT.getByPartialName("no-such-class"));
    assertEquals(Orc.class, FrequencyTable.DEFAULT.getByPartialName("Orc").getClass());
    assertEquals(MightyOrc.class, FrequencyTable.DEFAULT.getByPartialName("yOrc").getClass());
  }
}
