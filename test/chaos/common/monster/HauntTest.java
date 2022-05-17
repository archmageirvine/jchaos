package chaos.common.monster;

import chaos.board.World;
import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.State;

/**
 * Tests this monster.
 *
 * @author Sean A. Irvine
 */
public class HauntTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Haunt();
  }

  public void testOwnerChange() {
    final World w = new World(1, 1);
    final Haunt s = (Haunt) mCastable;
    s.setState(State.ACTIVE);
    s.setOwner(1);
    final boolean[] seen = new boolean[w.getWizardManager().getMaximumPlayerNumber()];
    int nochange = 0;
    for (int i = 0; i < 50000; ++i) {
      final int o = s.getOwner();
      s.update(w, null);
      if (s.getOwner() == o) {
        ++nochange;
      }
      seen[s.getOwner()] = true;
    }
    assertTrue(nochange > 40000);
    assertFalse(seen[0]); // don't change owner to nobody
    for (int i = 1; i < seen.length; ++i) {
      assertTrue(seen[i]);
    }
  }

}
