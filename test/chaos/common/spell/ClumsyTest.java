package chaos.common.spell;

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
public class ClumsyTest extends AbstractDecrementTest {

  @Override
  public Castable getCastable() {
    return new Clumsy();
  }

  public void testCast() {
    final World world = new World(20, 2);
    final Wizard w = new Wizard1();
    world.getWizardManager().setWizard(1, w);
    world.getCell(0).push(w);
    final Lion l = new Lion();
    l.set(Attribute.AGILITY, 5);
    world.getCell(5).push(l);
    final Clumsy s = new Clumsy();
    assertEquals(Castable.CAST_LIVING, s.getCastFlags());
    assertEquals(Castable.MAX_CAST_RANGE, s.getCastRange());
    s.cast(world, w, world.getCell(5), world.getCell(0));
    assertEquals(0, l.get(Attribute.AGILITY));
    assertEquals(3, s.getMultiplicity());
  }

}
