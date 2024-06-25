package irvine.heraldry;

import irvine.TestUtils;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class TinctureTest extends TestCase {

  public void testTincture() {
    TestUtils.testEnum(Tincture.class, "[OR, ARGENT, AZURE, GULES, PURPURE, SABLE, VERT, MURREY, TENNE]");
  }
}

