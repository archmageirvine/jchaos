package chaos.common.spell;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Castable;
import chaos.common.Realm;
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
public class ConvertTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new Convert();
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
    final Convert x = new Convert();
    assertEquals(Castable.CAST_LIVING | Castable.CAST_INANIMATE | Castable.CAST_GROWTH | Castable.CAST_LOS, x.getCastFlags());
    assertEquals(16, x.getCastRange());
    assertEquals(2, x.getMultiplicity());
    final Wizard1 w = new Wizard1();
    w.setRealm(Realm.ETHERIC);
    final World world = new World(1, 1);
    final EventListener listen = e -> {
      if (e instanceof CellEffectEvent) {
        final CellEffectEvent ce = (CellEffectEvent) e;
        if (ce.getEventType() == CellEffectType.REDRAW_CELL) {
          assertFalse(getRedraw());
          setRedraw();
        } else if (ce.getEventType() == CellEffectType.CHANGE_REALM) {
          assertFalse(getCast());
          setCast();
        }
      }
    };
    world.getCell(0).push(w);
    world.register(listen);
    x.cast(world, w, world.getCell(0), world.getCell(0));
    assertEquals(Realm.MATERIAL, w.getRealm());
    assertTrue(getRedraw());
    assertTrue(getCast());
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
    final Convert x = new Convert();
    final HashSet<Cell> t = new HashSet<>();
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    final Cell cn = new Cell(23);
    t.add(cn);
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    final Cell c = new Cell(0);
    final Skeleton l = new Skeleton();
    l.setOwner(2);
    c.push(l);
    t.add(c);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertTrue(t.contains(c));
    final Cell c2 = new Cell(1);
    final Skeleton l2 = new Skeleton();
    l2.setOwner(2);
    c2.push(l2);
    t.add(c2);
    x.filter(t, wiz, w);
    assertEquals(2, t.size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
    l2.setRealm(Realm.MATERIAL);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertTrue(t.contains(c));
  }
}
