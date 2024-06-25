package chaos.engine;

import java.util.Set;

import chaos.board.MoveMaster;
import chaos.board.Reachable;
import chaos.board.World;
import chaos.common.Castable;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.inanimate.MagicWood;
import chaos.common.inanimate.Rock;
import chaos.common.monster.Bat;
import chaos.common.monster.Lion;
import chaos.common.spell.Lightning;
import chaos.common.wizard.Wizard1;
import irvine.math.r.DoubleUtils;
import junit.framework.TestCase;

/**
 * Tests for the corresponding class.
 * @author Sean A. Irvine
 */
public class HumanEngineHelperTest extends TestCase {

  public void testNF() {
    assertNotNull(DoubleUtils.NF2);
    assertEquals("1.00", DoubleUtils.NF2.format(1));
    assertEquals("1.41", DoubleUtils.NF2.format(1.411));
    assertEquals("1.41", DoubleUtils.NF2.format(1.409));
  }

  public void testSetMoved() {
    HumanEngineHelper.setMoved(null);
    HumanEngineHelper.setMoved(new Rock());
    final Lion l = new Lion();
    HumanEngineHelper.setMoved(l);
    assertTrue(l.isMoved());
  }

  public void testSetShot() {
    HumanEngineHelper.setShot(null);
    HumanEngineHelper.setShot(new Rock());
    final Lion l = new Lion();
    HumanEngineHelper.setShot(l);
    assertEquals(1, l.getShotsMade());
  }

  public void testGetCastRange() {
    final Wizard1 w = new Wizard1();
    assertEquals(1, HumanEngineHelper.getCastRange(new Lion(), w));
    assertEquals(8, HumanEngineHelper.getCastRange(new Lightning(), w));
    w.increment(PowerUps.WAND);
    assertEquals(2, HumanEngineHelper.getCastRange(new Lion(), w));
    assertEquals(9, HumanEngineHelper.getCastRange(new Lightning(), w));
    w.increment(PowerUps.DEPTH);
    assertEquals(2, HumanEngineHelper.getCastRange(new Lion(), w));
    assertEquals(Castable.MAX_CAST_RANGE, HumanEngineHelper.getCastRange(new Lightning(), w));
  }

  public void testPossiblePlaceToMove() {
    final World world = new World(2, 2);
    final Reachable reachable = new Reachable(world);
    final Wizard1 wiz1 = new Wizard1();
    wiz1.setState(State.ACTIVE);
    world.getWizardManager().setWizard(1, wiz1);
    world.getCell(0).push(wiz1);
    final MagicWood mw = new MagicWood();
    mw.setOwner(wiz1.getOwner());
    world.getCell(1).push(mw);
    final MoveMaster mm = new MoveMaster(world);
    assertTrue(mm.isMountable(wiz1, 1));
    final Set<Integer> c = HumanEngineHelper.possiblePlacesToMove(world, reachable, mm, wiz1, 0);
    assertFalse(c.contains(0));
    assertTrue(c.contains(1));
    assertTrue(c.contains(2));
    assertTrue(c.contains(3));
    final Wizard1 wiz2 = new Wizard1();
    wiz2.setState(State.ACTIVE);
    wiz2.setOwner(2);
    world.getWizardManager().setWizard(2, wiz2);
    mw.setMount(wiz2);
    final Set<Integer> c2 = HumanEngineHelper.possiblePlacesToMove(world, reachable, mm, wiz1, 0);
    assertFalse(c2.contains(0));
    assertFalse(c2.contains(1));
    assertTrue(c2.contains(2));
    assertTrue(c2.contains(3));
    final Bat bat = new Bat();
    bat.setOwner(1);
    world.getCell(0).push(bat);
    final Set<Integer> c3 = HumanEngineHelper.possiblePlacesToMove(world, reachable, mm, wiz1, 0);
    assertFalse(c3.contains(0));
    assertFalse(c3.contains(1));
    assertTrue(c3.contains(2));
    assertTrue(c3.contains(3));
  }
}
