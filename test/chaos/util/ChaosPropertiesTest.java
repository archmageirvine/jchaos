package chaos.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import irvine.util.io.IOUtils;
import junit.framework.TestCase;

/**
 * Test the corresponding class.
 * @author Sean A. Irvine
 */
public class ChaosPropertiesTest extends TestCase {

  public void test() throws IOException {
    final File chaos = new File(".chaos");
    final File backup = new File(".chaos.bak");
    if (backup.exists()) {
      assertTrue(backup.delete());
    }
    try {
      if (chaos.exists()) {
        assertTrue(chaos.renameTo(backup));
      }
      final ChaosProperties p = ChaosProperties.properties();
      assertNotNull(p);
      assertNull(p.getProperty("xtestx"));
      assertEquals(42.0, p.getDoubleProperty("double", 42.0));
      assertEquals(42, p.getIntProperty("int", 42));
      p.setProperty("xtestx", "xhix");
      p.setProperty("double", "43");
      p.setProperty("int", "43");
      assertTrue(Math.abs(43 - p.getDoubleProperty("double", 42.0)) < 0.0001);
      assertEquals(43, p.getIntProperty("int", 42));
      p.save();
      try (final FileInputStream is = new FileInputStream(chaos)) {
        final String res = IOUtils.readAll(is);
        assertTrue(res.contains("xtestx"));
        assertTrue(res.contains("xhix"));
        assertTrue(res.contains("ChaosProperties configuration file"));
      }
      // Try harder to remove test property
      p.remove("xtestx");
      p.save();
    } finally {
      assertTrue(chaos.delete());
      if (backup.exists()) {
        assertTrue(backup.renameTo(chaos));
      }
    }
  }
}
