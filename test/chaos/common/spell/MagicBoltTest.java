package chaos.common.spell;

import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Monster;
import chaos.common.State;
import chaos.common.free.AbstractDecrementTest;
import chaos.common.inanimate.WeakWall;
import chaos.common.monster.Horse;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class MagicBoltTest extends AbstractDecrementTest {

  @Override
  public Castable getCastable() {
    return new MagicBolt();
  }

  public void testCast() {
    final Castable x = getCastable();
    assertEquals(Castable.CAST_LIVING | Castable.CAST_INANIMATE | Castable.CAST_LOS | Castable.CAST_GROWTH, x.getCastFlags());
    assertEquals(8, x.getCastRange());
    final Wizard1 w = new Wizard1();
    final World world = new World(2, 1);
    world.getCell(0).push(w);
    Monster h = new Horse();
    world.getCell(1).push(h);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(0, h.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(0, h.get(Attribute.LIFE));
    assertEquals(State.DEAD, h.getState());
    assertEquals(h, world.actor(1));
    assertEquals(8, w.getScore());
    h = new Horse();
    h.set(Attribute.LIFE, 16);
    world.getCell(1).pop();
    world.getCell(1).push(h);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(0, h.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(0, h.get(Attribute.LIFE));
    assertEquals(State.DEAD, h.getState());
    assertEquals(h, world.actor(1));
    h = new Horse();
    h.set(Attribute.MAGICAL_RESISTANCE, 31);
    world.getCell(1).pop();
    world.getCell(1).push(h);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(1, h.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(h.getDefault(Attribute.LIFE), h.get(Attribute.LIFE));
    assertEquals(State.ACTIVE, h.getState());
    assertEquals(h, world.actor(1));
    final Actor i = new WeakWall();
    i.set(Attribute.MAGICAL_RESISTANCE, 0);
    world.getCell(1).pop();
    world.getCell(1).push(i);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(0, i.get(Attribute.MAGICAL_RESISTANCE));
    assertNull(world.actor(1));
    assertEquals(1, w.getBonusCount());
    assertEquals(1, w.getBonusSelect());
  }
}
