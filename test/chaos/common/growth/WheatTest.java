package chaos.common.growth;

import chaos.board.World;
import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.Growth;
import chaos.common.State;

/**
 * Tests this inanimate.
 * @author Sean A. Irvine
 */
public class WheatTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Wheat();
  }

  @Override
  public void testCastRange() {
    assertEquals(0, getCastable().getCastRange());
  }

  @Override
  public void testReincarnation() {
    assertNull(new Wheat().reincarnation());
  }

  public void testGrowOver() {
    final World w = new World(2, 1);
    final Wheat f = new Wheat();
    assertEquals(Growth.GROW_OVER, f.getGrowthType());
    w.getCell(0).push(f);
    try {
      f.grow(1, w);
      fail("Grew wrong cell");
    } catch (final RuntimeException e) {
      assertEquals("Requested cell does not contain this growth", e.getMessage());
    }
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertTrue(f != w.actor(1));
    assertTrue(w.actor(1) instanceof Wheat);
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertTrue(f != w.actor(1));
    assertTrue(w.actor(1) instanceof Wheat);
  }

  public void testGrowOverDead() {
    final World w = new World(2, 1);
    final Wheat f = new Wheat();
    f.setState(State.DEAD);
    w.getCell(0).push(f);
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertEquals(null, w.actor(1));
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertEquals(null, w.actor(1));
  }
}
