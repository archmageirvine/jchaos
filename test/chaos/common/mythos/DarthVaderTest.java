package chaos.common.mythos;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class DarthVaderTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new DarthVader();
  }
}
