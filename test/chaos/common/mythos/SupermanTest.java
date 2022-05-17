package chaos.common.mythos;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;

/**
 * Tests this monster.
 *
 * @author Sean A. Irvine
 */
public class SupermanTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Superman();
  }

  @Override
  public void testReincarnation() {
    assertEquals(null, new Superman().reincarnation());
  }
}
