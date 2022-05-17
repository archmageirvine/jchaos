package chaos.common.mythos;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class Alien8Test extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Alien8();
  }

  @Override
  public void testReincarnation() {
    assertEquals(null, new Alien8().reincarnation());
  }
}
