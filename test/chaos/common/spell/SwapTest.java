package chaos.common.spell;

import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Castable;
import chaos.common.inanimate.ShadowWood;
import chaos.common.monster.Skeleton;
import chaos.common.monster.WoodElf;
import chaos.common.wizard.Wizard1;


/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class SwapTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new Swap();
  }

  public void test1() {
    final Swap a = new Swap();
    assertEquals(Castable.CAST_LIVING | Castable.CAST_GROWTH | Castable.CAST_LOS, a.getCastFlags());
    assertEquals(12, a.getCastRange());
    final World world = new World(9, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    world.getCell(1).push(w);
    final WoodElf elf = new WoodElf();
    elf.setOwner(1);
    world.getCell(0).push(elf);
    final ShadowWood sw = new ShadowWood();
    sw.setOwner(1);
    world.getCell(2).push(sw);
    final Skeleton sk = new Skeleton();
    sk.setOwner(3);
    world.getCell(3).push(sk);
    a.cast(world, w, world.getCell(0), world.getCell(1));
    assertEquals(3, elf.getOwner());
    assertEquals(1, sw.getOwner());
    assertEquals(1, sk.getOwner());
    a.cast(null, null, null, null);
    a.cast(world, null, null, null);
    a.cast(null, w, null, null);
  }

}
