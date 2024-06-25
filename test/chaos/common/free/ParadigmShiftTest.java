package chaos.common.free;

import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.CastableList;
import chaos.common.monster.Skeleton;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectType;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class ParadigmShiftTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new ParadigmShift();
  }

  public void test1() {
    final ParadigmShift a = new ParadigmShift();
    assertEquals(Castable.CAST_SINGLE, a.getCastFlags());
    final World world = new World(1, 6);
    final Wizard1 w = new Wizard1();
    w.setCastableList(new CastableList(100, 100, 24));
    world.getWizardManager().setWizard(3, w);
    // make sure castable lists are not full
    final CastableList cl = world.getWizardManager().getWizard(3).getCastableList();
    if (cl.getCount() > 0) {
      cl.use(cl.getVisible()[0]);
    }
    final int w3c = cl.getCount();
    final CastableList icl = world.getWizardManager().getIndependent().getCastableList();
    assertNull(icl);
    final Skeleton sk = new Skeleton();
    sk.setOwner(w.getOwner());
    world.getCell(0).push(sk);
    final Skeleton sk2 = new Skeleton();
    sk2.setOwner(Actor.OWNER_INDEPENDENT);
    world.getCell(1).push(sk2);
    world.getCell(5).push(w);
    castAndListenCheck(a, world, w, 5, CellEffectType.TWIRL, CellEffectType.REDRAW_CELL);
    a.cast(world, w, world.getCell(5));
    assertFalse(world.actor(0) instanceof Skeleton);
    assertFalse(world.actor(1) instanceof Skeleton);
    assertEquals(w3c + 1, cl.getCount());
    a.cast(null, null, null);
    a.cast(world, null, null);
    a.cast(null, w, null);
    a.cast(world, w, null);
  }
}
