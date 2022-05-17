package chaos.common.monster;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.inanimate.Web;

/**
 * Tests this monster.
 *
 * @author Sean A. Irvine
 */
public class GiantSpiderTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new GiantSpider();
  }

  public void testWeb() {
    final GiantSpider s = new GiantSpider();
    final World w = new World(1, 1);
    final Cell c = w.getCell(0);
    c.push(s);
    s.update(w, c);
    s.update(w, c);
    assertTrue(c.contains(Web.class));
    c.pop();
    c.pop();
    assertFalse(c.contains(Web.class));
    s.setState(State.DEAD);
    c.push(s);
    s.update(w, c);
    assertFalse(c.contains(Web.class));
  }
}
