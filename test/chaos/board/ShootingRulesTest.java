package chaos.board;

import junit.framework.TestCase;
import chaos.common.Attribute;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.inanimate.MagicCastle;
import chaos.common.inanimate.StandardWall;
import chaos.common.monster.Iridium;
import chaos.common.monster.Skeleton;
import chaos.common.monster.WoodElf;
import chaos.common.wizard.Wizard1;

/**
 * Test the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class ShootingRulesTest extends TestCase {

  public void testIsShootable() {
    assertFalse(ShootingRules.isShootable(null, null));
    assertFalse(ShootingRules.isShootable(null, new StandardWall()));
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(1);
    final WoodElf elf = new WoodElf();
    elf.setOwner(1);
    assertTrue(ShootingRules.isShootable(wiz, elf));
    elf.set(Attribute.RANGE, 0);
    assertFalse(ShootingRules.isShootable(wiz, elf));
    elf.set(Attribute.RANGE, 10);
    elf.setOwner(2);
    assertFalse(ShootingRules.isShootable(wiz, elf));
    elf.setOwner(1);
    elf.incrementShotsMade();
    assertFalse(ShootingRules.isShootable(wiz, elf));
    elf.resetShotsMade();
    elf.setState(State.DEAD);
    assertFalse(ShootingRules.isShootable(wiz, elf));
  }

  public void testIsShootableConveyance() {
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(1);
    assertFalse(ShootingRules.isShootableConveyance(null, null));
    assertFalse(ShootingRules.isShootableConveyance(wiz, null));
    assertFalse(ShootingRules.isShootableConveyance(wiz, new WoodElf()));
    final MagicCastle c = new MagicCastle();
    assertFalse(ShootingRules.isShootableConveyance(wiz, c));
    wiz.set(Attribute.RANGE, 10);
    wiz.set(Attribute.SHOTS, 1);
    c.setMount(wiz);
    assertTrue(ShootingRules.isShootableConveyance(wiz, c));
  }

  public void testIsShootableWorld() {
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(1);
    final World w = new World(3, 1);
    w.getCell(0).push(wiz);
    w.getCell(1).push(new WoodElf());
    w.getCell(2).push(new WoodElf());
    w.getWizardManager().setWizard(1, wiz);
    final LineOfSight los = new LineOfSight(w);
    assertFalse(ShootingRules.isShootable(0, 1, wiz, w, los));
    assertFalse(ShootingRules.isShootable(0, 2, wiz, w, los));
    wiz.set(Attribute.RANGE, 2);
    assertTrue(ShootingRules.isShootable(0, 1, wiz, w, los));
    assertFalse(ShootingRules.isShootable(0, 2, wiz, w, los));
    wiz.set(PowerUps.ARCHERY, 1);
    assertTrue(ShootingRules.isShootable(0, 1, wiz, w, los));
    assertTrue(ShootingRules.isShootable(0, 2, wiz, w, los));
    w.getCell(1).push(new Skeleton());
    assertFalse(ShootingRules.isShootable(0, 1, wiz, w, los));
    wiz.set(PowerUps.CONFIDENCE, 1);
    assertTrue(ShootingRules.isShootable(0, 1, wiz, w, los));
    wiz.set(PowerUps.CONFIDENCE, 0);
    wiz.set(PowerUps.ATTACK_ANY_REALM, 1);
    assertTrue(ShootingRules.isShootable(0, 1, wiz, w, los));
    final Iridium ir = new Iridium();
    ir.setOwner(1);
    w.getCell(0).push(ir);
    assertTrue(ShootingRules.isShootable(0, 1, ir, w, los));
    w.getCell(1).pop();
    w.getCell(1).pop();
    assertTrue(ShootingRules.isShootable(0, 1, ir, w, los));
  }
}
