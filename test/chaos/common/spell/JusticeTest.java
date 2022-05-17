package chaos.common.spell;

import java.util.Set;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractDrainerTest;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.inanimate.MagicWood;
import chaos.common.monster.Horse;
import chaos.common.monster.Skeleton;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class JusticeTest extends AbstractDrainerTest {

  @Override
  public Castable getCastable() {
    return new Justice();
  }

  public void testParams() {
    final Justice cons = new Justice();
    assertEquals(Castable.CAST_LIVING, cons.getCastFlags());
    assertEquals(Castable.MAX_CAST_RANGE, cons.getCastRange());
    assertEquals(18, cons.getDamage());
    assertEquals(2, cons.getMultiplicity());
    final World w = new World(3, 3);
    w.getCell(3).push(new Horse());
    w.getCell(5).push(new MagicWood());
    final Horse h = new Horse();
    h.setState(State.DEAD);
    w.getCell(6).push(h);
    w.getCell(7).push(new Skeleton());
    final Wizard1 wiz = new Wizard1();
    w.getCell(8).push(wiz);
    final Set<Cell> a = cons.getAffectedCells(w, w.getCell(7));
    assertEquals(1, a.size());
    assertTrue(a.contains(w.getCell(7)));
  }
}
