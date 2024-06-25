package chaos.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.StringTokenizer;

import chaos.engine.Weight;
import irvine.math.r.DoubleUtils;
import irvine.util.io.IOUtils;

/**
 * Compute a rating for each actor.
 * @author Sean A. Irvine
 */
public final class Lethality {

  private Lethality() {
  }

  private static final HashSet<Class<? extends Castable>> CASTABLES = new HashSet<>();

  static {
    try {
      try (final BufferedReader is = IOUtils.reader("chaos/resources/frequency.txt")) {
        String line;
        while ((line = is.readLine()) != null) {
          if (!line.isEmpty() && line.charAt(0) != '#') {
            final StringTokenizer st = new StringTokenizer(line);
            if (st.hasMoreTokens()) {
              final Class<? extends Castable> clazz;
              final String c = st.nextToken();
              try {
                clazz = Class.forName(c).asSubclass(Castable.class);
              } catch (final Exception e) {
                throw new RuntimeException(e);
              }
              CASTABLES.add(clazz);
            }
          }
        }
      }
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Compute for each actor.
   * @param args ignored
   */
  public static void main(final String[] args) {
    for (final Class<? extends Castable> clazz : CASTABLES) {
      final Castable c = FrequencyTable.instantiate(clazz);
      if (c instanceof Actor) {
        final Actor a = (Actor) c;
        a.setState(State.ACTIVE);
        final int totalCombat = a.get(Attribute.COMBAT) + a.get(Attribute.RANGED_COMBAT) + a.get(Attribute.SPECIAL_COMBAT);
        System.out.println(DoubleUtils.NF2.format(Weight.lethality(a)) + "\t" + a.getName() + "\t" + Math.abs(FrequencyTable.DEFAULT.getFrequency(a.getClass())) + "\t" + totalCombat + "\t" + a.get(Attribute.LIFE));
      }
    }
  }
}
