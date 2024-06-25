package chaos.common.spell;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Monster;
import chaos.common.State;
import chaos.common.dragon.RedDragon;
import chaos.common.monster.Aesculapius;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.EventListener;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class RangeBoostTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new RangeBoost();
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
    final Castable x = new RangeBoost();
    assertEquals(Castable.CAST_LIVING, x.getCastFlags());
    assertEquals(Castable.MAX_CAST_RANGE, x.getCastRange());
    final Wizard1 w = new Wizard1();
    final World world = new World(1, 2);
    final EventListener listen = e -> {
      if (e instanceof CellEffectEvent) {
        final CellEffectEvent ce = (CellEffectEvent) e;
        if (ce.getEventType() == CellEffectType.REDRAW_CELL) {
          assertFalse(getRedraw());
          setRedraw();
        } else if (ce.getEventType() == CellEffectType.SHIELD_GRANTED) {
          assertFalse(getCast());
          setCast();
        }
      }
    };
    world.getCell(0).push(w);
    world.register(listen);
    w.set(Attribute.RANGE, 4);
    w.set(Attribute.RANGED_COMBAT, 3);
    x.cast(world, w, world.getCell(0), world.getCell(0));
    assertEquals(8, w.get(Attribute.RANGE));
    assertEquals(7, w.get(Attribute.RANGED_COMBAT));
    assertTrue(getRedraw());
    assertTrue(getCast());
    w.set(Attribute.RANGE, Attribute.RANGE.max() - 1);
    w.set(Attribute.RANGED_COMBAT, Attribute.RANGED_COMBAT.max() - 1);
    mCast = false;
    mRedraw = false;
    x.cast(world, w, world.getCell(0), world.getCell(0));
    assertEquals(Attribute.RANGE.max(), w.get(Attribute.RANGE));
    assertEquals(Attribute.RANGED_COMBAT.max(), w.get(Attribute.RANGED_COMBAT));
    assertTrue(getRedraw());
    assertTrue(getCast());
    w.set(Attribute.RANGE, Attribute.RANGE.max());
    w.set(Attribute.RANGED_COMBAT, Attribute.RANGED_COMBAT.max());
    mCast = false;
    mRedraw = false;
    x.cast(world, w, world.getCell(0), world.getCell(0));
    assertEquals(1, w.get(Attribute.RANGE_RECOVERY));
    assertEquals(1, w.get(Attribute.RANGED_COMBAT_RECOVERY));
    assertTrue(getRedraw());
    assertTrue(getCast());
    world.deregister(listen);
    w.set(Attribute.RANGE_RECOVERY, Attribute.RANGE_RECOVERY.max());
    w.set(Attribute.RANGED_COMBAT_RECOVERY, Attribute.RANGED_COMBAT_RECOVERY.max());
    x.cast(world, w, world.getCell(0), world.getCell(0));
    assertEquals(Attribute.RANGE_RECOVERY.max(), w.get(Attribute.RANGE_RECOVERY));
    assertEquals(Attribute.RANGED_COMBAT_RECOVERY.max(), w.get(Attribute.RANGED_COMBAT_RECOVERY));
    x.cast(world, w, world.getCell(0), null);
    x.cast(world, w, null, world.getCell(0));
    x.cast(world, null, world.getCell(0), world.getCell(0));
    x.cast(null, w, world.getCell(0), world.getCell(0));
    final Monster h = new Aesculapius();
    world.getCell(1).push(h);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(7, h.get(Attribute.RANGE));
    assertEquals(-5, h.get(Attribute.RANGED_COMBAT));
    h.set(Attribute.RANGED_COMBAT, Attribute.RANGED_COMBAT.min() + 1);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(11, h.get(Attribute.RANGE));
    assertEquals(Attribute.RANGED_COMBAT.min(), h.get(Attribute.RANGED_COMBAT));
    h.set(Attribute.RANGED_COMBAT, Attribute.RANGED_COMBAT.min());
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(15, h.get(Attribute.RANGE));
    assertEquals(Attribute.RANGED_COMBAT.min(), h.get(Attribute.RANGED_COMBAT));
    assertEquals(1, h.get(Attribute.RANGED_COMBAT_RECOVERY));
    h.set(Attribute.RANGED_COMBAT_RECOVERY, Attribute.RANGED_COMBAT_RECOVERY.max());
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(19, h.get(Attribute.RANGE));
    assertEquals(Attribute.RANGED_COMBAT_RECOVERY.max(), h.get(Attribute.RANGED_COMBAT_RECOVERY));
  }

  public void testCast2() {
    final Castable x = new RangeBoost();
    final Wizard1 w = new Wizard1();
    final World world = new World(1, 2);
    final Lion l = new Lion();
    world.getCell(0).push(w);
    world.getCell(1).push(l);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(0, l.get(Attribute.RANGE));
    assertEquals(0, l.get(Attribute.RANGED_COMBAT));
  }

  public void testCast3() {
    final Castable x = new RangeBoost();
    final Wizard1 w = new Wizard1();
    final World world = new World(1, 2);
    final Lion l = new Lion();
    l.set(Attribute.RANGE, 1);
    world.getCell(0).push(w);
    world.getCell(1).push(l);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(5, l.get(Attribute.RANGE));
    assertEquals(4, l.get(Attribute.RANGED_COMBAT));
  }

  public void testFilter() {
    final World w = new World(2, 1);
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(1);
    final RangeBoost x = new RangeBoost();
    final HashSet<Cell> t = new HashSet<>();
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    final Cell cn = new Cell(23);
    t.add(cn);
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    final Cell c = new Cell(0);
    final RedDragon l = new RedDragon();
    l.setOwner(1);
    c.push(l);
    t.add(c);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertTrue(t.contains(c));
    final Cell c2 = new Cell(1);
    final RedDragon l2 = new RedDragon();
    l2.setOwner(1);
    c2.push(l2);
    t.add(c2);
    x.filter(t, wiz, w);
    assertEquals(2, t.size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
    final Cell c3 = new Cell(42);
    wiz.setState(State.ACTIVE);
    c3.push(wiz);
    t.add(c3);
    x.filter(t, wiz, w);
    assertEquals(2, t.size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
  }
}
