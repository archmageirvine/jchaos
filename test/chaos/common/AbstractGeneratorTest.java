package chaos.common;

import chaos.board.World;

/**
 * Tests basic functionality that all Generators should satisfy.
 * @author Sean A. Irvine
 */
public abstract class AbstractGeneratorTest extends AbstractActorTest {

  /**
   * Test supplied Castable is really a Generator.
   */
  @Override
  public void testInstanceOf() {
    assertTrue(mCastable instanceof AbstractGenerator);
    assertTrue(mCastable instanceof NoDeadImage);
  }

  public void testValidStateGenerates() {
    final World w = new World(2, 1);
    final AbstractGenerator g = (AbstractGenerator) mCastable;
    w.getCell(0).push(g);
    g.setState(State.ASLEEP);
    for (int k = 0; k < 10; ++k) {
      g.generate(w, 0);
      assertNull(w.actor(1));
    }
    g.setState(State.DEAD);
    for (int k = 0; k < 10; ++k) {
      g.generate(w, 0);
      assertNull(w.actor(1));
    }
    g.setState(State.ACTIVE);
  }
}
