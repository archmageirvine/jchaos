package chaos.common;

/**
 * Tests basic functionality that all Drainers should satisfy.
 *
 * @author Sean A. Irvine
 */
public abstract class AbstractDrainerTest extends AbstractCastableTest {


  public void testADrainer() {
    final Drainer d = (Drainer) getCastable();
    assertTrue(d.getDamage() > 0);
    assertNotNull(d.getEffectType());
  }

}
