package chaos.common.spell;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Castable;
import chaos.common.PowerUps;
import chaos.common.monster.StoneGolem;
import chaos.common.monster.Tiger;
import chaos.common.monster.Troll;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.EventListener;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class ReincarnateTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new Reincarnate();
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
    final Castable x = new Reincarnate();
    assertEquals(Castable.CAST_LIVING | Castable.CAST_LOS, x.getCastFlags());
    assertEquals(10, x.getCastRange());
    final Wizard1 w = new Wizard1();
    final World world = new World(1, 1);
    final EventListener listen = e -> {
      if (e instanceof CellEffectEvent) {
        final CellEffectEvent ce = (CellEffectEvent) e;
        if (ce.getEventType() == CellEffectType.REDRAW_CELL) {
          assertFalse(getRedraw());
          setRedraw();
        } else if (ce.getEventType() == CellEffectType.TWIRL) {
          assertFalse(getCast());
          setCast();
        }
      }
    };
    final Tiger l = new Tiger();
    world.getCell(0).push(l);
    world.register(listen);
    x.cast(world, w, world.getCell(0), world.getCell(0));
    assertTrue(getRedraw());
    assertTrue(getCast());
    assertTrue(l.is(PowerUps.REINCARNATE));
    world.deregister(listen);
    x.cast(world, w, world.getCell(0), null);
    x.cast(world, w, null, world.getCell(0));
    x.cast(world, null, world.getCell(0), world.getCell(0));
    x.cast(null, w, world.getCell(0), world.getCell(0));
  }

  public void testFilter() {
    final World w = new World(2, 1);
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(1);
    final Reincarnate x = new Reincarnate();
    final HashSet<Cell> t = new HashSet<>();
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    final Cell cn = new Cell(23);
    t.add(cn);
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    final Cell c = new Cell(0);
    final Tiger l = new Tiger();
    l.setOwner(1);
    c.push(l);
    t.add(c);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertTrue(t.contains(c));
    final Cell c2 = new Cell(1);
    final Tiger l2 = new Tiger();
    l2.setOwner(1);
    c2.push(l2);
    t.add(c2);
    x.filter(t, wiz, w);
    assertEquals(2, t.size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
    l2.set(PowerUps.REINCARNATE, l2.reincarnation() != null ? 1 : 0);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertTrue(t.contains(c));
  }

  public void testFilter2() {
    final World w = new World(2, 1);
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(1);
    final Reincarnate x = new Reincarnate();
    final HashSet<Cell> t = new HashSet<>();
    final Cell c0 = new Cell(0);
    final Troll l = new Troll();
    l.setOwner(1);
    c0.push(l);
    t.add(c0);
    final Cell c1 = new Cell(1);
    final StoneGolem s = new StoneGolem();
    s.setOwner(1);
    c1.push(s);
    t.add(c1);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertTrue(t.contains(c0));
  }
}
