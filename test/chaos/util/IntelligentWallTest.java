package chaos.util;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.WizardManager;
import chaos.board.World;
import chaos.common.State;
import chaos.common.inanimate.Pit;
import chaos.common.monster.Horse;
import chaos.common.wizard.Wizard1;
import junit.framework.TestCase;

/**
 * JUnit tests for the corresponding class.
 * @author Sean A. Irvine
 */
public class IntelligentWallTest extends TestCase {

  public void testChoose() {
    final World world = new World(5, 5);
    final WizardManager wm = world.getWizardManager();
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wm.setWizard(1, wiz);
    world.getCell(0).push(wiz);
    final HashSet<Cell> t = new HashSet<>();
    assertEquals(0, IntelligentWall.choose(t, wiz, world).size());
    t.add(world.getCell(23));
    assertEquals(1, IntelligentWall.choose(t, wiz, world).size());
    final Wizard1 w2 = new Wizard1();
    w2.setState(State.ACTIVE);
    wm.setWizard(2, w2);
    world.getCell(4).push(w2);
    t.add(world.getCell(3));
    t.add(world.getCell(8));
    assertEquals(2, IntelligentWall.choose(t, wiz, world).size());
    assertTrue(t.contains(world.getCell(3)));
    assertTrue(t.contains(world.getCell(8)));
    final Horse h = new Horse();
    h.setMount(w2);
    world.getCell(4).pop();
    world.getCell(4).push(h);
    t.add(world.getCell(23));
    assertEquals(2, IntelligentWall.choose(t, wiz, world).size());
    assertTrue(t.contains(world.getCell(3)));
    assertTrue(t.contains(world.getCell(8)));
    final Pit p = new Pit();
    p.setOwner(wiz.getOwner());
    world.getCell(14).push(p);
    t.add(world.getCell(19));
    t.add(world.getCell(20));
    assertEquals(1, IntelligentWall.choose(t, wiz, world).size());
    assertTrue(t.contains(world.getCell(8)));
    world.getCell(4).pop();
    t.add(world.getCell(19));
    t.add(world.getCell(20));
    assertEquals(1, IntelligentWall.choose(t, wiz, world).size());
    assertTrue(t.contains(world.getCell(19)));
    p.setOwner(w2.getOwner());
    t.add(world.getCell(8));
    t.add(world.getCell(20));
    assertEquals(2, IntelligentWall.choose(t, wiz, world).size());
    assertTrue(t.contains(world.getCell(8)));
    assertTrue(t.contains(world.getCell(19)));
    world.getCell(14).pop();
    t.add(world.getCell(1));
    t.add(world.getCell(4));
    assertEquals(2, IntelligentWall.choose(t, wiz, world).size());
    assertTrue(t.contains(world.getCell(4)));
    assertTrue(t.contains(world.getCell(19)));
    world.getCell(0).pop();
    t.add(world.getCell(8));
    t.add(world.getCell(1));
    assertEquals(4, IntelligentWall.choose(t, wiz, world).size());
    assertTrue(t.contains(world.getCell(4)));
    assertTrue(t.contains(world.getCell(8)));
    assertTrue(t.contains(world.getCell(19)));
    assertTrue(t.contains(world.getCell(1)));
    world.getCell(24).push(p);
    world.getCell(23).push(p);
    world.getCell(19).push(p);
    world.getCell(22).push(p);
    world.getCell(20).push(p);
    t.add(world.getCell(18));
    t.add(world.getCell(16));
    assertEquals(1, IntelligentWall.choose(t, wiz, world).size());
    assertTrue(t.contains(world.getCell(18)));
  }
}
