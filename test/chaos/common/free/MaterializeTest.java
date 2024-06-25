package chaos.common.free;

import chaos.board.World;
import chaos.common.Castable;
import chaos.common.Realm;
import chaos.common.monster.Skeleton;
import chaos.common.monster.Vampire;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectType;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class MaterializeTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Materialize();
  }

  public void test1() {
    final Materialize a = new Materialize();
    assertEquals(Castable.CAST_SINGLE, a.getCastFlags());
    final World world = new World(1, 5);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    a.cast(null, null, null);
    a.cast(world, null, null);
    a.cast(null, w, null);
    a.cast(world, w, null);
    final Skeleton l = new Skeleton();
    world.getCell(0).push(l);
    castAndListenCheck(a, world, w, -1, CellEffectType.REDRAW_CELL, CellEffectType.CHANGE_REALM);
    assertEquals(Realm.MATERIAL, l.getRealm());
    final Vampire g = new Vampire();
    world.getCell(0).push(g);
    a.cast(world, w, null);
    assertEquals(Realm.MATERIAL, g.getRealm());
  }
}
