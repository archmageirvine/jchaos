package chaos.common.spell;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Castable;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class FreezeTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new Freeze();
  }

  public void testCast() {
    final Freeze a = new Freeze();
    assertEquals(Castable.MAX_CAST_RANGE, a.getCastRange());
    assertEquals(Castable.CAST_LIVING | Castable.CAST_INANIMATE | Castable.CAST_GROWTH, a.getCastFlags());
    final World world = new World(20, 2);
    final Wizard w = new Wizard1();
    w.setOwner(1);
    world.getCell(0).push(w);
    final Wizard l = new Wizard1();
    l.setOwner(2);
    world.getWizardManager().setWizard(1, w);
    world.getWizardManager().setWizard(2, l);
    world.getCell(5).push(l);
    a.cast(world, w, world.getCell(5), world.getCell(0));
    assertEquals(1, l.get(PowerUps.FROZEN));
  }

  public void testFilter() {
    final World w = new World(2, 1);
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(1);
    w.getWizardManager().setWizard(1, wiz);
    wiz.setOwner(1);
    w.getWizardManager().setWizard(2, new Wizard1());
    w.getWizardManager().setWizard(3, new Wizard1());
    final Freeze x = new Freeze();
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
    l2.setOwner(3);
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
    w.getWizardManager().getWizard(2).setMass(1000);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertTrue(t.contains(c));
  }
}
