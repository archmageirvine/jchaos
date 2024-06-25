package chaos.common.free;

import java.util.Collection;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractIncrement;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.monster.Horse;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectType;
import chaos.util.Event;
import chaos.util.EventListener;
import chaos.util.PolyshieldEvent;
import junit.framework.Assert;

/**
 * Abstract superclass for testing increment spells.
 * @author Sean A. Irvine
 */
public abstract class AbstractIncrementTest extends AbstractFreeCastableTest {

  private static class MyBool {
    boolean mCast = false;
  }

  private static class MyListener implements EventListener {
    final Cell mZero, mCC;

    MyListener(final Cell zero, final Cell cc) {
      mZero = zero;
      mCC = cc;
    }

    final MyBool mState = new MyBool();

    @Override
    public void update(final Event e) {
      if (e instanceof PolyshieldEvent && ((PolyshieldEvent) e).getEventType() == CellEffectType.SHIELD_GRANTED) {
        final Collection<Cell> cells = ((PolyshieldEvent) e).getCells();
        Assert.assertTrue(cells.contains(mCC));
        Assert.assertTrue(cells.contains(mZero));
        Assert.assertEquals(2, cells.size());
        Assert.assertFalse(mState.mCast);
        mState.mCast = true;
      }
    }

    MyBool getState() {
      return mState;
    }
  }

  public void test1() {
    final AbstractIncrement a = (AbstractIncrement) getCastable();
    assertEquals(Castable.CAST_SINGLE, a.getCastFlags());
    final World world = new World(1, 5);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    w.setState(State.ACTIVE);
    final Cell cc = world.getCell(1);
    cc.push(w);
    final Lion l = new Lion();
    l.setOwner(3);
    final Cell zero = world.getCell(0);
    zero.push(l);
    final MyListener listen = new MyListener(zero, cc);
    final MyBool state = listen.getState();
    world.register(listen);
    try {
      final Attribute attr = a.attribute();
      final int max = attr.max();
      final int ldv = l.getDefault(attr);
      final int wdv = w.getDefault(attr);
      for (int i = a.increment(); i <= max + a.increment(); i += a.increment()) {
        state.mCast = false;
        a.cast(world, w, cc);
        assertEquals(Math.min(max, ldv + i), l.get(attr));
        assertEquals(Math.min(max, wdv + i), w.get(attr));
        assertTrue(state.mCast);
      }
    } finally {
      world.deregister(listen);
    }
    a.cast(null, null, null);
    a.cast(world, null, null);
    a.cast(null, w, null);
    a.cast(world, w, null);
  }

  public void testMounted() {
    final AbstractIncrement a = (AbstractIncrement) getCastable();
    assertEquals(Castable.CAST_SINGLE, a.getCastFlags());
    final World world = new World(1, 5);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    w.setState(State.ACTIVE);
    final Horse h = new Horse();
    h.setOwner(3);
    h.setMount(w);
    final Cell cc = world.getCell(1);
    cc.push(h);
    final Attribute attr = a.attribute();
    final int max = attr.max();
    final int hdv = h.getDefault(attr);
    final int wdv = w.getDefault(attr);
    a.cast(world, w, cc);
    assertEquals(Math.min(max, hdv + a.increment()), h.get(attr));
    assertEquals(Math.min(max, wdv + a.increment()), w.get(attr));
  }
}
