package chaos.common.growth;

import chaos.board.World;
import chaos.common.AbstractMonsterTest;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Growth;
import chaos.common.State;
import chaos.common.monster.Orc;

/**
 * Tests this inanimate.
 * @author Sean A. Irvine
 */
public class VioletFungiTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new VioletFungi();
  }

  @Override
  public void testReincarnation() {
    assertEquals(null, new VioletFungi().reincarnation());
  }

  public void testGrowOver() {
    final World w = new World(2, 1);
    final VioletFungi f = new VioletFungi();
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
    assertTrue(w.actor(1) instanceof VioletFungi);
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertTrue(f != w.actor(1));
    assertTrue(w.actor(1) instanceof VioletFungi);
  }

  public void testGrowOverDead() {
    final World w = new World(2, 1);
    final VioletFungi f = new VioletFungi();
    f.setState(State.DEAD);
    w.getCell(0).push(f);
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertEquals(null, w.actor(1));
    f.grow(0, w);
    assertEquals(f, w.actor(0));
    assertEquals(null, w.actor(1));
  }

  public void testGrowOverOrc() {
    final World w = new World(2, 1);
    final VioletFungi f = new VioletFungi();
    f.setOwner(2);
    w.getCell(0).push(f);
    final Orc o = new Orc();
    w.getCell(1).push(o);
    for (int i = 0; i < 20; ++i) {
      f.grow(0, w);
    }
    assertEquals(f, w.actor(0));
    assertEquals(o, w.actor(1));
    assertEquals(new Orc().get(Attribute.LIFE), o.get(Attribute.LIFE));
  }

}
