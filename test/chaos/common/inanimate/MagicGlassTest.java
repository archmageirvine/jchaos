package chaos.common.inanimate;

import chaos.Chaos;
import chaos.board.MoveMaster;
import chaos.board.World;
import chaos.common.AbstractMonsterTest;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.dragon.RedDragon;
import chaos.common.monster.Tiger;
import chaos.common.wizard.Wizard1;

/**
 * Tests this inanimate.
 * @author Sean A. Irvine
 */
public class MagicGlassTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new MagicGlass();
  }

  public void testGlassFunctions() {
    final World w = Chaos.getChaos().getWorld();
    try {
      final Wizard1 wiz1 = new Wizard1();
      final Wizard1 wiz2 = new Wizard1();
      w.getWizardManager().setWizard(1, wiz1);
      w.getWizardManager().setWizard(2, wiz2);
      final RedDragon rd = new RedDragon();
      rd.setOwner(1);
      w.getCell(0).push(rd);
      final MagicGlass mg = new MagicGlass();
      mg.setOwner(1);
      w.getCell(1).push(mg);
      final Tiger t = new Tiger();
      t.setOwner(3);
      w.getCell(2).push(t);
      final MoveMaster mm = Chaos.getChaos().getMoveMaster();
      assertTrue(mm.isShootable(w.getWizardManager().getWizard(rd), 0, 2));
      rd.setOwner(2);
      assertFalse(mm.isShootable(w.getWizardManager().getWizard(rd), 0, 2));
    } finally {
      w.getCell(2).pop();
      w.getCell(1).pop();
      w.getCell(0).pop();
    }
  }

  @Override
  public void testReincarnation() {
    final MagicGlass mg = new MagicGlass();
    assertEquals(Castable.CAST_DEAD | Castable.CAST_EMPTY | Castable.CAST_LOS, mg.getCastFlags());
    assertNull(mg.reincarnation());
    assertEquals(Attribute.INTELLIGENCE, mg.getSpecialCombatApply());
  }
}
