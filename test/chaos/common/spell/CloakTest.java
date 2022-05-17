package chaos.common.spell;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Castable;
import chaos.common.PowerUps;
import chaos.common.Realm;
import chaos.common.State;
import chaos.common.monster.Lion;
import chaos.common.monster.Skeleton;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.EventListener;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class CloakTest extends AbstractCastableTest {


  @Override
  public Castable getCastable() {
    return new Cloak();
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
    final Castable x = new Cloak();
    assertEquals(Castable.CAST_LIVING, x.getCastFlags());
    assertEquals(Castable.MAX_CAST_RANGE, x.getCastRange());
    final Wizard1 w = new Wizard1();
    final World world = new World(1, 1);
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
    x.cast(world, w, world.getCell(0), world.getCell(0));
    assertTrue(getRedraw());
    assertTrue(getCast());
    assertTrue(w.is(PowerUps.CLOAKED));
    world.deregister(listen);
    x.cast(world, w, world.getCell(0), null);
    x.cast(world, w, null, world.getCell(0));
    x.cast(world, null, world.getCell(0), world.getCell(0));
    x.cast(null, w, world.getCell(0), world.getCell(0));
  }

  public void testCastOnUndead() {
    final Castable x = new Cloak();
    final Skeleton s = new Skeleton();
    final Wizard1 w = new Wizard1();
    final World world = new World(2, 1);
    world.getCell(0).push(s);
    world.getCell(1).push(w);
    x.cast(world, w, world.getCell(0), world.getCell(1));
    assertTrue(s.is(PowerUps.CLOAKED));
    assertEquals(Realm.ETHERIC, s.getRealm());
  }

  public void testCastOnRaised() {
    final Castable x = new Cloak();
    final Lion s = new Lion();
    s.setRealm(Realm.ETHERIC);
    final Wizard1 w = new Wizard1();
    final World world = new World(2, 1);
    world.getCell(0).push(s);
    world.getCell(1).push(w);
    x.cast(world, w, world.getCell(0), world.getCell(1));
    assertTrue(s.is(PowerUps.CLOAKED));
    assertEquals(Realm.ETHERIC, s.getRealm());
  }

  public void testFilter() {
    final World w = new World(2, 1);
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(1);
    final Cloak x = new Cloak();
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
    c2.push(l2);
    t.add(c2);
    x.filter(t, wiz, w);
    assertEquals(2, t.size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
    final Cell c3 = new Cell(42);
    wiz.setState(State.ACTIVE);
    wiz.set(PowerUps.CLOAKED, 1);
    c3.push(wiz);
    t.add(c3);
    x.filter(t, wiz, w);
    assertEquals(2, t.size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
  }
}
