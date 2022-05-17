package chaos.util;

import java.util.HashSet;

import chaos.common.monster.Lion;
import chaos.common.monster.NamedCentaur;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class NameUtilsTest extends TestCase {

  public void testNonNamed() {
    assertEquals("Lion", NameUtils.getTextName(new Lion()));
  }

  public void testNamed() {
    final HashSet<String> seen = new HashSet<>();
    for (int k = 0; k < 5; ++k) {
      final String name = NameUtils.getTextName(new NamedCentaur());
      assertTrue(seen.add(name));
      assertFalse(name.contains("Centaur"));
    }
  }
}
