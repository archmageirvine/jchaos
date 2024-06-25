package chaos.common.free;

import java.util.Collection;

import chaos.board.Cell;
import chaos.board.World;
import chaos.util.AudioEvent;
import chaos.util.CellEffectEvent;
import chaos.util.Event;
import chaos.util.EventListener;
import chaos.util.PolycellEffectEvent;
import junit.framework.Assert;

/**
 * Used for testing events.
 * @author Sean A. Irvine
 */
class YourListener implements EventListener {

  private final World mWorld;

  YourListener(final World world) {
    mWorld = world;
  }

  @Override
  public void update(final Event e) {
    if (e instanceof CellEffectEvent) {
      Assert.assertTrue(e instanceof AudioEvent);
    } else if (e instanceof PolycellEffectEvent) {
      final PolycellEffectEvent p = (PolycellEffectEvent) e;
      final Collection<Cell> s = p.getCells();
      Assert.assertEquals(3, s.size());
      Assert.assertTrue(s.contains(mWorld.getCell(0)));
      Assert.assertTrue(s.contains(mWorld.getCell(1)));
      Assert.assertTrue(s.contains(mWorld.getCell(3)));
    }
  }
}

