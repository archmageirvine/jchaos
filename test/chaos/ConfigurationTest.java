package chaos;

import java.awt.GraphicsEnvironment;

import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class ConfigurationTest extends TestCase {

  public void test() {
    final Configuration c = new Configuration(10, 8, true, 11, 12);
    assertEquals(10, c.getCols());
    assertEquals(8, c.getRows());
    assertEquals(0, c.getCellWidth());
    assertEquals(0, c.getCellBits());
    assertEquals(0, c.getPixelWidth());
    assertEquals(0, c.getPixelHeight());
    assertEquals(0, c.getDepth());
    assertEquals(11, c.getWorldRows());
    assertEquals(12, c.getWorldCols());
    if (!GraphicsEnvironment.isHeadless()) {
      try {
        new Configuration(100000, 100000, false, -1, -1);
        fail();
      } catch (final UnsupportedOperationException e) {
        assertEquals("Cannot meet screen requirements.", e.getMessage());
      }
      final Configuration d = new Configuration(12, 10, false, -1, -1);
      assertTrue(d.getCols() >= 12);
      assertTrue(d.getRows() >= 10);
      assertEquals(d.getWorldCols(), d.getCols());
      assertEquals(d.getWorldRows(), d.getRows());
      assertTrue(d.getCellWidth() == 16 || d.getCellWidth() == 32 || d.getCellWidth() == 64);
      assertTrue(d.getCellBits() == 4 || d.getCellBits() == 5 || d.getCellBits() == 6);
    }
  }
}

