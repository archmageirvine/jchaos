package chaos.common.free;

import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectType;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class AcquisitionTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Acquisition();
  }

  public void testCast1() {
    final Acquisition x = new Acquisition();
    assertEquals(Castable.CAST_SINGLE, x.getCastFlags());
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    final Wizard1 w1 = new Wizard1();
    w1.setState(State.ACTIVE);
    final World world = new World(1, 2);
    world.getWizardManager().setWizard(1, w);
    world.getWizardManager().setWizard(0, w1);
    world.getCell(0).push(w1);
    world.getCell(1).push(w);
    castAndListenCheck(x, world, w, 1, CellEffectType.REDRAW_CELL, CellEffectType.ACQUISITION);
    assertEquals(w.getDefault(Attribute.LIFE) + 2, w.get(Attribute.LIFE));
    assertEquals(w1.getDefault(Attribute.LIFE) - 2, w1.get(Attribute.LIFE));
    assertEquals(w.getDefault(Attribute.INTELLIGENCE), w.get(Attribute.INTELLIGENCE));
    assertEquals(w1.getDefault(Attribute.INTELLIGENCE), w1.get(Attribute.INTELLIGENCE));
    nullParametersCastCheck(x, world, w);
  }

  public void testCast2() {
    final Acquisition x = new Acquisition();
    final Wizard1 w = new Wizard1();
    final Wizard1 w1 = new Wizard1();
    w1.setState(State.ACTIVE);
    w1.increment(Attribute.SPECIAL_COMBAT, 2);
    final World world = new World(1, 2);
    world.getWizardManager().setWizard(1, w);
    world.getWizardManager().setWizard(0, w1);
    world.getCell(0).push(w1);
    world.getCell(1).push(w);
    x.cast(world, w, world.getCell(1));
    assertEquals(w.getDefault(Attribute.LIFE) + 2, w.get(Attribute.LIFE));
    assertEquals(w1.getDefault(Attribute.LIFE) - 2, w1.get(Attribute.LIFE));
  }

  public void testCast3() {
    final Acquisition x = new Acquisition();
    final Wizard1 w = new Wizard1();
    final Wizard1 w1 = new Wizard1();
    w1.setState(State.ACTIVE);
    w1.increment(Attribute.SPECIAL_COMBAT, 3);
    final World world = new World(1, 2);
    world.getWizardManager().setWizard(1, w);
    world.getWizardManager().setWizard(0, w1);
    world.getCell(0).push(w1);
    world.getCell(1).push(w);
    x.cast(world, w, world.getCell(1));
    assertEquals(w.getDefault(Attribute.LIFE), w.get(Attribute.LIFE));
    assertEquals(w1.getDefault(Attribute.LIFE), w1.get(Attribute.LIFE));
    assertEquals(2, w.get(Attribute.SPECIAL_COMBAT));
    assertEquals(1, w1.get(Attribute.SPECIAL_COMBAT));
  }

  public void testCast4() {
    final Acquisition x = new Acquisition();
    final Wizard1 w = new Wizard1();
    final Wizard1 w1 = new Wizard1();
    w1.setState(State.ACTIVE);
    w1.increment(Attribute.RANGED_COMBAT, 3);
    w1.increment(Attribute.RANGE, 3);
    final World world = new World(1, 2);
    world.getWizardManager().setWizard(1, w);
    world.getWizardManager().setWizard(0, w1);
    world.getCell(0).push(w1);
    world.getCell(1).push(w);
    x.cast(world, w, world.getCell(1));
    assertEquals(w.getDefault(Attribute.LIFE), w.get(Attribute.LIFE));
    assertEquals(w1.getDefault(Attribute.LIFE), w1.get(Attribute.LIFE));
    assertEquals(2, w.get(Attribute.RANGED_COMBAT));
    assertEquals(1, w1.get(Attribute.RANGED_COMBAT));
    assertEquals(2, w.get(Attribute.RANGE));
    assertEquals(1, w1.get(Attribute.RANGE));
  }

  public void testCast5() {
    final Acquisition x = new Acquisition();
    final Wizard1 w = new Wizard1();
    final Wizard1 w1 = new Wizard1();
    w1.setState(State.ACTIVE);
    w1.increment(Attribute.COMBAT, 2);
    final World world = new World(1, 2);
    world.getWizardManager().setWizard(1, w);
    world.getWizardManager().setWizard(0, w1);
    world.getCell(0).push(w1);
    world.getCell(1).push(w);
    x.cast(world, w, world.getCell(1));
    assertEquals(w.getDefault(Attribute.LIFE), w.get(Attribute.LIFE));
    assertEquals(w1.getDefault(Attribute.LIFE), w1.get(Attribute.LIFE));
    assertEquals(3, w.get(Attribute.COMBAT));
    assertEquals(1, w1.get(Attribute.COMBAT));
  }

}
