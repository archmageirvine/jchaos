package chaos.common.monster;

import chaos.board.World;
import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.PowerUps;
import chaos.common.dragon.RedDragon;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class HiddenHorrorTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new HiddenHorror();
  }

  public void testHorrorness() {
    final World w = new World(1, 1);
    final HiddenHorror h = new HiddenHorror();
    w.getCell(0).push(h);
    w.getCell(0).reinstate();
    assertTrue(w.actor(0) instanceof RedDragon);
  }

  public void testHorrorness2() {
    final World w = new World(1, 1);
    final HiddenHorror h = new HiddenHorror();
    h.set(PowerUps.REINCARNATE, h.reincarnation() != null ? 1 : 0);
    w.getCell(0).push(h);
    w.getCell(0).reinstate();
    assertTrue(w.actor(0) instanceof MightyOrc);
  }
}
