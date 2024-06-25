package chaos.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * A file backed resource for holding various configuration properties
 * of the game.
 * @author Sean A. Irvine
 */
public final class ChaosProperties extends Properties {

  /** Boolean for whether Texas trade'em is active. */
  public static final String TEXAS_PROPERTY = "chaos.texas";
  /** Boolean for whether to attempt full-screen exclusive mode. */
  public static final String FSEM_PROPERTY = "chaos.full-screen-exclusive";
  /** Boolean for whether combat is capped at 15. */
  public static final String COMBAT_CAP_PROPERTY = "chaos.combat_cap";
  /** Boolean for whether initial ordering of wizards is randomized at start of game. */
  public static final String RANDOMIZE_PLAY_ORDER_PROPERTY = "chaos.randomize-play-order";
  /** Boolean for whether wizards start with life leech. */
  public static final String LIFE_LEECH_PROPERTY = "chaos.wizard-life-leech";
  /** Sound preference. */
  public static final String SOUND_PROPERTY = "chaos.sound-level";

  /** Singleton instance. */
  private static ChaosProperties sProperties = null;

  /**
   * Instantiate the properties.
   */
  private ChaosProperties() {
    try {
      final File f = new File(".chaos");
      if (f.exists()) {
        try (final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f))) {
          load(bis);
        }
      }
    } catch (final IOException e) {
      // too bad
    }
  }

  static synchronized void reset() {
    sProperties = new ChaosProperties();
  }

  /**
   * Retrieve the properties.
   * @return the properties
   */
  public static synchronized ChaosProperties properties() {
    if (sProperties == null) {
      reset();
    }
    return sProperties;
  }

  /** Save the properties. */
  public void save() {
    try {
      final File f = new File(".chaos");
      try (final BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f))) {
        store(bos, "ChaosProperties configuration file");
      }
    } catch (final IOException e) {
      // too bad
    }
  }

  /**
   * Get a property which is supposed to be a double.
   * @param property property name
   * @param defaultValue default value
   * @return resulting value
   */
  public double getDoubleProperty(final String property, final double defaultValue) {
    final String v = getProperty(property);
    return v == null ? defaultValue : Double.parseDouble(v);
  }

  /**
   * Get a property which is supposed to be a integer.
   * @param property property name
   * @param defaultValue default value
   * @return resulting value
   */
  public int getIntProperty(final String property, final int defaultValue) {
    final String v = getProperty(property);
    return v == null ? defaultValue : Integer.parseInt(v);
  }

  /**
   * Get a property which is supposed to be a boolean.
   * @param property property name
   * @param defaultValue default value
   * @return resulting value
   */
  public boolean getBooleanProperty(final String property, final boolean defaultValue) {
    final String v = getProperty(property);
    return v == null ? defaultValue : Boolean.parseBoolean(v);
  }
}
