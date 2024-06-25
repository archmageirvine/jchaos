package chaos.common.beam;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.monster.Horse;
import chaos.common.monster.Ogre;
import chaos.common.spell.LightningTest;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.EventListener;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class PlasmaBeamTest extends LightningTest {

  @Override
  public Castable getCastable() {
    return new PlasmaBeam();
  }

  @Override
  public void testFlags() {
    final Castable x = getCastable();
    assertEquals(Castable.CAST_ANY, x.getCastFlags());
    assertEquals(1, x.getCastRange());
  }

  @Override
  protected int getDamage() {
    return 45;
  }

  @Override
  protected EventListener getListener() {
    return e -> {
      if (e instanceof CellEffectEvent) {
        if (((CellEffectEvent) e).getEventType() == CellEffectType.REDRAW_CELL) {
          assertFalse(getRedraw());
          setRedraw();
          setCast();
        }
      }
    };
  }

  public void testFilter1() {
    final World w = new World(5, 5);
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(1);
    w.getCell(0).push(wiz);
    final PlasmaBeam x = new PlasmaBeam();
    boolean saw1 = false;
    boolean saw5 = false;
    boolean saw6 = false;
    for (int k = 0; k < 100; ++k) {
      final HashSet<Cell> t = new HashSet<>();
      x.filter(t, wiz, w);
      assertEquals(1, t.size());
      final int c = t.iterator().next().getCellNumber();
      switch (c) {
        case 1:
          saw1 = true;
          break;
        case 5:
          saw5 = true;
          break;
        case 6:
          saw6 = true;
          break;
        default:
          fail();
          break;
      }
    }
    assertTrue(saw1);
    assertTrue(saw5);
    assertTrue(saw6);
  }

  private static final int[] OGRE_CELL = {6, 7, 8, 13, 18, 17, 16, 11};

  public void testFilter2() {
    final PlasmaBeam x = new PlasmaBeam();
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(1);
    for (int oc : OGRE_CELL) {
      final World w = new World(5, 5);
      w.getCell(12).push(wiz);
      final Ogre o = new Ogre();
      o.setOwner(2);
      w.getCell(oc).push(o);
      final HashSet<Cell> t = new HashSet<>();
      x.filter(t, wiz, w);
      assertEquals(1, t.size());
      assertEquals(oc, t.iterator().next().getCellNumber());
    }
  }

  public void testFilter3() {
    final PlasmaBeam x = new PlasmaBeam();
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(1);
    for (int oc : OGRE_CELL) {
      final World w = new World(5, 5);
      w.getCell(12).push(wiz);
      final Ogre o = new Ogre();
      o.setOwner(1);
      w.getCell(oc).push(o);
      final HashSet<Cell> t = new HashSet<>();
      x.filter(t, wiz, w);
      assertEquals(1, t.size());
      assertTrue(oc != t.iterator().next().getCellNumber());
    }
  }

  public void testFilter4() {
    final PlasmaBeam x = new PlasmaBeam();
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(1);
    final World w = new World(5, 5);
    w.getCell(12).push(wiz);
    final Ogre o = new Ogre();
    o.setOwner(2);
    w.getCell(4).push(o);
    final HashSet<Cell> t = new HashSet<>();
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertEquals(8, t.iterator().next().getCellNumber());
    final Wizard1 ww = new Wizard1();
    ww.setOwner(2);
    w.getCell(24).push(ww);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertEquals(8, t.iterator().next().getCellNumber());
    ww.setState(State.ACTIVE);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertEquals(18, t.iterator().next().getCellNumber());
    final Horse sg = new Horse();
    sg.setOwner(2);
    w.getCell(2).push(sg);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertEquals(18, t.iterator().next().getCellNumber());
    w.getCell(7).push(sg);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertEquals(7, t.iterator().next().getCellNumber());
  }
}
