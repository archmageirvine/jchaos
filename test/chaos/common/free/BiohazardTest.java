package chaos.common.free;

import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class BiohazardTest extends AbstractFreeDecrementTest {

  @Override
  public Castable getCastable() {
    return new Biohazard();
  }

  public void test1() {
    final Biohazard a = new Biohazard();
    final World world = new World(1, 5);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    final Lion l = new Lion();
    l.setOwner(2);
    l.set(Attribute.LIFE_RECOVERY, 2);
    world.getCell(0).push(l);
    for (int i = 0; i < Attribute.LIFE_RECOVERY.max() + 2; ++i) {
      a.cast(world, w, null);
      assertEquals(1 - i, l.get(Attribute.LIFE_RECOVERY));
    }
    a.cast(world, w, null);
    assertEquals(Attribute.LIFE_RECOVERY.min(), l.get(Attribute.LIFE_RECOVERY));
  }
}
