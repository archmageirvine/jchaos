package chaos.common.mythos;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class MrStrongTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new MrStrong();
  }

  @Override
  public void testReincarnation() {
    assertEquals(null, new MrStrong().reincarnation());
  }
}
