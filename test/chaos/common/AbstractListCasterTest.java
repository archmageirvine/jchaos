package chaos.common;

/**
 * Tests a list caster makes at least one cast.
 * @author Sean A. Irvine
 */
public abstract class AbstractListCasterTest extends AbstractMonsterTest {

  @Override
  public void testInstanceOf() {
    super.testInstanceOf();
    assertTrue(mCastable instanceof ListCaster);
  }

  public void testCast() {
    final ListCaster u = (ListCaster) mCastable;
    assertNotNull(u.getCastable());
  }
}
