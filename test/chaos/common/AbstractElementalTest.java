package chaos.common;

/**
 * Tests functionality that elementals should satisfy.
 *
 * @author Sean A. Irvine
 */
public abstract class AbstractElementalTest extends AbstractMonsterTest {

  @Override
  public void testInstanceOf() {
    super.testInstanceOf();
    assertTrue(mCastable instanceof Elemental);
  }

  @Override
  public void testReincarnation() {
    assertNull(((Monster) mCastable).reincarnation());
  }

  public void testElemental() {
    final Monster m = (Monster) mCastable;
    m.setOwner(7);
    final Elemental e = (Elemental) mCastable;
    final Actor a = e.getElementalReplacement();
    assertNotNull(a);
    assertEquals(7, a.getOwner());
    assertTrue(!(a instanceof Elemental));
  }
}
