package chaos.common.monster;

import chaos.common.AbstractMonsterTest;
import chaos.common.Attribute;
import chaos.common.Castable;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class ArcticWolfTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new ArcticWolf();
  }

  public void testapply() {
    assertEquals(Attribute.MOVEMENT, new ArcticWolf().getCombatApply());
  }
}
