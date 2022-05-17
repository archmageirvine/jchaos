package chaos.engine;

import chaos.board.MoveMaster;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.State;
import chaos.common.inanimate.MagicWood;
import chaos.common.inanimate.WeakWall;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard;
import irvine.math.r.Constants;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class HumanMoveHelperTest extends TestCase {

  private Actor ww() {
    final WeakWall ww = new WeakWall();
    ww.setOwner(1);
    return ww;
  }

  public void testWalking() {
    final World world = new World(4, 4);
    final Lion lion = new Lion();
    lion.setOwner(1);
    world.getCell(0).push(lion);
    final HumanMoveHelper hmh = new HumanMoveHelper(world);
    hmh.setTargets(lion, 0, world.getWizardManager().getWizard(1), new MoveMaster(world));
    assertEquals(0.0, hmh.distance(0), 1e-4);
    assertEquals(0, hmh.prior(0));
    assertEquals(1.0, hmh.distance(1), 1e-4);
    assertEquals(0, hmh.prior(1));
    assertEquals(1 + Constants.SQRT2, hmh.distance(6), 1e-4);
    assertEquals(1, hmh.prior(6));
    assertTrue(hmh.distance(3) > 0);
    assertTrue(hmh.distance(14) > 0);
    assertTrue(hmh.distance(15) > 0);
    // Now put some walls in the way
    world.getCell(2).push(ww());
    world.getCell(6).push(ww());
    world.getCell(10).push(ww());
    hmh.setTargets(lion, 0, world.getWizardManager().getWizard(1), new MoveMaster(world));
    assertEquals(-1, hmh.distance(6), 1e-4);
    assertFalse(hmh.distance(3) > 0);
    assertTrue(hmh.distance(14) > 0);
    assertFalse(hmh.distance(15) > 0);
  }

  public void testMount() {
    final World world = new World(3, 2);
    final Wizard wizard = world.getWizardManager().getWizard(1);
    wizard.setState(State.ACTIVE);
    wizard.set(Attribute.MOVEMENT, 10);
    world.getCell(0).push(wizard);
    final HumanMoveHelper hmh = new HumanMoveHelper(world);
    hmh.setTargets(wizard, 0, wizard, new MoveMaster(world));
    assertEquals(0.0, hmh.distance(0), 1e-4);
    assertEquals(2.0, hmh.distance(2), 1e-4);
    assertEquals(1, hmh.prior(2));
    // Now put up a tree which will increase distance to 3
    final MagicWood mw = new MagicWood();
    mw.setOwner(1);
    world.getCell(1).push(mw);
    hmh.setTargets(wizard, 0, world.getWizardManager().getWizard(1), new MoveMaster(world));
    assertEquals(2 * Constants.SQRT2, hmh.distance(2), 1e-4);
    assertEquals(4, hmh.prior(2));
    assertEquals(1.0, hmh.distance(1), 1e-4);
  }
}
