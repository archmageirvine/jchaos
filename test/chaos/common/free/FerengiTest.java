package chaos.common.free;

import chaos.board.World;
import chaos.common.Castable;
import chaos.common.CastableList;
import chaos.common.State;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectType;
import chaos.util.RankingTable;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class FerengiTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Ferengi();
  }

  public void testCast1() {
    final Ferengi x = new Ferengi();
    assertEquals(Castable.CAST_SINGLE, x.getCastFlags());
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    final Wizard1 w1 = new Wizard1();
    w1.setState(State.ACTIVE);
    final World world = new World(1, 2);
    final CastableList good = new CastableList(10, 0, 10);
    w.setCastableList(good);
    final CastableList bad = new CastableList(10, 10, 10);
    w1.setCastableList(bad);
    world.getWizardManager().setWizard(1, w);
    world.getWizardManager().setWizard(0, w1);
    world.getCell(0).push(w1);
    world.getCell(1).push(w);
    castAndListenCheck(x, world, w, 1, CellEffectType.REDRAW_CELL, CellEffectType.BONUS);
    assertEquals(9, bad.getCount());
    assertEquals(1, good.getCount());
    final int rank = RankingTable.getRanking(good.getVisible()[0]);
    for (final Castable cc : bad.getVisible()) {
      assertTrue(rank >= RankingTable.getRanking(cc));
    }
    nullParametersCastCheck(x, world, w);
  }

}
