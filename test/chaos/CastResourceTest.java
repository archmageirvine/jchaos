package chaos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

import chaos.common.Castable;
import irvine.util.io.IOUtils;
import junit.framework.TestCase;

/**
 * Tests the cast resources to make sure all entries correspond to existent
 * spells and checks the spelling and length of the entries.
 *
 * @author Sean A. Irvine
 */
public class CastResourceTest extends TestCase {

  /**
   * Want to test that all described spells actually occur in the system.  This
   * is tricky because it is not trivial to get a list of all classes implementing
   * Castable in the classpath.  Therefore we resort to a nasty retrieval from
   * the frequency resource as well.
   *
   * @exception Exception if an error occurs
   */
  public void testExistence() throws Exception {
    final StringBuilder sb = new StringBuilder();
    final HashSet<String> set = new HashSet<>();
    try (final BufferedReader f = IOUtils.reader("chaos/resources/frequency.txt")) {
      String line;
      while ((line = f.readLine()) != null) {
        if (!line.isEmpty() && line.charAt(0) != '#') {
          final int t = line.lastIndexOf('.');
          final int sp = line.indexOf(' ');
          if (t != -1 && sp != -1 && sp > t) {
            // first check we can instantiate the class from frequency.txt
            final String clazz = line.substring(0, sp);
            try {
              assertTrue(Class.forName(clazz).getDeclaredConstructor().newInstance() instanceof Castable);
            } catch (final ClassNotFoundException e) {
              sb.append(clazz).append(" is not found\n");
            }
            // retain in the map
            set.add(clazz.substring(t + 1));
          }
        }
      }
    }
    try (final BufferedReader r = IOUtils.reader("chaos/resources/CastResource.properties")) {
      String line;
      while ((line = r.readLine()) != null) {
        final int t = line.indexOf("Name=");
        if (t != -1) {
          final String clazz = line.substring(0, t);
          if (!set.contains(clazz) && !set.contains("Named" + clazz)) {
            sb.append(clazz).append(" has no corresponding implementation\n");
          }
        }
      }
    }
    if (sb.length() != 0) {
      fail(sb.toString());
    }
  }

  private static void addDictionary(final BufferedReader r, final HashSet<String> s) throws IOException {
    String line;
    while ((line = r.readLine()) != null) {
      s.add(line);
    }
  }

  private static String tidy(final String w) {
    int i = -1;
    while (++i < w.length()) {
      final char c = w.charAt(i);
      if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
        break;
      }
    }
    final int left = i;
    i = w.length();
    while (--i >= left) {
      final char c = w.charAt(i);
      if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
        break;
      }
    }
    return i == left ? null : w.substring(left, i + 1);
  }

  public void testSpelling() throws Exception {
    final HashSet<String> dict = new HashSet<>();
    try {
      try (final BufferedReader r = new BufferedReader(new FileReader("/usr/share/dict/linux.words"))) {
        addDictionary(r, dict);
      }
    } catch (final IOException e) {
      try {
        try (final BufferedReader r = new BufferedReader(new FileReader("/usr/share/dict/words"))) {
          addDictionary(r, dict);
        }
      } catch (final IOException ex) {
        // too bad, probably means dict not found
      }
    }
    try {
      try (final BufferedReader r = IOUtils.reader("chaos/words.txt")) {
        addDictionary(r, dict);
      }
    } catch (final IOException e) {
      // too bad, probably means dict not found
    }
    try (final BufferedReader r = IOUtils.reader("chaos/resources/CastResource.properties")) {
      final HashSet<String> bad = new HashSet<>();
      String line;
      while ((line = r.readLine()) != null) {
        if (!line.isEmpty()) {
          final String[] parts = line.split("\\s+");
          for (final String p : parts) {
            final String w = tidy(p);
            if (w != null && !w.isEmpty() && w.indexOf('=') == -1 && !dict.contains(w) && !dict.contains(w.toLowerCase(Locale.getDefault())) && (!w.endsWith("s") || !dict.contains(w.substring(0, w.length() - 1))) && (!w.endsWith("'s") || !dict.contains(w.substring(0, w.length() - 2)))) {
              bad.add(w);
            }
          }
        }
      }
      if (!bad.isEmpty()) {
        final String[] b = bad.toArray(new String[0]);
        Arrays.sort(b);
        final StringBuilder sb = new StringBuilder();
        for (final String s : b) {
          sb.append(s).append('\n');
        }
        fail("Bad spelling detected:\n" + sb.toString());
      }
    }
  }
}
