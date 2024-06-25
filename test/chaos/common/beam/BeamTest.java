package chaos.common.beam;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.Monster;
import chaos.common.State;
import chaos.common.monster.Horse;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class BeamTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new PlasmaBeam();
  }

  public void testCast() {
    final Castable x = new PlasmaBeam();
    assertEquals(Castable.CAST_ANY, x.getCastFlags());
    assertEquals(1, x.getCastRange());
    final Wizard1 w = new Wizard1();
    final World world = new World(2, 1);
    world.getCell(0).push(w);
    final Monster h = new Horse();
    world.getCell(1).push(h);
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(0, h.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(0, h.get(Attribute.LIFE));
    assertEquals(State.DEAD, h.getState());
    assertEquals(h, world.actor(1));
    assertEquals(8, w.getScore());
    x.cast(world, w, world.getCell(1), world.getCell(0));
    assertEquals(13, w.getScore());
    assertEquals(null, world.actor(1));
    x.cast(world, w, world.getCell(0), null);
    x.cast(world, w, null, world.getCell(0));
    x.cast(world, null, world.getCell(0), world.getCell(0));
    x.cast(null, w, world.getCell(0), world.getCell(0));
  }

  private static class MyBeam extends Beam {
    int mCount = 0;

    @Override
    protected Attribute getAttribute() {
      return Attribute.LIFE;
    }

    @Override
    public void beamEffect(final Cell cell, final Caster caster, final Cell casterCell) {
      ++mCount;
    }
  }

  public void testValids() {
    final MyBeam beam = new MyBeam();
    final World world = new World(5, 5);
    beam.cast(world, null, world.getCell(0), world.getCell(2));
    assertEquals(0, beam.mCount);
    beam.cast(world, null, world.getCell(0), world.getCell(0));
    assertEquals(0, beam.mCount);
    beam.cast(world, null, world.getCell(0), world.getCell(12));
    assertEquals(0, beam.mCount);
    beam.cast(world, null, world.getCell(0), world.getCell(10));
    assertEquals(0, beam.mCount);
    beam.cast(world, null, world.getCell(2), world.getCell(0));
    assertEquals(0, beam.mCount);
    beam.cast(world, null, world.getCell(12), world.getCell(0));
    assertEquals(0, beam.mCount);
    beam.cast(world, null, world.getCell(10), world.getCell(0));
    assertEquals(0, beam.mCount);
    beam.cast(world, null, world.getCell(6), world.getCell(0));
    assertEquals(4, beam.mCount);
    beam.cast(world, null, world.getCell(16), world.getCell(10));
    assertEquals(6, beam.mCount);
    beam.cast(world, null, world.getCell(10), world.getCell(16));
    assertEquals(7, beam.mCount);
    beam.cast(null, null, world.getCell(10), world.getCell(16));
    assertEquals(7, beam.mCount);
  }
}
