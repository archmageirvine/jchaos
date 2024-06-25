package chaos.util;

import chaos.board.World;
import chaos.common.Monster;
import chaos.common.State;
import chaos.common.inanimate.MagicWood;
import chaos.common.monster.CatLord;
import chaos.common.monster.Lion;
import chaos.common.monster.WoodElf;
import chaos.common.wizard.Wizard1;
import junit.framework.TestCase;

/**
 * JUnit tests for the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class MovementUtilsTest extends TestCase {

  public void testClearMovement() {
    final World w = new World(3, 3);
    w.getCell(0).push(new Lion());
    w.getCell(1).push(new WoodElf());
    final MagicWood mw = new MagicWood();
    final WoodElf we = new WoodElf();
    we.setOwner(1);
    mw.setMount(we);
    w.getCell(2).push(mw);
    MovementUtils.clearMovement(w, Lion.class, 2);
    assertEquals(2, w.actor(0).getOwner());
    assertFalse(w.actor(0).isMoved());
    assertEquals(1, ((Monster) w.actor(1)).getShotsMade());
    assertTrue(w.actor(1).isMoved());
    assertEquals(1, we.getShotsMade());
    assertTrue(we.isMoved());
    assertEquals(1, we.getOwner());
  }

  public void testClearMovementOwner1() {
    final World w = new World(3, 3);
    w.getCell(0).push(new Lion());
    w.getCell(1).push(new WoodElf());
    final MagicWood mw = new MagicWood();
    final WoodElf we = new WoodElf();
    we.setOwner(1);
    mw.setMount(we);
    w.getCell(2).push(mw);
    MovementUtils.clearMovement(w, Lion.class, 1);
    assertEquals(1, w.actor(0).getOwner());
    assertFalse(w.actor(0).isMoved());
    assertEquals(1, ((Monster) w.actor(1)).getShotsMade());
    assertTrue(w.actor(1).isMoved());
    assertEquals(1, we.getShotsMade());
    assertTrue(we.isMoved());
    assertEquals(1, we.getOwner());
  }

  public void testClearMovementNoOwnerChange() {
    final World w = new World(3, 3);
    w.getCell(0).push(new Lion());
    w.getCell(1).push(new WoodElf());
    final MagicWood mw = new MagicWood();
    final WoodElf we = new WoodElf();
    we.setOwner(1);
    mw.setMount(we);
    w.getCell(2).push(mw);
    MovementUtils.clearMovement(w, Lion.class, -1);
    assertEquals(-1, w.actor(0).getOwner());
    assertFalse(w.actor(0).isMoved());
    assertEquals(1, ((Monster) w.actor(1)).getShotsMade());
    assertTrue(w.actor(1).isMoved());
    assertEquals(1, we.getShotsMade());
    assertTrue(we.isMoved());
    assertEquals(1, we.getOwner());
  }

  public void testClearMovementPlayer() {
    final World w = new World(3, 3);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(1);
    wiz.setShotsMade(1);
    wiz.setMoved(true);
    wiz.setEngaged(true);
    wiz.setShotsMade(2);
    final Lion l = new Lion();
    l.setOwner(1);
    l.setMoved(true);
    w.getCell(0).push(l);
    final WoodElf we = new WoodElf();
    we.setOwner(1);
    we.setShotsMade(1);
    w.getCell(3).push(we);
    final WoodElf we2 = new WoodElf();
    we2.setOwner(2);
    we2.setShotsMade(1);
    w.getCell(2).push(we2);
    MovementUtils.clearMovement(w, wiz, false);
    assertEquals(0, wiz.getShotsMade());
    assertFalse(wiz.isMoved());
    assertFalse(wiz.isEngaged());
    assertEquals(0, we.getShotsMade());
    assertFalse(we.isMoved());
    assertEquals(1, we2.getShotsMade());
    assertFalse(we2.isMoved());
    assertFalse(l.isMoved());
  }

  public void testClearMovementPlayerCat() {
    final World w = new World(3, 3);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(1);
    wiz.setShotsMade(1);
    wiz.setMoved(true);
    wiz.setEngaged(true);
    w.getCell(1).push(wiz);
    final Lion l = new Lion();
    l.setOwner(1);
    l.setMoved(true);
    w.getCell(2).push(l);
    final WoodElf we = new WoodElf();
    we.setOwner(1);
    we.setShotsMade(1);
    we.setMoved(true);
    w.getCell(3).push(we);
    final WoodElf we2 = new WoodElf();
    we2.setOwner(2);
    we2.setShotsMade(1);
    w.getCell(0).push(we2);
    MovementUtils.clearMovement(w, wiz, true);
    assertEquals(0, wiz.getShotsMade());
    assertFalse(wiz.isMoved());
    assertFalse(wiz.isEngaged());
    assertEquals(0, we.getShotsMade());
    assertFalse(we.isMoved());
    assertEquals(1, we2.getShotsMade());
    assertFalse(we2.isMoved());
    assertTrue(l.isMoved());
  }

  public void testMarkCatsAsMoved() {
    final World w = new World(3, 3);
    final Lion l = new Lion();
    l.setOwner(1);
    w.getCell(1).push(l);
    assertFalse(MovementUtils.markAllCatsAsMoved(w));
    assertFalse(l.isMoved());
  }

  public void testMarkCatsAsMovedWithCatLord() {
    final World w = new World(3, 3);
    final Lion l = new Lion();
    l.setOwner(1);
    w.getCell(0).push(l);
    final CatLord cl = new CatLord();
    cl.setOwner(2);
    w.getCell(2).push(cl);
    assertTrue(MovementUtils.markAllCatsAsMoved(w));
    assertTrue(l.isMoved());
  }
}
