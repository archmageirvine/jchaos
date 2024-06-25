package chaos.common.mythos;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class OrangeTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Orange();
  }

  @Override
  public void testReincarnation() {
    assertEquals(null, new Orange().reincarnation());
  }
}
