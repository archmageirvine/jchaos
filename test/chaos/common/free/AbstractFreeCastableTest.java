package chaos.common.free;

import java.lang.reflect.Method;

import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.FreeCastable;
import chaos.common.PowerUp;
import chaos.common.wizard.Wizard;
import chaos.util.CellEffectType;

/**
 * Tests basic functionality that all FreeCastables should satisfy.
 *
 * @author Sean A. Irvine
 */
public abstract class AbstractFreeCastableTest extends AbstractCastableTest {

  /**
   * Test supplied Castable is really an Actor.
   */
  public void testInstanceOf() {
    assertTrue(mCastable instanceof FreeCastable);
  }

  /**
   * Testing the casting range is valid.
   */
  @Override
  public void testCastRange() {
    assertEquals("Cast range error", 0, mCastable.getCastRange());
  }

  /**
   * Tests, using reflection, that method names are acceptable.
   */
  public void testMethodNames() {
    final Method[] methods = mCastable.getClass().getMethods();
    // we know methods cannot be null
    for (final Method method : methods) {
      final String name = method.getName();
      if ("getPowerUpType".equals(name) && !(mCastable instanceof PowerUp)) {
        fail("Saw getPowerUpType() without implementing Bonus");
      }
    }
  }

  protected void nullParametersCastCheck(final FreeCastable x, final World world, final Wizard w) {
    x.cast(world, w, null);
    x.cast(world, null, world.getCell(0));
    x.cast(null, w, world.getCell(0));
  }

  protected void castAndListenCheck(final FreeCastable x, final World world, final Wizard w, final int cell, final CellEffectType... types) {
    final TestListener listen = new TestListener(types);
    world.register(listen);
    x.cast(world, w, cell == -1 ? null : world.getCell(cell));
    world.deregister(listen);
    listen.checkAndReset();
  }
}
