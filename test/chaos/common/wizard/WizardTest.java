package chaos.common.wizard;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Castable;
import chaos.common.CastableList;
import chaos.common.Caster;
import chaos.common.Monster;
import chaos.common.Multiplicity;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.monster.Orc;
import chaos.engine.PlayerEngine;
import chaos.selector.Selector;
import junit.framework.Assert;

/**
 * Tests this wizard.
 *
 * @author Sean A. Irvine
 */
public class WizardTest extends AbstractWizardTest {

  @Override
  public Castable getCastable() {
    final Wizard w = new Wizard1();
    w.setState(State.ACTIVE);
    return w;
  }

  public void testSWP() {
    final Wizard w = new Wizard1();
    try {
      w.setPersonalName(null);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Illegal wizard name", e.getMessage());
    }
    try {
      w.setPersonalName("abcdefghijklmnopqrstuvwxyz");
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Illegal wizard name", e.getMessage());
    }
    assertEquals(0x007E7E7E3C3C7E00L, w.getLosMask());
    try {
      w.addScore(-1);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Cannot add negative points", e.getMessage());
    }
    assertEquals(0, w.getScore());
    w.addScore(42);
    assertEquals(42, w.getScore());
    w.addScore(42);
    assertEquals(84, w.getScore());
    try {
      w.addBonus(-1, -1);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad arguments to addBonus", e.getMessage());
    }
    try {
      w.addBonus(2, 1);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad arguments to addBonus", e.getMessage());
    }
    assertEquals(State.DEAD, w.getState());
    assertNull(w.getCastable());
    w.select(false);
    assertNull(w.getCastable());
    w.setMass(42);
    assertEquals(42, w.getMass());
    w.bonusSelect();
  }

  private static class NecroSelector implements Selector {
    @Override
    public Castable[] select(final Castable[] castables, final boolean texas) {
      assertTrue(castables.length > 0);
      for (final Castable castable : castables) {
        assertTrue(castable instanceof Monster);
      }
      return new Castable[] {castables[0], null};
    }

    @Override
    public Castable[] selectBonus(final Castable[] spells, final int count) {
      return new Castable[0];
    }
  }

  private static class MyEngine implements PlayerEngine {
    private final int[] mCount;
    MyEngine(final int[] count) {
      mCount = count;
    }
    @Override
    public boolean cast(final Caster caster, final Castable c, final Cell cell) {
      mCount[0]++;
      return true;
    }
    @Override
    public void moveAll(final Wizard w) {
    }
  }

  public void testNecropotenceSelector() {
    final Wizard w = new Wizard1();
    w.setCastableList(new CastableList(100, 100, 24));
    w.set(PowerUps.NECROPOTENCE, 1);
    w.setSelector(new NecroSelector());
    w.select(false);
    assertTrue(w.get(PowerUps.NECROPOTENCE) < 2);
    assertTrue(w.getCastable() instanceof Monster);
    if (w.getCastable() instanceof Orc) {
      return;
    }
    final World world = new World(3, 3);
    final Cell cell = world.getCell(4);
    cell.push(w);
    w.setState(State.ACTIVE);
    final int[] count = new int[1];
    w.setPlayerEngine(new MyEngine(count));
    final int expectedCount;
    if (w.getCastable() instanceof Multiplicity) {
      expectedCount = ((Multiplicity) w.getCastable()).getMultiplicity();
    } else {
      expectedCount = 1;
    }
    w.set(PowerUps.DOUBLE, 3);
    w.doCasting(cell);
    assertEquals(2, w.get(PowerUps.DOUBLE));
    assertEquals(w.getCastable().toString(), 2 * expectedCount, count[0]);
    w.set(PowerUps.TRIPLE, 3);
    count[0] = 0;
    w.doCasting(cell);
    assertEquals(6 * expectedCount, count[0]);
    assertEquals(2, w.get(PowerUps.TRIPLE));
    assertEquals(1, w.get(PowerUps.DOUBLE));
    final Wizard w2 = new Wizard1();
    assertNull(w2.getCastableList());
    w2.setCastableList(new CastableList(100, 100, 24));
    final CastableList cl = w2.getCastableList();
    assertNotNull(cl);
    assertEquals(24, cl.getMaximumVisible());
    for (int i = 0; i < 20; ++i) {
      w2.set(PowerUps.NECROPOTENCE, 1);
      w2.select(false);
      assertTrue(w2.get(PowerUps.NECROPOTENCE) < 2);
    }
  }

  private static class SillySelector implements Selector {
    @Override
    public Castable[] select(final Castable[] castables, final boolean texas) {
      return new Castable[] {castables.length > 0 ? castables[0] : null, null};
    }
    @Override
    public Castable[] selectBonus(final Castable[] spells, final int count) {
      return new Castable[0];
    }
  }

  public void testSillySelector() {
    final Wizard w = new Wizard1();
    w.setCastableList(new CastableList(100, 100, 24));
    w.setSelector(new SillySelector());
    for (int i = 0; i < 120; ++i) {
      w.select(false);
      if (i < 100) {
        assertNotNull(w.getCastable());
      } else {
        assertNull(w.getCastable());
      }
    }
    w.addBonus(10, 10);
    w.bonusSelect();
    assertEquals(0, w.getBonusCount());
    assertEquals(0, w.getBonusSelect());
  }

  private static class AmnesiaSelector implements Selector {
    @Override
    public Castable[] select(final Castable[] castables, final boolean texas) {
      Assert.assertTrue(castables.length <= 1);
      return new Castable[] {castables.length > 0 ? castables[0] : null, null};
    }
    @Override
    public Castable[] selectBonus(final Castable[] spells, final int count) {
      Assert.assertEquals(2, count);
      Assert.assertEquals(4, spells.length);
      final Castable[] res = new Castable[count];
      System.arraycopy(spells, 0, res, 0, count);
      return res;
    }
  }

  public void testSillySelectorAmnesia() {
    final Wizard w = new Wizard1();
    w.setCastableList(new CastableList(100, 100, 24));
    w.setSelector(new AmnesiaSelector());
    for (int i = 0; i < 120; ++i) {
      w.set(PowerUps.AMNESIA, 1);
      w.select(false);
      assertTrue(w.get(PowerUps.AMNESIA) < 2);
      if (i < 100) {
        assertNotNull(w.getCastable());
      } else {
        assertNull(w.getCastable());
      }
    }
    w.set(PowerUps.AMNESIA, 1);
    for (int i = 0; i < 30; ++i) {
      w.select(false);
    }
    assertEquals(0, w.get(PowerUps.AMNESIA));
    w.addBonus(2, 4);
    w.bonusSelect();
  }

  private static class CoercionSelector implements Selector {
    @Override
    public Castable[] select(final Castable[] castables, final boolean texas) {
      Assert.assertEquals(0, castables.length);
      return new Castable[] {null, null};
    }
    @Override
    public Castable[] selectBonus(final Castable[] spells, final int count) {
      return new Castable[0];
    }
  }

  private static class NullCoercionSelector implements Selector {
    @Override
    public Castable[] select(final Castable[] castables, final boolean texas) {
      return new Castable[] {null, null};
    }
    @Override
    public Castable[] selectBonus(final Castable[] spells, final int count) {
      return new Castable[0];
    }
  }

  public void testSillySelectorCoercion() {
    final Wizard w = new Wizard1();
    w.setCastableList(new CastableList(100, 100, 24));
    w.setSelector(new CoercionSelector());
    for (int i = 0; i < 50; ++i) {
      w.set(PowerUps.COERCION, 1);
      w.select(false);
      assertTrue(w.get(PowerUps.COERCION) < 2);
      assertNull(w.getCastable());
    }
    w.set(PowerUps.COERCION, 1);
    w.setSelector(new NullCoercionSelector());
    for (int i = 0; i < 30; ++i) {
      w.select(false);
    }
    assertEquals(0, w.get(PowerUps.COERCION));
  }

}
