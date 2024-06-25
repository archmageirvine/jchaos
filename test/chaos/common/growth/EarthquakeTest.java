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
 * @author Sean A. Irvine
 */
public class EarthquakeTest extends AbstractActorTest {

  @Override
  public Castable getCastable() {
    return new Earthquake();
  }

  public void test() {
    final Earthquake eq = new Earthquake();
    AbstractMonsterTest.checkAgainstSource(eq);
    assertEquals(Growth.GROW_FOUR_WAY, eq.getGrowthType());
    final Cell c = new Cell(0);
    assertTrue(eq.canGrowOver(c));
    final Lion l = new Lion();
    c.push(l);
    assertTrue(eq.canGrowOver(c));
    l.setState(State.DEAD);
    assertTrue(eq.canGrowOver(c));
    final Wizard1 w = new Wizard1();
    c.push(w);
    assertTrue(eq.canGrowOver(c));
    w.set(PowerUps.EARTHQUAKE_SHIELD, 1);
    assertFalse(eq.canGrowOver(c));
  }
}
