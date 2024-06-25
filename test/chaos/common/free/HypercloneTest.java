package chaos.common.free;

import chaos.board.World;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.inanimate.MagicWood;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class HypercloneTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Hyperclone();
  }

  public void test1() {
    final Hyperclone a = new Hyperclone();
    assertEquals(Castable.CAST_SINGLE, a.getCastFlags());
    final World world = new World(3, 3);
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    final Lion l = new Lion();
    l.setOwner(2);
    world.getCell(0).push(l);
    world.getCell(1).push(new Lion());
    world.getCell(2).push(new MagicWood());
    world.getCell(3).push(w);
    final Lion dl = new Lion();
    dl.setState(State.DEAD);
    world.getCell(5).push(dl);
    final Lion sl = new Lion();
    sl.setState(State.ASLEEP);
    world.getCell(6).push(sl);
    world.getCell(7).push(new Lion());
    world.getCell(8).push(new Lion());
    a.cast(null, null, null);
    a.cast(world, null, null);
    a.cast(null, w, null);
    a.cast(world, w, world.getCell(3));
    assertEquals(sl, world.actor(6));
    assertEquals(w, world.actor(3));
    assertTrue(world.actor(1) instanceof Lion);
    assertTrue(world.actor(2) instanceof MagicWood);
    if (world.actor(4) instanceof MagicWood) {
      assertTrue(world.actor(5) instanceof Lion);
    } else {
      assertTrue(world.actor(4) instanceof Lion);
      assertTrue(world.actor(5) instanceof MagicWood);
    }
  }

  public void testDoesNotMulticlone() {
    final Hyperclone a = new Hyperclone();
    assertEquals(Castable.CAST_SINGLE, a.getCastFlags());
    final World world = new World(1, 3);
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    w.setOwner(2);
    final Lion l = new Lion();
    l.setOwner(2);
    world.getCell(0).push(l);
    a.cast(world, w, null);
    assertEquals(l, world.actor(0));
    assertTrue(world.actor(1) instanceof Lion);
    assertNull(world.actor(2));
  }

}
