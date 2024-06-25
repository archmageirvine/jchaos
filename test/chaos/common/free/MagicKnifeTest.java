package chaos.common.free;

import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.PowerUps;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectType;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class MagicKnifeTest extends AbstractFreeIncrementTest {

  @Override
  public Castable getCastable() {
    return new MagicKnife();
  }

  public void testCast() {
    final MagicKnife x = new MagicKnife();
    assertEquals(Castable.CAST_SINGLE, x.getCastFlags());
    final Wizard1 w = new Wizard1();
    final World world = new World(1, 1);
    castAndListenCheck(x, world, w, 0, CellEffectType.REDRAW_CELL, CellEffectType.SHIELD_GRANTED);
    assertEquals(1, w.get(PowerUps.SWORD));
    assertEquals(1, w.get(PowerUps.ATTACK_ANY_REALM));
    assertEquals(4, w.get(Attribute.COMBAT));
    for (int i = 0; i < 50; ++i) {
      x.cast(world, w, world.getCell(0));
      assertEquals(1, w.get(PowerUps.SWORD));
      assertEquals(1, w.get(PowerUps.ATTACK_ANY_REALM));
      assertEquals(Math.min(7 + 3 * i, Attribute.COMBAT.max()), w.get(Attribute.COMBAT));
    }
    nullParametersCastCheck(x, world, w);
  }
}
