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
public class SpikyTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Spiky();
  }

  @Override
  public void testReincarnation() {
    assertEquals(null, new Spiky().reincarnation());
  }

  public void testGrowOver() {
    final World w = new World(2, 1);
    final Spiky f = new Spiky();
    assertEquals(Growth.GROW_FOUR_WAY, f.getGrowthType());
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
    assertTrue(w.actor(1) instanceof Spiky || w.actor(1) instanceof DarkMatter);
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertTrue(f != w.actor(1));
    assertTrue(w.actor(1) instanceof Spiky || w.actor(1) instanceof DarkMatter);
  }

  public void testGrowOverDead() {
    final World w = new World(2, 1);
    final Spiky f = new Spiky();
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
