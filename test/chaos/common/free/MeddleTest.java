package chaos.common.free;

import chaos.board.World;
import chaos.common.Castable;
import chaos.common.monster.Ghost;
import chaos.common.monster.Imp;
import chaos.common.monster.Vampire;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectType;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class MeddleTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Meddle();
  }

  public void test1() {
    final Meddle a = new Meddle();
    assertEquals(Castable.CAST_SINGLE, a.getCastFlags());
    final World world = new World(1, 5);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    a.cast(null, null, null);
    a.cast(world, null, null);
    a.cast(null, w, null);
    a.cast(world, w, null);
    final Imp l = new Imp();
    world.getCell(0).push(l);
    castAndListenCheck(a, world, w, -1, CellEffectType.REDRAW_CELL, CellEffectType.REINCARNATE);
    assertEquals(l, world.actor(0));
    final Vampire g = new Vampire();
    world.getCell(0).push(g);
    a.cast(world, w, null);
    assertTrue(world.actor(0) instanceof Ghost);
  }
}
