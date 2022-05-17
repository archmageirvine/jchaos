package chaos.common.free;

import junit.framework.Assert;
import chaos.util.CellEffectEvent;
import chaos.util.Event;
import chaos.util.EventListener;

/**
 * Used for testing events.
 *
 * @author Sean A. Irvine
 */
public class MyEventListener implements EventListener {
  @Override
  public void update(final Event e) {
    if (e instanceof CellEffectEvent) {
      Assert.assertEquals(1, ((CellEffectEvent) e).getCellNumber());
    } else {
      Assert.fail();
    }
  }
}

