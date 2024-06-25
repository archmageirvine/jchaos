package chaos.common.spell;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.free.AbstractDecrementTest;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class StopTest extends AbstractDecrementTest {

  @Override
  public Castable getCastable() {
    return new Stop();
  }

  public void testCast() {
    assertEquals(Attribute.MOVEMENT.max(), new Stop().decrement());
    final World world = new World(20, 2);
    final Wizard w = new Wizard1();
    world.getWizardManager().setWizard(1, w);
    world.getCell(0).push(w);
    final Lion l = new Lion();
    world.getCell(5).push(l);
    final Stop s = new Stop();
    assertEquals(Castable.CAST_LIVING, s.getCastFlags());
    assertEquals(Castable.MAX_CAST_RANGE, s.getCastRange());
    s.cast(world, w, world.getCell(5), world.getCell(0));
    assertEquals(0, l.get(Attribute.MOVEMENT));
    assertEquals(3, s.getMultiplicity());
  }

  public void testCastOnAlreadyStopped() {
    final World world = new World(20, 2);
    final Wizard w = new Wizard1();
    world.getWizardManager().setWizard(1, w);
    world.getCell(0).push(w);
    final Lion l = new Lion();
    l.set(Attribute.MOVEMENT, 0);
    world.getCell(5).push(l);
    final Stop s = new Stop();
    final HashSet<Cell> targets = new HashSet<>();
    targets.add(world.getCell(5));
    s.filter(targets, w, world);
    assertEquals(0, targets.size());
  }

}
