package chaos.common.wizard;

import chaos.common.AbstractMonsterTest;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Humanoid;
import chaos.util.NameUtils;
import chaos.util.RankingTable;
import junit.framework.Assert;

/**
 * Tests this wizard.
 *
 * @author Sean A. Irvine
 */
public abstract class AbstractWizardTest extends AbstractMonsterTest {


  @Override
  public void testReincarnation() {
    assertEquals(null, ((Wizard) getCastable()).reincarnation());
  }

  public void testScoring() {
    final Wizard w = (Wizard) getCastable();
    assertEquals(0, w.getScore());
    w.addScore(1);
    assertEquals(1, w.getScore());
    try {
      w.addScore(-1);
      fail("Allowed addition of negative points");
    } catch (final IllegalArgumentException e) {
      // ok
    }
    assertEquals(1, w.getScore());
    w.addScore(42);
    assertEquals(43, w.getScore());
    w.addScore(42);
    assertEquals(85, w.getScore());
  }

  public void testAddBonus() {
    final Wizard w = (Wizard) getCastable();
    try {
      w.addBonus(-1, -1);
      fail("Bad bonus");
    } catch (final IllegalArgumentException e) {
      // ok
    }
    try {
      w.addBonus(1, -1);
      fail("Bad bonus");
    } catch (final IllegalArgumentException e) {
      // ok
    }
    try {
      w.addBonus(2, 1);
      fail("Bad bonus");
    } catch (final IllegalArgumentException e) {
      // ok
    }
    w.addBonus(1, 2);
  }

  /**
   * Test weight range.
   */
  @Override
  public void testDefaultWeight() {
    Assert.assertEquals(60, ((Actor) mCastable).getDefaultWeight());
  }

  public void testHumanoid() {
    assertTrue(mCastable instanceof Humanoid);
  }

  @Override
  public void testGetRanking() {
    assertEquals("Bad ranking found", -1, RankingTable.getRanking(mCastable));
  }

  /** Test naming. */
  public void testNaming() {
    final Wizard w = (Wizard) getCastable();
    final String n = w.getPersonalName();
    assertTrue(n != null);
    assertTrue(!n.isEmpty());
    assertTrue(n.length() <= NameUtils.MAX_NAME_LENGTH);
    try {
      w.setPersonalName(null);
      fail("null accepted");
    } catch (final RuntimeException e) {
      // ok
    }
    try {
      w.setPersonalName("This name is way too long to be accepted as a wizard name");
      fail("null accepted");
    } catch (final RuntimeException e) {
      // ok
    }
    assertEquals(n, w.getPersonalName());
    w.setPersonalName("test");
    assertEquals("test", w.getPersonalName());
  }

  public void testWizStats() {
    final Wizard w = (Wizard) getCastable();
    assertEquals(19, w.get(Attribute.LIFE));
    assertEquals(53, w.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(100, w.get(Attribute.INTELLIGENCE));
    assertEquals(1, w.get(Attribute.COMBAT));
    assertEquals(1, w.get(Attribute.MOVEMENT));
    assertEquals(7, w.get(Attribute.AGILITY));
    assertEquals(6, w.getBonus());
    assertEquals(0, w.get(Attribute.LIFE_RECOVERY));
  }
}
