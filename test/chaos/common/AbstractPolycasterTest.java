package chaos.common;

/**
 * Tests variables defined for a Unicaster.
 *
 * @author Sean A. Irvine
 */
public abstract class AbstractPolycasterTest extends AbstractMonsterTest {

  /**
   * Test supplied Monster is really a Polycaster.
   */
  @Override
  public void testInstanceOf() {
    super.testInstanceOf();
    assertTrue(mCastable instanceof Polycaster);
  }

  /**
   * Test Polycaster variables.
   */
  public void testPolycast() {
    final Polycaster u = (Polycaster) mCastable;
    assertTrue("Delay bad", u.mDelay > 0);
    assertTrue("Spells bad", u.mCastClass != null);
    assertTrue("Spells bad", u.mCastClass.length > 1);
    for (int i = 0; i < u.mCastClass.length; ++i) {
      try {
        final Object o = u.mCastClass[i].newInstance();
        assertTrue("Spell is not a Castable", o instanceof Castable);
      } catch (final Exception e) {
        fail(e.getMessage());
      }
    }
  }

}
