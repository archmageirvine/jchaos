package chaos.common.free;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.monster.Lion;
import chaos.common.monster.WoodElf;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectType;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class SharpshootingTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Sharpshooting();
  }

  public void test1() {
    final Sharpshooting a = new Sharpshooting();
    assertEquals(Castable.CAST_SINGLE, a.getCastFlags());
    final World world = new World(1, 5);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    w.increment(Attribute.RANGE, 1);
    w.setState(State.ACTIVE);
    final Cell cc = world.getCell(1);
    cc.push(w);
    final WoodElf l = new WoodElf();
    l.setOwner(3);
    world.getCell(0).push(l);
    final Lion ll = new Lion();
    ll.setOwner(3);
    world.getCell(2).push(ll);
    final Wizard1 ww = new Wizard1();
    ww.setOwner(3);
    ww.setState(State.ACTIVE);
    world.getCell(3).push(ww);
    final int ldv1 = l.getDefault(Attribute.RANGE);
    final int wdv1 = 1;
    final int ldv2 = l.getDefault(Attribute.RANGED_COMBAT);
    final int wdv2 = w.getDefault(Attribute.RANGED_COMBAT);
    for (int i = 1; i <= 101; ++i) {
      castAndListenCheck(a, world, w, 1, CellEffectType.SHIELD_GRANTED);
      assertEquals(Math.min(Attribute.RANGE.max(), ldv1 + i), l.get(Attribute.RANGE));
      assertEquals(Math.min(Attribute.RANGE.max(), wdv1 + i), w.get(Attribute.RANGE));
      assertEquals(Math.min(Attribute.RANGED_COMBAT.max(), ldv2 + 2 * i), l.get(Attribute.RANGED_COMBAT));
      assertEquals(Math.min(Attribute.RANGED_COMBAT.max(), wdv2 + 2 * i), w.get(Attribute.RANGED_COMBAT));
    }
    assertEquals(0, ll.get(Attribute.RANGE));
    assertEquals(0, ll.get(Attribute.RANGED_COMBAT));
    assertEquals(0, ww.get(Attribute.RANGE));
    assertEquals(0, ww.get(Attribute.RANGED_COMBAT));
    a.cast(null, null, null);
    a.cast(world, null, null);
    a.cast(null, w, null);
    a.cast(world, w, null);
  }
}
