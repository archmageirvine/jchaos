package chaos.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;

import chaos.common.Castable;
import chaos.common.Named;
import irvine.util.io.IOUtils;

/**
 * Name utility functions.
 *
 * @author Sean A. Irvine
 */
public final class NameUtils {

  private NameUtils() { }

  /**
   * Get the display name of this castable.
   *
   * @param c castable
   * @return display name
   */
  public static String getTextName(final Castable c) {
    if (c instanceof Named) {
      return ((Named) c).getPersonalName();
    }
    return c.getName();
  }

  private static final String NAMED = "Named";
  private static final int NLEN = NAMED.length();

  private static String getShortName(final Class<? extends Castable> clazz) {
    final String className = clazz.getName();
    final String fullName = className.substring(className.lastIndexOf('.') + 1);
    return fullName.startsWith(NAMED) ? fullName.substring(NLEN) : fullName;
  }

  /**
   * Get the short name of this castable. This is based on the class name,
   * but excludes the prefix <code>Named</code> where present.
   *
   * @param c castable
   * @return short name
   */
  public static String getShortName(final Castable c) {
    return getShortName(c.getClass());
  }

  /**
   * Get the resource name for the backdrop of this castable.
   *
   * @param c castable
   * @return display name
   */
  public static String getBackdropResource(final Castable c) {
    final String rawName = c.getName().toLowerCase(Locale.getDefault());
    return "chaos/resources/backdrops/" + rawName.replace(' ', '_').replace("'", "") + ".png";
  }

  /** Maximum length of a personal name. */
  public static final int MAX_NAME_LENGTH = 25;
  private static final int MAX_TRIES = 50;
  private static final Random RANDOM = new Random();
  private static final HashSet<String> USED = new HashSet<>();
  private static final HashMap<Class<? extends Castable>, String[]> NAMES_BY_CLASS = new HashMap<>();

  private static String[] initNames(final Class<? extends Castable> clazz) {
    final String[] n = NAMES_BY_CLASS.get(clazz);
    if (n != null) {
      return n;
    }
    final String resourceName = "chaos/resources/names/" + getShortName(clazz).toLowerCase(Locale.getDefault());
    final HashSet<String> names = new HashSet<>();
    try (final BufferedReader r = IOUtils.reader(resourceName)) {
      String line;
      while ((line = r.readLine()) != null) {
        if (!line.isEmpty() && line.length() <= MAX_NAME_LENGTH && line.charAt(0) != '#') {
          names.add(line.trim());
        }
      }
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
    if (names.isEmpty()) {
      throw new RuntimeException();
    }
    final String[] nameArray = names.toArray(new String[0]);
    NAMES_BY_CLASS.put(clazz, nameArray);
    return nameArray;
  }

  private static String selectPersonalName(final Class<? extends Castable> clazz) {
    final String[] names = initNames(clazz);
    return names[RANDOM.nextInt(names.length)];
  }

  /**
   * Attempt to get a new personal name for a castable of given type. An effort
   * is made to ensure the name has not previously been used in the current game.
   *
   * @param clazz type of castable to get name for
   * @return name to use
   */
  public static String getPersonalName(final Class<? extends Castable> clazz) {
    int tries = 0;
    String name;
    do {
      name = selectPersonalName(clazz);
    } while (++tries < MAX_TRIES && USED.contains(name));
    USED.add(name);
    return name;
  }
}
