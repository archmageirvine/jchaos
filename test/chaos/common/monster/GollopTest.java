package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Attribute;
import chaos.common.Castable;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class GollopTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Gollop();
  }

  @Override
  public void testReincarnation() {
    assertEquals(null, new Gollop().reincarnation());
  }

  public void testShouldMerge() {
    final Gollop gollop = new Gollop();
    assertFalse(gollop.shouldMerge(new Gollop()));
    assertTrue(gollop.shouldMerge(new FireDemon()));
    assertTrue(gollop.shouldMerge(new Orc()));
    gollop.set(Attribute.LIFE, 100);
    assertFalse(gollop.shouldMerge(new FireDemon()));
    assertTrue(gollop.shouldMerge(new Orc()));
  }
}
