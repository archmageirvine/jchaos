package chaos.common.spell;

import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.free.AbstractDecrementTest;
import chaos.common.inanimate.StandardWall;
import chaos.common.monster.Horse;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class LifeAttackTest extends AbstractDecrementTest {

  @Override
  public Castable getCastable() {
    return new LifeAttack();
  }

  public void testCast() {
    final Castable x = getCastable();
    assertEquals(Castable.CAST_LIVING | Castable.CAST_INANIMATE | Castable.CAST_LOS | Castable.CAST_GROWTH, x.getCastFlags());
    assertEquals(10, x.getCastRange());
    final Wizard1 w = new Wizard1();
    final World world = new World(2, 1);
    world.getCell(0).push(w);
    Actor h = new Horse();
    h.set(Attribute.LIFE, 10);
    world.getCell(1).push(h);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(0, h.get(Attribute.LIFE));
    assertEquals(State.DEAD, h.getState());
    assertEquals(h, world.actor(1));
    assertEquals(8, w.getScore());
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(16, w.getScore());
    assertEquals(null, world.actor(1));
    h = new StandardWall();
    h.set(Attribute.LIFE, 30);
    world.getCell(1).pop();
    world.getCell(1).push(h);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(20, h.get(Attribute.LIFE));
    assertEquals(State.ACTIVE, h.getState());
    assertEquals(h, world.actor(1));
    h.set(Attribute.LIFE, 1);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(1, w.getBonusCount());
    assertEquals(1, w.getBonusSelect());
  }
}
