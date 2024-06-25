package chaos.common.free;

import chaos.board.CastMaster;
import chaos.board.Cell;
import chaos.board.MoveMaster;
import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.CastableList;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.wizard.Wizard1;
import chaos.engine.AiEngine;
import chaos.selector.OrdinarySelector;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class ElfBootsTest extends AbstractFreeIncrementTest {

  @Override
  public Castable getCastable() {
    return new ElfBoots();
  }

  public void testEB() {
    // Test a possible problem reported by Callum
    final World world = new World(1, 1);
    final CastableList l = new CastableList(1, 0, 1);
    l.add(new ElfBoots());
    assertEquals(1, l.getCount());
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    w.set(PowerUps.DOUBLE, 1);
    w.setCastableList(l);
    final CastMaster castMaster = new CastMaster(world);
    w.setSelector(new OrdinarySelector(w, world, castMaster));
    w.setPlayerEngine(new AiEngine(world, new MoveMaster(world), castMaster));
    final Cell c = world.getCell(0);
    c.push(w);
    w.select(false);
    assertEquals(0, l.getCount());
    w.doCasting(c);
    assertEquals(Attribute.AGILITY.max(), w.get(Attribute.AGILITY));
    assertEquals(1, w.get(Attribute.AGILITY_RECOVERY));
  }
}
