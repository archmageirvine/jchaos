package chaos.common.spell;

import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Monster;
import chaos.common.State;
import chaos.common.free.AbstractDecrementTest;
import chaos.common.monster.Horse;
import chaos.common.monster.StoneGiant;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class IdiocyTest extends AbstractDecrementTest {

  @Override
  public Castable getCastable() {
    return new Idiocy();
  }

  public void testCast() {
    final Castable x = getCastable();
    assertEquals(Castable.CAST_LIVING | Castable.CAST_LOS, x.getCastFlags());
    assertEquals(9, x.getCastRange());
    final Wizard1 w = new Wizard1();
    final World world = new World(2, 1);
    world.getCell(0).push(w);
    Monster h = new Horse();
    world.getCell(1).push(h);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(0, h.get(Attribute.INTELLIGENCE));
    assertEquals(0, h.get(Attribute.LIFE));
    assertEquals(State.DEAD, h.getState());
    assertEquals(h, world.actor(1));
    assertEquals(8, w.getScore());
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(16, w.getScore());
    assertEquals(null, world.actor(1));
    h = new StoneGiant();
    h.set(Attribute.INTELLIGENCE, 66);
    h.set(Attribute.LIFE, 30);
    world.getCell(1).pop();
    world.getCell(1).push(h);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(0, h.get(Attribute.INTELLIGENCE));
    assertEquals(0, h.get(Attribute.LIFE));
    assertEquals(State.DEAD, h.getState());
    assertEquals(h, world.actor(1));
    assertEquals(2, w.getBonusCount());
    assertEquals(1, w.getBonusSelect());
    h = new Horse();
    h.set(Attribute.INTELLIGENCE, 67);
    world.getCell(1).pop();
    world.getCell(1).push(h);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(0, h.get(Attribute.INTELLIGENCE));
    assertEquals(State.ACTIVE, h.getState());
    assertEquals(h, world.actor(1));
  }
}
