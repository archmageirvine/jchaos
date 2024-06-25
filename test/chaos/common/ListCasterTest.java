package chaos.common;

import chaos.common.monster.Eagle;
import chaos.common.monster.Lion;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class ListCasterTest extends TestCase {

  public void test() {
    final ListCaster f = new ListCaster(Lion.class, Eagle.class) {
      @Override
      public Class<? extends Monster> reincarnation() {
        return null;
      }

      @Override
      public long getLosMask() {
        return 0;
      }
    };
    assertTrue(f.getCastable() instanceof Lion);
    assertTrue(f.getCastable() instanceof Eagle);
    assertNull(f.getCastable());
  }
}
