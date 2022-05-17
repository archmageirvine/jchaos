package irvine.util.graphics;

import java.io.BufferedInputStream;
import java.io.IOException;

import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class EstimatePbmSkewTest extends TestCase {

  public void test() throws IOException {
    try (final BufferedInputStream is = new BufferedInputStream(EstimatePbmSkewTest.class.getResourceAsStream("/irvine/util/graphics/resources/example.pbm"))) {
      assertEquals(-5.0, EstimatePbmSkew.skew(is), 1e-3);
    }
  }
}
