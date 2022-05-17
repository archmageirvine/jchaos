package chaos.common;

/**
 * Tests variables defined for a Unicaster.
 *
 * @author Sean A. Irvine
 */
public abstract class AbstractUnicasterTest extends AbstractMonsterTest {

  /**
   * Test supplied Monster is really a Unicaster.
   */
  @Override
  public void testInstanceOf() {
    super.testInstanceOf();
    assertTrue(mCastable instanceof Unicaster);
  }

  /**
   * Test Unicaster variables.
   */
  public void testUnicast() {
    final Unicaster u = (Unicaster) mCastable;
    assertTrue("Delay bad", u.mDelay > 0);
    assertTrue("Spell bad", u.mCastClass != null);
    try {
      final Object o = u.mCastClass.newInstance();
      assertTrue("Spell in not a Castable", o instanceof Castable);
    } catch (final Exception e) {
      fail(e.getMessage());
    }
  }

}
