package chaos.common.monster;

import chaos.Chaos;
import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractPolycasterTest;
import chaos.common.Castable;
import chaos.common.State;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class NecromancerTest extends AbstractPolycasterTest {

  @Override
  public Castable getCastable() {
    return new Necromancer();
  }

  public void testRaiseDead() {
    final World w = Chaos.getChaos().getWorld();
    final Necromancer n = new Necromancer();
    n.setOwner(1);
    final Cell cc = w.getCell(0);
    cc.push(n);
    final Lion l = new Lion();
    l.setState(State.DEAD);
    w.getCell(1).push(l);
    int c = 0;
    while (l.getState() == State.DEAD) {
      if (c++ == 5000) {
        fail("Failed to raise dead!");
      }
      n.doCasting(cc);
    }
  }

}
