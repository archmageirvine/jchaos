package chaos.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import chaos.common.monster.Horse;
import chaos.common.monster.Thundermare;
import irvine.util.io.IOUtils;

/**
 * Compute the reincarnation tree for the documentation.
 * @author Sean A. Irvine
 */
public final class ReincarnationTree {

  private ReincarnationTree() {
  }

  /** The set of castables. */
  public static final Set<Class<? extends Castable>> CASTABLES;

  static {
    final HashSet<Class<? extends Castable>> castables = new HashSet<>();
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
              castables.add(clazz);
            }
          }
        }
      }
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
    CASTABLES = Collections.unmodifiableSet(castables);
  }

  private static void dumpDotty(final HashMap<Class<? extends Castable>, ArrayList<Class<? extends Castable>>> tree, final Class<? extends Castable> node, final int indent) {
    final Monster m = (Monster) FrequencyTable.instantiate(node);
    final Class<? extends Actor> pc = m instanceof Promotion ? ((Promotion) m).promotion() : null;
    final int pcount = pc == null ? -1 : ((Promotion) m).promotionCount();
    final String name = m.getName();
    final StringBuilder f = new StringBuilder();
    if (m instanceof Named) {
      f.append("Named");
    }
    for (int k = 0; k < name.length(); ++k) {
      final char c = name.charAt(k);
      if (c != ' ' && c != '-') {
        f.append(c);
      }
    }
    System.out.println(name.replace(' ', '_').replace('-', '_') + " [shapefile=\"cicons/" + f.toString() + ".eps\"];");
    final ArrayList<Class<? extends Castable>> list = tree.get(node);
    if (list != null) {
      for (final Class<? extends Castable> n : list) {
        dumpDotty(tree, n, indent + 1);
      }
      boolean pcOk = pc == null;
      for (final Class<? extends Castable> n : list) {
        final String end = FrequencyTable.instantiate(n).getName().replace(' ', '_').replace('-', '_');
        final StringBuilder line = new StringBuilder(end)
          .append(" -> ")
          .append(name.replace(' ', '_').replace('-', '_'));
        // special case of horse-thundermare powerup
        if (Horse.class.equals(node) && Thundermare.class.equals(n)) {
          line.append(" [dir=both label=\"1*\"]");
        } else if (n.equals(pc)) {
          line.append(" [dir=both label=\"")
            .append(pcount)
            .append("\"]");
          pcOk = true;
        }
        System.out.println(line.append(';').toString());
      }
      if (!pcOk) {
        System.err.println("Promotion/Reincarnation cycle failure for: " + name + ":" + pc.getName());
      }
    }
  }

  /**
   * Construct the reincarnation trees.
   * @param args root of tree
   * @throws Exception if an error occurs
   */
  public static void main(final String[] args) throws Exception {

    final Class<? extends Monster> root = Class.forName(args[0]).asSubclass(Monster.class);

    final HashMap<Class<? extends Castable>, ArrayList<Class<? extends Castable>>> tree = new HashMap<>();

    // build tree
    for (final Class<? extends Castable> clazz : CASTABLES) {
      final Castable c = FrequencyTable.instantiate(clazz);
      if (c instanceof Monster) {
        final Monster m = (Monster) c;
        final Class<? extends Monster> reClass = m.reincarnation();
        if (reClass != null) {
          final ArrayList<Class<? extends Castable>> list = tree.computeIfAbsent(reClass, k -> new ArrayList<>());
          list.add(clazz);
        }
      }
    }

    // special check for orphan promotions
    for (final Class<? extends Castable> clazz : CASTABLES) {
      final Castable c = FrequencyTable.instantiate(clazz);
      if (c instanceof Promotion && tree.get(clazz) == null) {
        System.err.println("Promotion/Reincarnation cycle failure for: " + c.getName());
      }
    }

    System.out.println("digraph G {");
    System.out.println("margin=0.25;");
    System.out.println("ranksep=0.35;");
    System.out.println("node [shape=plaintext width=0.2 height=0.2 label=\"\" shape=plaintext style=\"setlinewidth(0.2)\"];");
    System.out.println("edge [arrowsize=0.5 style=\"setlinewidth(0.2)\" shape=plaintext width=0.2 height=0.2 label=\"\" shape=plaintext fontsize=9];");
    dumpDotty(tree, root, 0);
    System.out.println('}');
  }
}
