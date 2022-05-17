package chaos.common.inanimate;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.monster.Lion;

/**
 * Tests this inanimate.
 *
 * @author Sean A. Irvine
 */
public class RoperTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Roper();
  }

  @Override
  public void testReincarnation() {
    assertEquals(null, new Roper().reincarnation());
  }

  @Override
  public void testUpdate() {
    final World w = new World(5, 5);
    final Roper r = new Roper();
    r.cast(null, null, null, null);
    final Cell c = w.getCell(0);
    c.push(r);
    w.getCell(w.size() - 1).push(new Lion());
    final boolean[] seen = new boolean[w.size()];
    int totalChanges = 0;
    for (int k = 0; k < 100000; ++k) {
      r.setMoved(true);
      assertFalse(r.update(w, c));
      if (w.actor(0) == null) {
        // it moved
        for (int j = 1; j < w.size(); ++j) {
          if (w.actor(j) == r) {
            seen[j] |= true;
            w.getCell(j).pop();
            c.push(r);
            ++totalChanges;
            break;
          }
        }
      }
    }
    assertTrue(totalChanges < 100000 / 6);
    assertTrue(totalChanges > 100000 / 10);
    for (int k = 1; k < w.size() - 1; ++k) {
      assertTrue(seen[k]);
    }
    assertFalse(seen[w.size() - 1]);
  }
}
