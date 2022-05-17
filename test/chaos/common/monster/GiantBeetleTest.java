package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;

/**
 * Tests this monster.
 *
 * @author Sean A. Irvine
 */
public class GiantBeetleTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new GiantBeetle();
  }

  @Override
  public void testReincarnation() {
    assertEquals(null, new GiantBeetle().reincarnation());
  }

}
