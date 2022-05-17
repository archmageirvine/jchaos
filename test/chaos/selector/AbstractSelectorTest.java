package chaos.selector;

import junit.framework.TestCase;
import chaos.common.Castable;
import chaos.common.dragon.RedDragon;
import chaos.common.free.Quench;
import chaos.common.growth.Fire;
import chaos.common.monster.Horse;
import chaos.common.monster.Hydra;
import chaos.common.monster.Lion;
import chaos.common.monster.Ogre;
import chaos.common.monster.Skeleton;

/**
 * Tests basic functionality that all Selectors should satisfy.
 *
 * @author Sean A. Irvine
 */
public abstract class AbstractSelectorTest extends TestCase {

  protected Selector mSelector;

  // implemented in subclasses to provide objects
  public abstract Selector getSelector();

  @Override
  public void setUp() {
    mSelector = getSelector();
  }

  @Override
  public void tearDown() {
    mSelector = null;
  }

  public void testNull() {
    final Castable[] c = mSelector.select(null, true);
    assertEquals(2, c.length);
    assertNull(c[0]);
    assertNull(c[1]);
  }

  public void testEmpty() {
    final Castable[] c = mSelector.select(new Castable[0], true);
    assertEquals(2, c.length);
    assertNull(c[0]);
    assertNull(c[1]);
  }

  public void testSimple() {
    final Castable[] c = {
      new Lion(),
      new Lion(),
      new Hydra(),
      new Ogre(),
      new Quench(),
      new Horse(),
      new RedDragon(),
      new Skeleton(),
      new Skeleton(),
      new Fire(),
      new Lion(),
    };
    for (int i = 0; i < 10; ++i) {
      final Castable cs = mSelector.select(c, false)[0];
      boolean seen = false;
      for (int j = 0; j < c.length && !seen; ++j) {
        seen = c[j] == cs;
      }
      if (!seen) {
        fail("Faulty choice: " + i);
      }
    }
  }

  public void testSimpleWithDiscard() {
    final Castable[] c = {
      new Lion(),
      new Lion(),
      new Hydra(),
      new Ogre(),
      new Quench(),
      new Horse(),
      new RedDragon(),
      new Skeleton(),
      new Skeleton(),
      new Fire(),
      new Lion(),
    };
    final Castable[] select = mSelector.select(c, true);
    assertEquals(2, select.length);
    assertNotNull(select[0]);
    assertNotNull(select[1]);
    assertTrue(select[0] != select[1]);
  }

  public void testBonusSelect() {
    Castable[] c = new Castable[0];
    assertEquals(0, mSelector.selectBonus(c, 0).length);
    c = new Castable[] {new Lion()};
    assertEquals(0, mSelector.selectBonus(c, 0).length);
    assertEquals(1, mSelector.selectBonus(c, 1).length);
  }
}
