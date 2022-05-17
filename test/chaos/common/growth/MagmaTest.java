package chaos.common.growth;

import chaos.board.Cell;
import chaos.common.AbstractActorTest;
import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.Growth;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;

/**
 * Tests this growth.
 *
 * @author Sean A. Irvine
 */
public class MagmaTest extends AbstractActorTest {

  @Override
  public Castable getCastable() {
    return new Magma();
  }

  public void test() {
    final Magma magma = new Magma();
    AbstractMonsterTest.checkAgainstSource(magma);
    assertEquals(Growth.GROW_FOUR_WAY, magma.getGrowthType());
    final Cell c = new Cell(0);
    assertTrue(magma.canGrowOver(c));
    final Lion l = new Lion();
    c.push(l);
    assertTrue(magma.canGrowOver(c));
    l.setState(State.DEAD);
    assertTrue(magma.canGrowOver(c));
    final Wizard1 w = new Wizard1();
    c.push(w);
    assertTrue(magma.canGrowOver(c));
    w.set(PowerUps.EARTHQUAKE_SHIELD, 1);
    assertFalse(magma.canGrowOver(c));
  }
}
