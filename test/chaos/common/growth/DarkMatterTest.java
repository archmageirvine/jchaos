package chaos.common.growth;

import chaos.board.Cell;
import chaos.common.AbstractActorTest;
import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.Growth;
import chaos.common.State;
import chaos.common.monster.Lion;

/**
 * Tests this growth.
 *
 * @author Sean A. Irvine
 */
public class DarkMatterTest extends AbstractActorTest {

  @Override
  public Castable getCastable() {
    return new DarkMatter();
  }

  public void testFire() {
    final DarkMatter darkMatter = new DarkMatter();
    AbstractMonsterTest.checkAgainstSource(darkMatter);
    assertEquals(Growth.GROW_BY_COMBAT, darkMatter.getGrowthType());
    final Cell c = new Cell(0);
    assertTrue(darkMatter.canGrowOver(c));
    final Lion l = new Lion();
    c.push(l);
    assertFalse(darkMatter.canGrowOver(c));
    l.setState(State.DEAD);
    assertTrue(darkMatter.canGrowOver(c));
  }
}
