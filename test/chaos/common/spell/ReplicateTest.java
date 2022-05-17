package chaos.common.spell;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.TargetFilter;
import chaos.common.inanimate.MagicWood;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class ReplicateTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new Replicate();
  }

  protected boolean checkEqual(final Lion a, final Lion b) {
    return a.getState() == b.getState()
      && a.getOwner() == b.getOwner()
      && a.get(Attribute.LIFE) == b.get(Attribute.LIFE)
      && a.get(Attribute.LIFE_RECOVERY) == b.get(Attribute.LIFE_RECOVERY)
      && a.get(PowerUps.HORROR) == b.get(PowerUps.HORROR)
      && a.is(PowerUps.REINCARNATE) == b.is(PowerUps.REINCARNATE)
      && a.get(Attribute.COMBAT) == b.get(Attribute.COMBAT)
      && a.get(Attribute.INTELLIGENCE) == b.get(Attribute.INTELLIGENCE);
  }

  public void testCast() {
    final World world = new World(10, 1);
    final Castable a = getCastable();
    assertEquals(8, a.getCastRange());
    assertEquals(Castable.CAST_LIVING | Castable.CAST_LOS | Castable.CAST_INANIMATE | Castable.CAST_GROWTH | Castable.CAST_NOWIZARDCELL, a.getCastFlags());
    final Wizard1 w = new Wizard1();
    final Lion l = new Lion();
    world.getCell(5).push(l);
    a.cast(world, w, world.getCell(5), world.getCell(0));
    if (world.getCell(4).peek() instanceof Lion) {
      assertNull(world.getCell(6).peek());
    } else {
      assertNull(world.getCell(4).peek());
      assertTrue(world.getCell(6).peek() instanceof Lion);
    }
    a.cast(world, w, world.getCell(5), world.getCell(0));
    // i.e. should have lion on both sides now
    assertTrue(world.getCell(4).peek() instanceof Lion);
    assertTrue(world.getCell(6).peek() instanceof Lion);
    // try again, this will fail but call ensures no exception
    a.cast(world, w, world.getCell(5), world.getCell(0));
    // check copies
    final Lion l4 = (Lion) world.getCell(4).peek();
    assertTrue(checkEqual(l, l4));
    assertTrue(checkEqual(l, (Lion) world.getCell(6).peek()));
    l4.set(PowerUps.REINCARNATE, l4.reincarnation() != null ? 1 : 0);
    l4.set(PowerUps.HORROR, 1);
    l4.set(Attribute.LIFE, 1);
    l4.setState(State.ASLEEP);
    l4.setOwner(3);
    assertFalse(l4.isMoved());
    a.cast(world, w, world.getCell(4), world.getCell(0));
    assertTrue(checkEqual(l4, (Lion) world.getCell(3).peek()));
    final MagicWood mw = new MagicWood();
    world.getCell(0).push(mw);
    a.cast(world, w, world.getCell(0), world.getCell(0));
    assertTrue(world.getCell(1).peek() instanceof MagicWood);
  }

  public void testFilter() {
    final World w = new World(2, 1);
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(1);
    final TargetFilter x = (TargetFilter) getCastable();
    final HashSet<Cell> t = new HashSet<>();
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    final Cell cn = new Cell(23);
    t.add(cn);
    x.filter(t, wiz, w);
    assertEquals(0, t.size());
    final Cell c = new Cell(0);
    final Lion l = new Lion();
    l.setOwner(1);
    c.push(l);
    t.add(c);
    x.filter(t, wiz, w);
    assertEquals(1, t.size());
    assertTrue(t.contains(c));
    final Cell c2 = new Cell(1);
    final Lion l2 = new Lion();
    l2.setOwner(1);
    c2.push(l2);
    t.add(c2);
    x.filter(t, wiz, w);
    assertEquals(2, t.size());
    assertTrue(t.contains(c));
    assertTrue(t.contains(c2));
    final Cell c3 = new Cell(42);
    wiz.setState(State.ACTIVE);
    c3.push(wiz);
    t.add(c3);
    x.filter(t, wiz, w);
    assertEquals(2, t.size());
    assertTrue(t.contains(c2));
    assertTrue(t.contains(c));
  }
}
