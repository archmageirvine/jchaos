package chaos.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;

import chaos.common.monster.Hippocrates;
import chaos.common.monster.Orc;
import chaos.sound.SoundSelection;
import junit.framework.Assert;

/**
 * Tests basic functionality that all Monsters should satisfy.
 *
 * @author Sean A. Irvine
 */
public abstract class AbstractMonsterTest extends AbstractActorTest {

  @Override
  public void testInstanceOf() {
    assertTrue(mCastable instanceof Monster);
  }

  public void testMovement() {
    final Monster a = (Monster) mCastable;
    assertTrue(a.get(Attribute.MOVEMENT) >= 0);
    a.set(Attribute.MOVEMENT, 7);
    assertEquals(a instanceof Inanimate ? 0 : 7, a.get(Attribute.MOVEMENT));
    a.set(Attribute.MOVEMENT, 5);
    assertEquals(a instanceof Inanimate ? 0 : 5, a.get(Attribute.MOVEMENT));
  }

  public void testMovementRecovery() {
    final Monster a = (Monster) mCastable;
    a.set(Attribute.MOVEMENT_RECOVERY, 7);
    assertEquals(7, a.get(Attribute.MOVEMENT_RECOVERY));
    a.set(Attribute.MOVEMENT_RECOVERY, 5);
    assertEquals(5, a.get(Attribute.MOVEMENT_RECOVERY));
  }

  public void testAgility() {
    final Monster a = (Monster) mCastable;
    assertTrue(a.get(Attribute.AGILITY) >= 0);
    a.set(Attribute.AGILITY, 7);
    assertEquals(7, a.get(Attribute.AGILITY));
    a.set(Attribute.AGILITY, 5);
    assertEquals(5, a.get(Attribute.AGILITY));
  }

  public void testAgilityRecovery() {
    final Monster a = (Monster) mCastable;
    a.set(Attribute.AGILITY_RECOVERY, 7);
    assertEquals(7, a.get(Attribute.AGILITY_RECOVERY));
    a.set(Attribute.AGILITY_RECOVERY, 5);
    assertEquals(5, a.get(Attribute.AGILITY_RECOVERY));
  }

  public void testIntelligence() {
    final Monster a = (Monster) mCastable;
    assertTrue(a.get(Attribute.INTELLIGENCE) >= 0);
    a.set(Attribute.INTELLIGENCE, 7);
    assertEquals(7, a.get(Attribute.INTELLIGENCE));
    a.set(Attribute.INTELLIGENCE, 5);
    assertEquals(5, a.get(Attribute.INTELLIGENCE));
  }

  public void testIntelligenceRecovery() {
    final Monster a = (Monster) mCastable;
    a.set(Attribute.INTELLIGENCE_RECOVERY, 7);
    assertEquals(7, a.get(Attribute.INTELLIGENCE_RECOVERY));
    a.set(Attribute.INTELLIGENCE_RECOVERY, 5);
    assertEquals(5, a.get(Attribute.INTELLIGENCE_RECOVERY));
  }

  public void testRange() {
    final Monster a = (Monster) mCastable;
    assertTrue(a.get(Attribute.RANGE) >= 0);
    a.set(Attribute.RANGE, 7);
    assertEquals(7, a.get(Attribute.RANGE));
    a.set(Attribute.RANGE, 5);
    assertEquals(5, a.get(Attribute.RANGE));
  }

  public void testRangeRecovery() {
    final Monster a = (Monster) mCastable;
    a.set(Attribute.RANGE_RECOVERY, 7);
    assertEquals(7, a.get(Attribute.RANGE_RECOVERY));
    a.set(Attribute.RANGE_RECOVERY, 5);
    assertEquals(5, a.get(Attribute.RANGE_RECOVERY));
  }

  public void testRangedCombat() {
    final Monster a = (Monster) mCastable;
    a.set(Attribute.RANGED_COMBAT, 7);
    assertEquals(7, a.get(Attribute.RANGED_COMBAT));
    a.set(Attribute.RANGED_COMBAT, 5);
    assertEquals(5, a.get(Attribute.RANGED_COMBAT));
  }

  public void testRangedCombatRecovery() {
    final Monster a = (Monster) mCastable;
    a.set(Attribute.RANGED_COMBAT_RECOVERY, 7);
    assertEquals(7, a.get(Attribute.RANGED_COMBAT_RECOVERY));
    a.set(Attribute.RANGED_COMBAT_RECOVERY, 5);
    assertEquals(5, a.get(Attribute.RANGED_COMBAT_RECOVERY));
  }

  public void testSpecialCombat() {
    final Monster a = (Monster) mCastable;
    a.set(Attribute.SPECIAL_COMBAT, 7);
    assertEquals(7, a.get(Attribute.SPECIAL_COMBAT));
    a.set(Attribute.SPECIAL_COMBAT, 5);
    assertEquals(5, a.get(Attribute.SPECIAL_COMBAT));
  }

  public void testSpecialCombatRecovery() {
    final Monster a = (Monster) mCastable;
    a.set(Attribute.SPECIAL_COMBAT_RECOVERY, 7);
    assertEquals(7, a.get(Attribute.SPECIAL_COMBAT_RECOVERY));
    a.set(Attribute.SPECIAL_COMBAT_RECOVERY, 5);
    assertEquals(5, a.get(Attribute.SPECIAL_COMBAT_RECOVERY));
  }

  public void testCombat() {
    final Monster a = (Monster) mCastable;
    a.set(Attribute.COMBAT, 7);
    assertEquals(7, a.get(Attribute.COMBAT));
    a.set(Attribute.COMBAT, 5);
    assertEquals(5, a.get(Attribute.COMBAT));
  }

  public void testCombatRecovery() {
    final Monster a = (Monster) mCastable;
    a.set(Attribute.COMBAT_RECOVERY, 7);
    assertEquals(7, a.get(Attribute.COMBAT_RECOVERY));
    a.set(Attribute.COMBAT_RECOVERY, 5);
    assertEquals(5, a.get(Attribute.COMBAT_RECOVERY));
  }

  /**
   * If one of Range or RangedCombat is nonzero the other
   * should be non-zero as well.
   */
  public void testRangeAndRangedCombat() {
    final Monster a = (Monster) mCastable;
    if (a.get(Attribute.RANGED_COMBAT) != 0) {
      assertTrue("ranged combat was >0 but range was not", a.get(Attribute.RANGE) > 0);
    } else {
      assertEquals("ranged combat was 0 but range was not", 0, a.get(Attribute.RANGE));
    }
  }

  /**
   * Test the CombatApply field works.
   */
  public void testCA() {
    final Monster a = (Monster) mCastable;
    assertNotNull("CombatApply not set", a.getCombatApply());
    a.setCombatApply(Attribute.MOVEMENT);
    assertEquals(Attribute.MOVEMENT, a.getCombatApply());
  }

  /**
   * Test the RangedCombatApply field works.
   */
  public void testRCA() {
    final Monster a = (Monster) mCastable;
    assertNotNull("RangedCombatApply not set", a.getRangedCombatApply());
    a.setRangedCombatApply(Attribute.MOVEMENT);
    assertEquals(Attribute.MOVEMENT, a.getRangedCombatApply());
  }

  /**
   * Test the SpecialCombatApply field works.
   */
  public void testSCA() {
    final Monster a = (Monster) mCastable;
    assertNotNull("SpecialCombatApply not set", a.getSpecialCombatApply());
    a.setSpecialCombatApply(Attribute.MOVEMENT);
    assertEquals(Attribute.MOVEMENT, a.getSpecialCombatApply());
  }

  public void testIsShot() {
    // should be false be default at start
    final Monster a = (Monster) mCastable;
    assertEquals("isShot was set unexpectedly", 0, a.getShotsMade());
    a.setShotsMade(1);
    assertEquals("isShot was clear unexpectedly", 1, a.getShotsMade());
  }

  /**
   * Test defaults were correctly applied.
   */
  @Override
  public void testDefaults() {
    super.testDefaults();
    final Monster a = (Monster) mCastable;
    assertEquals("agility error", a.getDefault(Attribute.AGILITY), a.get(Attribute.AGILITY));
    assertEquals("agility recovery error", a.getDefault(Attribute.AGILITY_RECOVERY), a.get(Attribute.AGILITY_RECOVERY));
    assertEquals("RC error", a.getDefault(Attribute.RANGED_COMBAT), a.get(Attribute.RANGED_COMBAT));
    assertEquals("RC recovery error", a.getDefault(Attribute.RANGED_COMBAT_RECOVERY), a.get(Attribute.RANGED_COMBAT_RECOVERY));
    assertEquals("combat error", a.getDefault(Attribute.COMBAT), a.get(Attribute.COMBAT));
    assertEquals("combat recovery error", a.getDefault(Attribute.COMBAT_RECOVERY), a.get(Attribute.COMBAT_RECOVERY));
    assertEquals("movement error", a.getDefault(Attribute.MOVEMENT), a.get(Attribute.MOVEMENT));
    assertEquals("movement recovery error", a.getDefault(Attribute.MOVEMENT_RECOVERY), a.get(Attribute.MOVEMENT_RECOVERY));
    assertEquals("range error", a.getDefault(Attribute.RANGE), a.get(Attribute.RANGE));
    assertEquals("range recovery error", a.getDefault(Attribute.RANGE_RECOVERY), a.get(Attribute.RANGE_RECOVERY));
    assertEquals("intelligence error", a.getDefault(Attribute.INTELLIGENCE), a.get(Attribute.INTELLIGENCE));
    assertEquals("intelligence recovery error", a.getDefault(Attribute.INTELLIGENCE_RECOVERY), a.get(Attribute.INTELLIGENCE_RECOVERY));
    assertEquals("SC error", a.getDefault(Attribute.SPECIAL_COMBAT), a.get(Attribute.SPECIAL_COMBAT));
    assertEquals("SC recovery error", a.getDefault(Attribute.SPECIAL_COMBAT_RECOVERY), a.get(Attribute.SPECIAL_COMBAT_RECOVERY));
  }

  /**
   * Test flying state can be toggled.
   */
  public void testFlyingBit() {
    final Monster a = (Monster) mCastable;
    a.set(PowerUps.FLYING, 1);
    assertTrue(a.is(PowerUps.FLYING));
    a.set(PowerUps.FLYING, 0);
    assertFalse(a.is(PowerUps.FLYING));
    a.set(PowerUps.FLYING, 1);
    assertTrue(a.is(PowerUps.FLYING));
  }

  /**
   * Test archer state can be toggled.
   */
  public void testArcherBit() {
    final Monster a = (Monster) mCastable;
    a.set(PowerUps.ARCHERY, 1);
    assertTrue(a.is(PowerUps.ARCHERY));
    a.set(PowerUps.ARCHERY, 0);
    assertFalse(a.is(PowerUps.ARCHERY));
    a.set(PowerUps.ARCHERY, 1);
    assertTrue(a.is(PowerUps.ARCHERY));
  }

  /**
   * Make sure monster can be cast on at least empty space.
   */
  public void testCastFlags() {
    assertTrue("monster should cast on empty", (mCastable.getCastFlags() & Castable.CAST_EMPTY) != 0);
  }

  /**
   * Make sure does not reincarnate from outset.
   */
  public void testReincarnationState() {
    final Monster a = (Monster) mCastable;
    assertFalse(a.is(PowerUps.REINCARNATE));
    a.set(PowerUps.REINCARNATE, a.reincarnation() != null ? 1 : 0);
    if (a.reincarnation() != null) {
      assertTrue(a.is(PowerUps.REINCARNATE));
    }
    a.set(PowerUps.REINCARNATE, 0);
    assertFalse(a.is(PowerUps.REINCARNATE));
  }

  /**
   * Test reincarnation exists, and sequence is finite
   */
  public void testReincarnation() {
    Monster a = (Monster) mCastable;
    Class<? extends Monster> re;
    int i = 0;
    do {
      re = a.reincarnation();
      if (i++ == 0) {
        assertTrue("no reincarnation", re != null);
      }
      if (re != null) {
        try {
          a = re.getDeclaredConstructor().newInstance();
        } catch (final Exception e) {
          e.printStackTrace();
          fail(e.getMessage());
        }
      }
    } while (re != null && i < 20);
    assertTrue("loop in reincarnation", re == null);
  }

  /**
   * Make sure does not horror from outset.
   */
  public void testHorrorState() {
    final Monster a = (Monster) mCastable;
    assertEquals(0, a.get(PowerUps.HORROR));
    a.set(PowerUps.HORROR, 2);
    assertEquals(2, a.get(PowerUps.HORROR));
    a.decrement(PowerUps.HORROR);
    assertEquals(1, a.get(PowerUps.HORROR));
    a.set(PowerUps.HORROR, 0);
    assertEquals(0, a.get(PowerUps.HORROR));
  }

  /**
   * Check only dragons are in the draconic.
   */
  public void testDraconic() {
    if (mCastable.getName().contains("Dragon")) {
      assertEquals(Realm.DRACONIC, ((Actor) mCastable).getRealm());
      assertTrue(((Monster) mCastable).is(PowerUps.FLYING));
    } else {
      assertTrue(((Actor) mCastable).getRealm() != Realm.DRACONIC);
    }
  }

  /**
   * Check update clears movement flag. And stats don't go haywire
   */
  @Override
  public void testUpdate() {
    final Monster a = (Monster) mCastable;
    a.setShotsMade(1);
    super.testUpdate();
    assertEquals(0, a.getShotsMade());
    assertEquals(a.getDefault(Attribute.COMBAT), a.get(Attribute.COMBAT));
    assertEquals(a.getDefault(Attribute.COMBAT_RECOVERY), a.get(Attribute.COMBAT_RECOVERY));
    assertEquals(a.getDefault(Attribute.INTELLIGENCE), a.get(Attribute.INTELLIGENCE));
    assertEquals(a.getDefault(Attribute.INTELLIGENCE_RECOVERY), a.get(Attribute.INTELLIGENCE_RECOVERY));
    assertEquals(a.getDefault(Attribute.AGILITY), a.get(Attribute.AGILITY));
    assertEquals(a.getDefault(Attribute.AGILITY_RECOVERY), a.get(Attribute.AGILITY_RECOVERY));
    assertEquals(a.getDefault(Attribute.MOVEMENT), a.get(Attribute.MOVEMENT));
    assertEquals(a.getDefault(Attribute.MOVEMENT_RECOVERY), a.get(Attribute.MOVEMENT_RECOVERY));
    assertEquals(a.getDefault(Attribute.RANGED_COMBAT), a.get(Attribute.RANGED_COMBAT));
    assertEquals(a.getDefault(Attribute.RANGED_COMBAT_RECOVERY), a.get(Attribute.RANGED_COMBAT_RECOVERY));
    assertEquals(a.getDefault(Attribute.SPECIAL_COMBAT), a.get(Attribute.SPECIAL_COMBAT));
    assertEquals(a.getDefault(Attribute.SPECIAL_COMBAT_RECOVERY), a.get(Attribute.SPECIAL_COMBAT_RECOVERY));
    assertEquals(a.getDefault(Attribute.RANGE), a.get(Attribute.RANGE));
    assertEquals(a.getDefault(Attribute.RANGE_RECOVERY), a.get(Attribute.RANGE_RECOVERY));
    a.set(Attribute.COMBAT, 0);
    a.set(Attribute.INTELLIGENCE, 0);
    a.set(Attribute.AGILITY, 0);
    a.set(Attribute.MOVEMENT, 0);
    a.set(Attribute.RANGED_COMBAT, 0);
    a.set(Attribute.RANGE, 0);
    a.set(Attribute.SPECIAL_COMBAT, 0);
    assertEquals(a.getDefault(Attribute.LIFE_RECOVERY) < 0, a.update(null, null));
    if (!(a instanceof Hippocrates)) {
      assertEquals("Combat", Math.min(a.get(Attribute.COMBAT_RECOVERY), a.getDefault(Attribute.COMBAT)), a.get(Attribute.COMBAT));
    }
    assertEquals("Agility", Math.min(a.get(Attribute.AGILITY_RECOVERY), a.getDefault(Attribute.AGILITY)), a.get(Attribute.AGILITY));
    assertEquals("Intel", Math.min(a.get(Attribute.INTELLIGENCE_RECOVERY), a.getDefault(Attribute.INTELLIGENCE)), a.get(Attribute.INTELLIGENCE));
    assertEquals("Move", Math.min(a.get(Attribute.MOVEMENT_RECOVERY), a.getDefault(Attribute.MOVEMENT)), a.get(Attribute.MOVEMENT));
    assertEquals("RC", a.get(Attribute.RANGED_COMBAT_RECOVERY), a.get(Attribute.RANGED_COMBAT));
    assertEquals("Range", Math.min(a.get(Attribute.RANGE_RECOVERY), a.getDefault(Attribute.RANGE)), a.get(Attribute.RANGE));
    assertEquals("SC", a.get(Attribute.SPECIAL_COMBAT_RECOVERY), a.get(Attribute.SPECIAL_COMBAT));
  }

  private static int getStat(final String s) {
    final int r = s.indexOf("return ");
    final int e = s.indexOf(';', r);
    final String ss = s.substring(r + 7, e);
    if ("MAX_CAST_RANGE".equals(ss)) {
      return Castable.MAX_CAST_RANGE;
    }
    return Integer.parseInt(ss);
  }

  private static int getStat(final BufferedReader r) throws IOException {
    final String s = r.readLine();
    if (s != null) {
      return getStat(s);
    }
    throw new IOException();
  }

  private static int getStatInLine(final String s) {
    final int r = s.indexOf("= ");
    final int e = s.indexOf(';', r);
    return Integer.parseInt(s.substring(r + 2, e));
  }

  private static long getMask(final String s) {
    int r = s.indexOf("return 0x");
    boolean comp = false;
    if (r == -1) {
      r = s.indexOf("return ~");
      comp = true;
    }
    int e = s.indexOf("L;", r);
    if (e == -1) {
      e = s.indexOf(';', r);
    }
    // Contortion is need since longs are signed and parse long barfs
    // when the sign bit is used in an unsigned manner
    final BigInteger b = new BigInteger(s.substring(comp ? r + 8 : r + 9, e), 16);
    assertTrue(b.bitLength() < 65);
    return comp ? ~b.longValue() : b.longValue();
  }

  /**
   * This nasty test, verifies various numerical constants in the compiled
   * class against the source.  It helps ensure the source correctly
   * defines these values in an idiomatic manner, but the main purpose is
   * to cope with Jumble testing of these classes.  If the source is not
   * available then this test does nothing.
   */
  public static void checkAgainstSource(final Actor actor) {
    final Monster m = actor instanceof Monster ? (Monster) actor : null;
    final String cname = actor.getClass().getName().replace('.', '/') + ".java";
    try {
      final InputStream source = AbstractMonsterTest.class.getClassLoader().getResourceAsStream(cname);
      if (source == null) {
        // Don't do this test if source not avilable
        return;
      }
      try (BufferedReader r = new BufferedReader(new InputStreamReader(source))) {
        String line;
        while ((line = r.readLine()) != null) {
          if (line.contains("public int getDefault(Attribute.LIFE) {")) {
            Assert.assertEquals(actor.get(Attribute.LIFE), getStat(r));
          } else if (line.contains("public int getDefault(Attribute.LIFE_RECOVERY) {")) {
            Assert.assertEquals(actor.get(Attribute.LIFE_RECOVERY), getStat(r));
          } else if (line.contains("public int getDefault(Attribute.MAGICAL_RESISTANCE) {")) {
            Assert.assertEquals(actor.get(Attribute.MAGICAL_RESISTANCE), getStat(r));
          } else if (line.contains("public int getDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY) {")) {
            Assert.assertEquals(actor.get(Attribute.MAGICAL_RESISTANCE_RECOVERY), getStat(r));
          } else if (line.contains("public int getDefault(Attribute.INTELLIGENCE) {")) {
            Assert.assertEquals(m.get(Attribute.INTELLIGENCE), getStat(r));
          } else if (line.contains("public int getDefault(Attribute.INTELLIGENCE_RECOVERY) {")) {
            Assert.assertEquals(m.get(Attribute.INTELLIGENCE_RECOVERY), getStat(r));
          } else if (line.contains("public int getDefault(Attribute.AGILITY) {")) {
            Assert.assertEquals(m.get(Attribute.AGILITY), getStat(r));
          } else if (line.contains("public int getDefault(Attribute.AGILITY_RECOVERY) {")) {
            Assert.assertEquals(m.get(Attribute.AGILITY_RECOVERY), getStat(r));
          } else if (line.contains("public int getDefault(Attribute.COMBAT) {")) {
            Assert.assertEquals(m.get(Attribute.COMBAT), getStat(r));
          } else if (line.contains("public int getDefault(Attribute.COMBAT_RECOVERY) {")) {
            Assert.assertEquals(m.get(Attribute.COMBAT_RECOVERY), getStat(r));
          } else if (line.contains("public int getDefault(Attribute.RANGED_COMBAT) {")) {
            Assert.assertEquals(m.get(Attribute.RANGED_COMBAT), getStat(r));
          } else if (line.contains("public int getDefault(Attribute.RANGED_COMBAT_RECOVERY) {")) {
            Assert.assertEquals(m.get(Attribute.RANGED_COMBAT_RECOVERY), getStat(r));
          } else if (line.contains("public int getDefault(Attribute.SPECIAL_COMBAT) {")) {
            Assert.assertEquals(m.get(Attribute.SPECIAL_COMBAT), getStat(r));
          } else if (line.contains("public int getDefault(Attribute.MOVEMENT) {")) {
            Assert.assertEquals(m.get(Attribute.MOVEMENT), getStat(r));
          } else if (line.contains("public int getDefault(Attribute.RANGE) {")) {
            Assert.assertEquals(m.get(Attribute.RANGE), getStat(r));
          } else if (line.contains("public int getCastRange() {")) {
            Assert.assertEquals(actor.getCastRange(), getStat(r));
          } else if (line.contains("public int getDefaultWeight() {")) {
            Assert.assertEquals(actor.getDefaultWeight(), getStat(r));
          } else if (line.contains("public int promotionCount() {")) {
            Assert.assertTrue(actor instanceof Promotion);
            Assert.assertEquals(((Promotion) actor).promotionCount(), getStat(r));
          } else if (line.contains("public int growthRate() {")) {
            Assert.assertTrue(actor instanceof Growth);
            Assert.assertEquals(((Growth) actor).growthRate(), getStat(r));
          } else if (line.contains("public int getBonus() {")) {
            Assert.assertTrue(actor instanceof Bonus);
            Assert.assertEquals(((Bonus) actor).getBonus(), getStat(r));
          } else if (!(actor instanceof Orc) && line.contains("public int getMultiplicity() {")) {
            Assert.assertTrue(actor instanceof Multiplicity);
            Assert.assertEquals(((Multiplicity) actor).getMultiplicity(), getStat(r));
          } else if (line.contains("mDelay")) {
            Assert.assertTrue(actor instanceof Caster);
            if (actor instanceof Polycaster) {
              Assert.assertEquals(((Polycaster) actor).mDelay, getStatInLine(line));
            } else if (actor instanceof Unicaster) {
              Assert.assertEquals(((Unicaster) actor).mDelay, getStatInLine(line));
            }
          } else if (line.contains("public long getLOSMask() {")) {
            final String ml = r.readLine();
            if (ml != null) {
              Assert.assertEquals(actor.getLosMask(), getMask(ml));
            } else {
              fail();
            }
          }
        }
      } catch (final IOException ex) {
        Assert.fail(ex.getMessage());
      } finally {
        source.close();
      }
    } catch (final IOException e) {
      // too bad
    }
  }

  public void testAgainstSource() {
    checkAgainstSource((Actor) mCastable);
  }

  public void testPromotionMount() {
    if (mCastable instanceof Promotion) {
      final Class<? extends Actor> promo = ((Promotion) mCastable).promotion();
      if (mCastable instanceof Conveyance) {
        assertTrue(FrequencyTable.instantiate(promo) instanceof Conveyance);
      }
    }
  }

  public void testSpecialCombatSound() {
    final Monster m = (Monster) mCastable;
    if (m.getDefault(Attribute.SPECIAL_COMBAT) != 0) {
      final String s = SoundSelection.getSpecialCombatSound(m);
      assertNotNull(s);
      assertFalse(s.contains("chaos/resources/sound/special/shocker"));
    }
  }
}
