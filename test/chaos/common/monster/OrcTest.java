package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class OrcTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new Orc();
  }

  @Override
  public void testMultiplicity() {
    final int m = new Orc().getMultiplicity();
    assertTrue("multiplicity wasn't >1", m > 0);
    assertTrue("multiplicity wasn't <9", m < 9);
  }

}
