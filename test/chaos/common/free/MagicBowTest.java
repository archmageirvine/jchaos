package chaos.common.free;

import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.PowerUps;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class MagicBowTest extends AbstractFreeIncrementTest {

  @Override
  public Castable getCastable() {
    return new MagicBow();
  }

  public void testMBSpecial() {
    final MagicBow x = new MagicBow();
    final Wizard1 w = new Wizard1();
    final World world = new World(1, 1);
    x.cast(world, w, world.getCell(0));
    assertEquals(1, w.get(PowerUps.BOW));
    assertEquals(1, w.get(PowerUps.ATTACK_ANY_REALM));
    assertEquals(Attribute.LIFE, w.getRangedCombatApply());
  }
}
