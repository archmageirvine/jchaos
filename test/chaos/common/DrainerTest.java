package chaos.common;

import java.util.HashSet;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.inanimate.Pit;
import chaos.common.monster.Hydra;
import chaos.common.monster.Lion;
import chaos.common.monster.Orc;
import chaos.common.monster.StoneGolem;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectType;
import chaos.util.EventListener;
import chaos.util.PolycellEffectEvent;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class DrainerTest extends TestCase {


  private boolean mCast = false;

  private void setCast() {
    mCast = true;
  }

  private boolean getCast() {
    return mCast;
  }

  private boolean mSpunge = false;

  private void setSpunge() {
    mSpunge = true;
  }

  private boolean getSpunge() {
    return mSpunge;
  }

  private static class MyDrainer extends Drainer {
    @Override
    public int getCastFlags() {
      return 0;
    }

    @Override
    public int getCastRange() {
      return 0;
    }

    @Override
    public Set<Cell> getAffectedCells(final World world, final Cell cell) {
      final HashSet<Cell> hs = new HashSet<>();
      hs.add(world.getCell(0));
      hs.add(world.getCell(1));
      hs.add(world.getCell(2));
      hs.add(world.getCell(3));
      hs.add(world.getCell(4));
      hs.add(world.getCell(5));
      return hs;
    }

    @Override
    public CellEffectType getEffectType() {
      return CellEffectType.SPUNGER;
    }

    @Override
    public int getDamage() {
      return 85;
    }
  }

  public void test() {
    final Drainer f = new MyDrainer();
    final EventListener listen = e -> {
      if (e instanceof PolycellEffectEvent) {
        final CellEffectType type = ((PolycellEffectEvent) e).getEventType();
        if (type != CellEffectType.REDRAW_CELL) {
          assertEquals(CellEffectType.SPUNGER, type);
          setSpunge();
        } else {
          setCast();
        }
      }
    };
    f.cast(null, null, null, null);
    final World w = new World(3, 3);
    final Lion lion = new Lion();
    lion.set(Attribute.MAGICAL_RESISTANCE, 100);
    w.getCell(0).push(lion);
    final Hydra h = new Hydra();
    h.set(Attribute.LIFE, 0);
    h.set(Attribute.AGILITY, 0);
    h.setState(State.DEAD);
    w.getCell(1).push(h);
    final Orc o = new Orc();
    o.set(Attribute.MAGICAL_RESISTANCE, 5);
    w.getCell(1).push(o);
    final Pit pit = new Pit();
    pit.set(Attribute.MAGICAL_RESISTANCE, 55);
    w.getCell(3).push(pit);
    final Orc o2 = new Orc();
    o2.set(Attribute.MAGICAL_RESISTANCE, 5);
    w.getCell(3).push(o2);
    final StoneGolem o3 = new StoneGolem();
    o3.set(Attribute.MAGICAL_RESISTANCE, 5);
    w.getCell(4).push(o3);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.set(Attribute.MAGICAL_RESISTANCE, 0);
    w.getCell(5).push(wiz);
    w.register(listen);
    f.cast(w, wiz, w.getCell(0), w.getCell(0));
    assertEquals(15, lion.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(h, w.actor(1));
    assertEquals(State.ACTIVE, h.getState());
    assertEquals(h.getDefault(Attribute.AGILITY), h.get(Attribute.AGILITY));
    assertEquals(h.getDefault(Attribute.AGILITY), h.get(Attribute.AGILITY));
    assertEquals(pit, w.actor(3));
    assertEquals(pit.getDefault(Attribute.MAGICAL_RESISTANCE), pit.get(Attribute.MAGICAL_RESISTANCE));
    assertNull(w.actor(4));
    assertEquals(wiz, w.actor(5));
    assertTrue(wiz.getBonusCount() >= 6);
    assertTrue(wiz.getBonusSelect() >= 2);
    assertEquals(0, wiz.get(Attribute.MAGICAL_RESISTANCE));
    assertTrue(getCast());
    assertTrue(getSpunge());
    f.cast(w, null, null, w.getCell(0));
    assertEquals(15, lion.get(Attribute.MAGICAL_RESISTANCE));
  }

  public void testFilter() {
    final World w = new World(2, 1);
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(1);
    final Drainer x = new MyDrainer();
    final HashSet<Cell> t = new HashSet<>();
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    final Cell cn = new Cell(23);
    t.add(cn);
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    final Cell c = new Cell(0);
    final Lion l = new Lion();
    l.setOwner(2);
    c.push(l);
    t.add(c);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertTrue(t.contains(c));
    final Cell c2 = new Cell(1);
    final Lion l2 = new Lion();
    l2.setOwner(2);
    l2.set(PowerUps.CLOAKED, 1);
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
    l.set(Attribute.COMBAT, 0);
    l.set(Attribute.MAGICAL_RESISTANCE, 5);
    l2.set(Attribute.MAGICAL_RESISTANCE, 5);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertTrue(t.contains(c2));
    l2.set(Attribute.COMBAT, 0);
    t.add(c);
    x.filter(t, wiz, w);
    assertEquals(2, t.size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
    l.set(Attribute.LIFE, 1);
    l2.set(Attribute.LIFE, 1);
    x.filter(t, wiz, w);
    assertEquals(2, t.size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
  }
}
