package chaos.common.spell;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Castable;
import chaos.common.monster.EarthElemental;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class SubversionTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new Subversion();
  }

  public void testCast() {
    final World world = new World(20, 2);
    final Subversion a = new Subversion();
    assertEquals(11, a.getCastRange());
    assertEquals(Castable.CAST_LIVING | Castable.CAST_LOS | Castable.CAST_GROWTH | Castable.CAST_NOWIZARDCELL, a.getCastFlags());
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    final Lion l = new Lion();
    l.setOwner(1);
    world.getCell(5).push(l);
    for (int i = 0; i < 100; ++i) {
      a.cast(world, w, world.getCell(5), world.getCell(0));
    }
    assertEquals(3, l.getOwner());
  }

  public void testFilter() {
    final World w = new World(2, 1);
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(1);
    final Subversion x = new Subversion();
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
    final EarthElemental l2 = new EarthElemental();
    l2.setOwner(2);
    c2.push(l2);
    t.add(c2);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertTrue(t.contains(c2));
  }
}
