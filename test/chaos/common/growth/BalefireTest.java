package chaos.common.growth;

import chaos.board.Cell;
import chaos.common.AbstractActorTest;
import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.Growth;
import chaos.common.PowerUps;
import chaos.common.Realm;
import chaos.common.State;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;

/**
 * Tests this growth.
 * @author Sean A. Irvine
 */
public class BalefireTest extends AbstractActorTest {

  @Override
  public Castable getCastable() {
    return new Balefire();
  }

  public void testBalefire() {
    final Balefire balefire = new Balefire();
    AbstractMonsterTest.checkAgainstSource(balefire);
    assertEquals(Growth.GROW_BY_COMBAT, balefire.getGrowthType());
    assertEquals(Realm.ETHERIC, balefire.getRealm());
    final Cell c = new Cell(0);
    assertTrue(balefire.canGrowOver(c));
    final Lion l = new Lion();
    c.push(l);
    assertTrue(balefire.canGrowOver(c));
    l.setState(State.DEAD);
    assertTrue(balefire.canGrowOver(c));
    final Wizard1 w = new Wizard1();
    c.push(w);
    assertTrue(balefire.canGrowOver(c));
    w.set(PowerUps.FIRE_SHIELD, 1);
    assertFalse(balefire.canGrowOver(c));
  }
}
