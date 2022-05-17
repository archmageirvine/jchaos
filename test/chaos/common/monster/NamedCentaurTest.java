package chaos.common.monster;

import chaos.board.CastMaster;
import chaos.board.MoveMaster;
import chaos.board.World;
import chaos.common.AbstractMonsterTest;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.wizard.Wizard1;
import chaos.engine.AiEngine;

/**
 * Tests this monster.
 *
 * @author Sean A. Irvine
 */
public class NamedCentaurTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new NamedCentaur();
  }

  public void testNamePreservation() {
    final World world = new World(2, 1);
    final NamedCentaur nc = new NamedCentaur();
    final Wizard1 wiz = new Wizard1() {
      @Override
      public Castable getCastable() {
        return nc;
      }
    };
    wiz.setState(State.ACTIVE);
    wiz.setPlayerEngine(new AiEngine(world, new MoveMaster(world), new CastMaster(world)));
    world.getCell(0).push(wiz);
    wiz.doCasting(world.getCell(0));
    assertTrue(world.actor(1) instanceof NamedCentaur);
    assertEquals(nc.getPersonalName(), ((NamedCentaur) world.actor(1)).getPersonalName());
  }
}
