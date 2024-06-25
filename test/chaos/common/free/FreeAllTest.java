package chaos.common.free;

import java.util.Collection;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Castable;
import chaos.util.CellEffectEvent;
import chaos.util.EventListener;
import chaos.util.PolycellEffectEvent;
import junit.framework.Assert;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class FreeAllTest extends CharmTest {

  @Override
  public Castable getCastable() {
    return new FreeAll();
  }

  @Override
  protected int getLimit() {
    return 1;
  }

  @Override
  protected EventListener getListener(final World world) {
    return e -> {
      if (e instanceof CellEffectEvent) {
        Assert.fail();
      } else if (e instanceof PolycellEffectEvent) {
        final PolycellEffectEvent p = (PolycellEffectEvent) e;
        final Collection<Cell> s = p.getCells();
        Assert.assertEquals(3, s.size());
        Assert.assertTrue(s.contains(world.getCell(0)));
        Assert.assertTrue(s.contains(world.getCell(2)));
        setCast();
      }
    };
  }
}
