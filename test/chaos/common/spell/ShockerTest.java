package chaos.common.spell;

import chaos.board.World;
import chaos.common.AbstractShield;
import chaos.common.AbstractShieldTest;
import chaos.common.Castable;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class ShockerTest extends AbstractShieldTest {

  @Override
  public Castable getCastable() {
    return new Shocker();
  }

  public void testInc() {
    final AbstractShield s = (AbstractShield) getCastable();
    assertEquals(12, s.getCastRange());
    assertEquals(10, s.increment());
    assertEquals(Castable.CAST_LIVING | Castable.CAST_LOS | Castable.CAST_INANIMATE, s.getCastFlags());
  }

  public void testFlags() {
    final Wizard1 w = new Wizard1();
    final World world = new World(1, 1);
    world.getCell(0).push(w);
    getCastable().cast(world, w, world.getCell(0), world.getCell(0));
  }
}
