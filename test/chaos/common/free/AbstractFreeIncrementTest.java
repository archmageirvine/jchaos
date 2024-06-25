package chaos.common.free;

import chaos.board.World;
import chaos.common.AbstractFreeIncrement;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectType;

/**
 * Tests free attribute increment spells.
 * @author Sean A. Irvine
 */
public abstract class AbstractFreeIncrementTest extends AbstractFreeCastableTest {

  public void testCast() {
    final AbstractFreeIncrement x = (AbstractFreeIncrement) getCastable();
    assertEquals(Castable.CAST_SINGLE, x.getCastFlags());
    final Wizard1 w = new Wizard1();
    w.increment(Attribute.INTELLIGENCE, -w.get(Attribute.INTELLIGENCE));
    w.increment(Attribute.MAGICAL_RESISTANCE, -w.get(Attribute.MAGICAL_RESISTANCE));
    final World world = new World(1, 1);
    world.getCell(0).push(w);
    final Attribute attr = x.attribute();
    final int max = attr.max();
    final int expect = w.getDefault(attr) + x.increment();
    castAndListenCheck(x, world, w, 0, CellEffectType.REDRAW_CELL, CellEffectType.SHIELD_GRANTED);
    assertEquals(Math.min(max, expect), w.get(attr));
    final Attribute rec = attr.recovery();
    if (rec != null) {
      if (w.get(attr) != max) {
        w.increment(attr, max);
      }
      for (int i = 1; i <= 5; ++i) {
        x.cast(world, w, world.getCell(0), world.getCell(0));
        assertEquals(w.getDefault(rec) + i, w.get(rec));
      }
    }
    x.cast(world, w, world.getCell(0), null);
    x.cast(world, w, null, world.getCell(0));
    x.cast(world, null, world.getCell(0), world.getCell(0));
    x.cast(null, w, world.getCell(0), world.getCell(0));
  }

}
