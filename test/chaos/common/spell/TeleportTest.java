package chaos.common.spell;

import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.growth.GooeyBlob;
import chaos.common.monster.Lion;
import chaos.common.monster.Orc;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.EventListener;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class TeleportTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new Teleport();
  }

  private boolean mCast = false;

  private void setCast() {
    mCast = true;
  }

  private boolean getCast() {
    return mCast;
  }

  private boolean mRedraw = false;

  private void setRedraw() {
    mRedraw = true;
  }

  private boolean getRedraw() {
    return mRedraw;
  }

  public void testCast() {
    final Castable x = new Teleport();
    assertEquals(Castable.CAST_ANY, x.getCastFlags());
    assertEquals(Castable.MAX_CAST_RANGE, x.getCastRange());
    final Wizard1 w = new Wizard1();
    final World world = new World(1, 10);
    final EventListener listen = e -> {
      if (e instanceof CellEffectEvent) {
        final CellEffectEvent ce = (CellEffectEvent) e;
        if (ce.getEventType() == CellEffectType.REDRAW_CELL) {
          setRedraw();
        } else if (ce.getEventType() == CellEffectType.WARP_OUT) {
          assertFalse(getCast());
          setCast();
        }
      }
    };
    world.getCell(0).push(w);
    world.register(listen);
    x.cast(world, w, world.getCell(6), world.getCell(0));
    assertTrue(getRedraw());
    assertTrue(getCast());
    assertNull(world.actor(0));
    assertEquals(w, world.actor(6));
    world.deregister(listen);
    x.cast(world, w, world.getCell(0), null);
    x.cast(world, w, null, world.getCell(0));
    x.cast(world, null, world.getCell(0), world.getCell(0));
    x.cast(null, w, world.getCell(0), world.getCell(0));
  }

  public void testComplicatedCopy() {
    final World w = new World(1, 2);
    final Lion l = new Lion();
    l.setState(State.DEAD);
    w.getCell(0).push(l);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    w.getCell(0).push(wiz);
    final Orc o = new Orc();
    w.getCell(1).push(o);
    final GooeyBlob gb = new GooeyBlob();
    w.getCell(1).push(gb);
    new Teleport().cast(w, wiz, w.getCell(1), w.getCell(0));
    assertEquals(gb, w.getCell(0).pop());
    assertEquals(o, w.actor(0));
    assertEquals(wiz, w.getCell(1).pop());
    assertEquals(l, w.actor(1));
  }

}
