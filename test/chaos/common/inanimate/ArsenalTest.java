package chaos.common.inanimate;

import chaos.Chaos;
import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractMonsterTest;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.monster.Aesculapius;
import chaos.common.monster.WoodElf;
import chaos.common.wizard.Wizard1;
import chaos.util.CombatUtils;

/**
 * Tests this inanimate.
 * @author Sean A. Irvine
 */
public class ArsenalTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Arsenal();
  }

  @Override
  public void testReincarnation() {
    assertEquals(Castable.CAST_EMPTY | Castable.CAST_LOS | Castable.CAST_DEAD, getCastable().getCastFlags());
    assertEquals(null, new Arsenal().reincarnation());
  }

  public void testRC() {
    final Arsenal v = new Arsenal();
    v.cast(null, null, null, null);
    assertEquals(Attribute.RANGED_COMBAT, v.getSpecialCombatApply());
    final World w = Chaos.getChaos().getWorld();
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(2);
    wiz.setState(State.ACTIVE);
    w.getCell(0).push(wiz);
    final Cell c = w.getCell(1);
    v.cast(w, wiz, c, w.getCell(0));
    assertEquals(2, v.getOwner());
    assertEquals(v, c.peek());
    final WoodElf elf = new WoodElf();
    elf.setOwner(2);
    w.getCell(2).push(elf);
    final WoodElf deadElf = new WoodElf();
    deadElf.setOwner(2);
    deadElf.setState(State.DEAD);
    deadElf.set(Attribute.LIFE, 0);
    w.getCell(w.width() + 1).push(deadElf);
    CombatUtils.performSpecialCombat(w, Chaos.getChaos().getMoveMaster());
    assertEquals(1 + elf.getDefault(Attribute.RANGE), elf.get(Attribute.RANGE));
    assertEquals(1 + elf.getDefault(Attribute.RANGED_COMBAT), elf.get(Attribute.RANGED_COMBAT));
    assertEquals(wiz.getDefault(Attribute.RANGE), wiz.get(Attribute.RANGE));
    assertEquals(deadElf.getDefault(Attribute.RANGE), deadElf.get(Attribute.RANGE));
    wiz.set(Attribute.RANGE, 1);
    CombatUtils.performSpecialCombat(w, Chaos.getChaos().getMoveMaster());
    assertEquals(2 + elf.getDefault(Attribute.RANGE), elf.get(Attribute.RANGE));
    assertEquals(2 + elf.getDefault(Attribute.RANGED_COMBAT), elf.get(Attribute.RANGED_COMBAT));
    assertEquals(2, wiz.get(Attribute.RANGE));
    assertEquals(deadElf.getDefault(Attribute.RANGE), deadElf.get(Attribute.RANGE));
    elf.set(Attribute.RANGE, Attribute.RANGE.max());
    elf.set(Attribute.RANGED_COMBAT, Attribute.RANGED_COMBAT.max());
    CombatUtils.performSpecialCombat(w, Chaos.getChaos().getMoveMaster());
    assertEquals(Attribute.RANGE.max(), elf.get(Attribute.RANGE));
    assertEquals(Attribute.RANGED_COMBAT.max(), elf.get(Attribute.RANGED_COMBAT));
  }

  public void testRC2() {
    final Arsenal v = new Arsenal();
    v.cast(null, null, null, null);
    assertEquals(Attribute.RANGED_COMBAT, v.getSpecialCombatApply());
    final World w = Chaos.getChaos().getWorld();
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(2);
    wiz.setState(State.ACTIVE);
    w.getCell(0).push(wiz);
    final Cell c = w.getCell(1);
    v.cast(w, wiz, c, w.getCell(0));
    assertEquals(2, v.getOwner());
    assertEquals(v, c.peek());
    final Aesculapius aesculapius = new Aesculapius();
    aesculapius.setOwner(2);
    w.getCell(2).push(aesculapius);
    CombatUtils.performSpecialCombat(w, Chaos.getChaos().getMoveMaster());
    assertEquals(1 + aesculapius.getDefault(Attribute.RANGE), aesculapius.get(Attribute.RANGE));
    assertEquals(-1 + aesculapius.getDefault(Attribute.RANGED_COMBAT), aesculapius.get(Attribute.RANGED_COMBAT));
    assertEquals(wiz.getDefault(Attribute.RANGE), wiz.get(Attribute.RANGE));
    aesculapius.set(Attribute.RANGE, Attribute.RANGE.max());
    aesculapius.set(Attribute.RANGED_COMBAT, Attribute.RANGED_COMBAT.min());
    CombatUtils.performSpecialCombat(w, Chaos.getChaos().getMoveMaster());
    assertEquals(Attribute.RANGE.max(), aesculapius.get(Attribute.RANGE));
    assertEquals(Attribute.RANGED_COMBAT.min(), aesculapius.get(Attribute.RANGED_COMBAT));
  }
}
