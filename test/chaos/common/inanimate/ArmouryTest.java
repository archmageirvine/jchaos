package chaos.common.inanimate;

import chaos.board.MoveMaster;
import chaos.board.World;
import chaos.common.AbstractMonsterTest;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.monster.AerialServant;
import chaos.util.CombatUtils;

/**
 * Tests this inanimate.
 *
 * @author Sean A. Irvine
 */
public class ArmouryTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Armoury();
  }

  @Override
  public void testReincarnation() {
    assertEquals(Castable.CAST_EMPTY | Castable.CAST_LOS | Castable.CAST_DEAD, getCastable().getCastFlags());
    assertEquals(null, new Armoury().reincarnation());
  }

  public void testWorks() {
    final World world = new World(1, 10);
    final AerialServant as = new AerialServant();
    as.setOwner(1);
    world.getCell(0).push(as);
    final Armoury a = new Armoury();
    a.setOwner(1);
    world.getCell(6).push(a);
    CombatUtils.performSpecialCombat(world, new MoveMaster(world));
    assertEquals(1, as.get(Attribute.COMBAT));
  }
}
