package chaos.common.spell;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Monster;
import chaos.common.State;
import chaos.common.TargetFilter;
import chaos.common.monster.Centaur;
import chaos.common.monster.Hippocrates;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.EventListener;
import junit.framework.Assert;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class HealTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new Heal();
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

  public void testFlags() {
    final Castable x = getCastable();
    assertEquals(Castable.CAST_LIVING | Castable.CAST_INANIMATE | Castable.CAST_GROWTH, x.getCastFlags());
    assertEquals(Castable.MAX_CAST_RANGE, x.getCastRange());
  }

  protected void checkState(final Monster m) {
    assertEquals(m.get(Attribute.LIFE), m.getDefault(Attribute.LIFE));
    assertEquals(m.get(Attribute.LIFE_RECOVERY), m.getDefault(Attribute.LIFE_RECOVERY));
    assertEquals(m.get(Attribute.MAGICAL_RESISTANCE), m.getDefault(Attribute.MAGICAL_RESISTANCE));
    assertEquals(m.get(Attribute.MAGICAL_RESISTANCE_RECOVERY), m.getDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY));
    assertEquals(m.get(Attribute.AGILITY), m.getDefault(Attribute.AGILITY));
    assertEquals(m.get(Attribute.AGILITY_RECOVERY), m.getDefault(Attribute.AGILITY_RECOVERY));
    assertEquals(m.get(Attribute.INTELLIGENCE), m.getDefault(Attribute.INTELLIGENCE));
    assertEquals(m.get(Attribute.INTELLIGENCE_RECOVERY), m.getDefault(Attribute.INTELLIGENCE_RECOVERY));
    assertEquals(m.get(Attribute.MOVEMENT), m.getDefault(Attribute.MOVEMENT));
    assertEquals(m.get(Attribute.MOVEMENT_RECOVERY), m.getDefault(Attribute.MOVEMENT_RECOVERY));
    assertEquals(m.get(Attribute.RANGE), m.getDefault(Attribute.RANGE));
    assertEquals(m.get(Attribute.RANGE_RECOVERY), m.getDefault(Attribute.RANGE_RECOVERY));
    assertEquals(m.get(Attribute.COMBAT), m.getDefault(Attribute.COMBAT));
    assertEquals(m.get(Attribute.COMBAT_RECOVERY), m.getDefault(Attribute.COMBAT_RECOVERY));
    assertEquals(m.get(Attribute.RANGED_COMBAT), m.getDefault(Attribute.RANGED_COMBAT));
    assertEquals(m.get(Attribute.RANGED_COMBAT_RECOVERY), m.getDefault(Attribute.RANGED_COMBAT_RECOVERY));
    assertEquals(m.get(Attribute.SPECIAL_COMBAT), m.getDefault(Attribute.SPECIAL_COMBAT));
    assertEquals(m.get(Attribute.SPECIAL_COMBAT_RECOVERY), m.getDefault(Attribute.SPECIAL_COMBAT_RECOVERY));
  }

  protected void check(final Monster m) {
    final Castable x = getCastable();
    final Wizard1 w = new Wizard1();
    final World world = new World(1, 1);
    final EventListener listen = e -> {
      if (e instanceof CellEffectEvent) {
        final CellEffectEvent ce = (CellEffectEvent) e;
        if (ce.getEventType() == CellEffectType.REDRAW_CELL) {
          Assert.assertFalse(getRedraw());
          setRedraw();
        } else if (ce.getEventType() == CellEffectType.SHIELD_GRANTED) {
          Assert.assertFalse(getCast());
          setCast();
        }
      }
    };
    world.getCell(0).push(m);
    if (!(x instanceof Restoration)) {
      world.getCell(0).reinstate();
    }
    world.register(listen);
    try {
      w.set(Attribute.INTELLIGENCE, 42);
      x.cast(world, w, world.getCell(0), null);
      assertEquals(m.getClass(), world.actor(0).getClass());
      checkState(m);
      assertTrue(getRedraw());
      assertTrue(getCast());
    } finally {
      world.deregister(listen);
    }
    x.cast(world, w, world.getCell(0), null);
    x.cast(world, w, null, world.getCell(0));
    x.cast(world, null, world.getCell(0), world.getCell(0));
    x.cast(null, w, world.getCell(0), world.getCell(0));
  }

  public void testCast() {
    check(new Centaur());
  }

  public void testCast2() {
    check(new Hippocrates());
  }

  public void testFilter() {
    final World w = new World(2, 1);
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(1);
    final TargetFilter x = (TargetFilter) getCastable();
    final HashSet<Cell> t = new HashSet<>();
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    final Cell cn = new Cell(23);
    t.add(cn);
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    final Cell c = new Cell(0);
    final Lion l = new Lion();
    l.setOwner(1);
    c.push(l);
    t.add(c);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertTrue(t.contains(c));
    final Cell c2 = new Cell(1);
    final Lion l2 = new Lion();
    l2.setOwner(1);
    l2.set(Attribute.LIFE, 5);
    c2.push(l2);
    t.add(c2);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertTrue(t.contains(c2));
    final Cell c3 = new Cell(42);
    wiz.setState(State.ACTIVE);
    wiz.set(Attribute.LIFE, 100);
    c3.push(wiz);
    t.add(c3);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertTrue(t.contains(c2));
  }
}
