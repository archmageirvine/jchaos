package chaos.common.free;

import java.util.Collection;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.FreeCastable;
import chaos.common.State;
import chaos.common.growth.Flood;
import chaos.common.inanimate.Pool;
import chaos.common.monster.Horse;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.EventListener;
import chaos.util.PolycellEffectEvent;
import junit.framework.Assert;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class CharmTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Charm();
  }

  private boolean mCast = false;

  protected void setCast() {
    mCast = true;
  }

  protected boolean getCast() {
    return mCast;
  }

  protected EventListener getListener(final World world) {
    return e -> {
      if (e instanceof PolycellEffectEvent) {
        final PolycellEffectEvent p = (PolycellEffectEvent) e;
        final Collection<Cell> s = p.getCells();
        Assert.assertTrue(!s.isEmpty());
        setCast();
      }
    };
  }

  protected int getLimit() {
    return 500;
  }

  public void test1() {
    final FreeCastable a = (FreeCastable) getCastable();
    assertEquals(Castable.CAST_SINGLE, a.getCastFlags());
    final World world = new World(7, 1);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    a.cast(null, null, null);
    a.cast(world, null, null);
    a.cast(null, w, null);
    final EventListener listen = e -> {
      if (e instanceof CellEffectEvent) {
        Assert.assertEquals(1, ((CellEffectEvent) e).getCellNumber());
        setCast();
      } else {
        Assert.fail();
      }
    };
    world.register(listen);
    try {
      a.cast(world, w, world.getCell(1));
      assertTrue(getCast());
      mCast = false;
      assertEquals(0, w.getScore());
    } finally {
      world.deregister(listen);
    }
    final Horse x = new Horse();
    x.set(Attribute.INTELLIGENCE, 0);
    x.setOwner(3);
    world.getCell(0).push(x);
    world.getCell(1).push(new Pool());
    final Horse h = new Horse();
    h.set(Attribute.INTELLIGENCE, 0);
    world.getCell(2).push(h);
    final Horse dh2 = new Horse();
    dh2.set(Attribute.INTELLIGENCE, 0);
    dh2.setState(State.DEAD);
    dh2.setOwner(0);
    world.getCell(3).push(dh2);
    final Flood flood = new Flood();
    flood.setOwner(2);
    world.getCell(3).push(flood);
    world.getCell(5).push(w);
    final EventListener listen2 = getListener(world);
    try {
      world.register(listen2);
      for (int i = 0; i < getLimit(); ++i) {
        a.cast(world, w, world.getCell(5));
      }
    } finally {
      world.deregister(listen2);
    }
    assertTrue(getCast());
    assertEquals(0, dh2.getOwner());
    assertEquals(h, world.actor(2));
    assertEquals(State.ACTIVE, h.getState());
    assertEquals(w, world.actor(5));
    assertEquals(a instanceof Charm ? 3 : Actor.OWNER_INDEPENDENT, h.getOwner());
  }

}
