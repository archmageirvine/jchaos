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
public class FireTest extends AbstractActorTest {

  @Override
  public Castable getCastable() {
    return new Fire();
  }

  public void testFire() {
    final Fire fire = new Fire();
    AbstractMonsterTest.checkAgainstSource(fire);
    assertEquals(Growth.GROW_BY_COMBAT, fire.getGrowthType());
    final Cell c = new Cell(0);
    assertTrue(fire.canGrowOver(c));
    final Lion l = new Lion();
    c.push(l);
    assertTrue(fire.canGrowOver(c));
    l.setState(State.DEAD);
    assertTrue(fire.canGrowOver(c));
    final Wizard1 w = new Wizard1();
    c.push(w);
    assertTrue(fire.canGrowOver(c));
    w.set(PowerUps.FIRE_SHIELD, 1);
    assertFalse(fire.canGrowOver(c));
  }
}
