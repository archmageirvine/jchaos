package chaos.common.spell;

import java.util.HashSet;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Monster;
import chaos.common.State;
import chaos.common.inanimate.WeakWall;
import chaos.common.monster.Horse;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.EventListener;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class IceBreathTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new IceBreath();
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
    final Castable x = new IceBreath();
    assertEquals(Castable.CAST_LIVING | Castable.CAST_DEAD | Castable.CAST_INANIMATE | Castable.CAST_GROWTH | Castable.CAST_EMPTY, x.getCastFlags());
    assertEquals(12, x.getCastRange());
    final Wizard1 w = new Wizard1();
    final World world = new World(2, 1);
    final EventListener listen = e -> {
      if (e instanceof CellEffectEvent && ((CellEffectEvent) e).getEventType() == CellEffectType.REDRAW_CELL) {
        setRedraw();
      } else if (e instanceof WeaponEffectEvent && ((WeaponEffectEvent) e).getEventType() == WeaponEffectType.ICE_BEAM) {
        assertFalse(getCast());
        setCast();
      }
    };
    world.getCell(0).push(w);
    world.register(listen);
    Monster h = new Horse();
    world.getCell(1).push(h);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(0, h.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(0, h.get(Attribute.LIFE));
    assertEquals(State.DEAD, h.getState());
    assertTrue(getRedraw());
    assertTrue(getCast());
    assertEquals(h, world.actor(1));
    mCast = false;
    mRedraw = false;
    assertEquals(13, w.getScore());
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(18, w.getScore());
    assertEquals(null, world.actor(1));
    mCast = false;
    mRedraw = false;
    h = new Horse();
    h.set(Attribute.LIFE, 30);
    world.getCell(1).pop();
    world.getCell(1).push(h);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(0, h.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(0, h.get(Attribute.LIFE));
    assertEquals(State.DEAD, h.getState());
    assertTrue(getRedraw());
    assertTrue(getCast());
    assertEquals(h, world.actor(1));
    mCast = false;
    mRedraw = false;
    h = new Horse();
    h.set(Attribute.MAGICAL_RESISTANCE, 31);
    world.getCell(1).pop();
    world.getCell(1).push(h);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(1, h.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(h.getDefault(Attribute.LIFE), h.get(Attribute.LIFE));
    assertEquals(State.ACTIVE, h.getState());
    assertTrue(getRedraw());
    assertTrue(getCast());
    assertEquals(h, world.actor(1));
    mCast = false;
    mRedraw = false;
    final Actor i = new WeakWall();
    world.getCell(1).pop();
    world.getCell(1).push(i);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(i.getDefault(Attribute.LIFE) - 60, i.get(Attribute.LIFE));
    assertTrue(getRedraw());
    assertTrue(getCast());
    assertEquals(i, world.actor(1));
    world.deregister(listen);
    i.set(Attribute.LIFE, 1);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertNull(world.actor(1));
    assertEquals(1, w.getBonusCount());
    assertEquals(1, w.getBonusSelect());
    x.cast(world, w, world.getCell(0), null);
    x.cast(world, w, null, world.getCell(0));
    x.cast(world, null, world.getCell(0), world.getCell(0));
    x.cast(null, w, world.getCell(0), world.getCell(0));
  }

  public void testFilter() {
    final World world = new World(4, 4);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(1);
    world.getCell(0).push(wiz);
    final Horse h = new Horse();
    h.setOwner(2);
    world.getCell(10).push(h);
    final Set<Cell> targets = new HashSet<>(world.size());
    for (int k = 0; k < world.size(); ++k) {
      targets.add(world.getCell(k));
    }
    new IceBreath().filter(targets, wiz, world);
    assertEquals(8, targets.size());
    new IceBreath().filter(targets, wiz, world);
    assertEquals(8, targets.size());
    final Horse u = new Horse();
    u.setOwner(1);
    world.getCell(5).push(u);
    new IceBreath().filter(targets, wiz, world);
    assertEquals(5, targets.size());
  }
}
