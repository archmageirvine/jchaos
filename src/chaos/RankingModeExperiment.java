package chaos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import chaos.board.CastMaster;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.CastableList;
import chaos.common.Cat;
import chaos.common.FrequencyTable;
import chaos.common.Named;
import chaos.common.State;
import chaos.common.inanimate.FenceVertical;
import chaos.common.inanimate.Nuked;
import chaos.common.inanimate.PowerWallHorizontal;
import chaos.common.inanimate.PowerWallVertical;
import chaos.common.monster.Solar;
import chaos.common.wizard.Wizard;
import chaos.common.wizard.Wizard1;
import chaos.engine.PlayerEngine;
import chaos.selector.Strategiser;
import chaos.sound.Sound;
import chaos.util.ChaosProperties;
import chaos.util.CombatUtils;
import chaos.util.MovementUtils;
import chaos.util.TeamUtils;
import irvine.math.r.DoubleUtils;
import irvine.util.Pair;
import irvine.util.array.Sort;
import irvine.util.io.IOUtils;

/**
 * Run a series of experimental games to rank all actors. The idea in this ranking is
 * to perform a series of one-on-one fights and see who wins.  Two kinds of fights are
 * done. In the first, the actors are in a 3-by-4 world and start adjacent to each other
 * in the middle. In the second, the actors are in a 3-by-5 world, and are one cell
 * apart and every other cell is a nuked square. In both cases, the leftmost actor
 * get first attack.
 * @author Sean A. Irvine
 */
public final class RankingModeExperiment {

  private RankingModeExperiment() { }

  private static final int MAX_FIGHT_TURNS = 20;
  private static final int RUNS = 5;

  // Need my own implementation of this to get 0-probability actors
  static List<Class<? extends Castable>> getCastables() throws IOException {
    final List<Class<? extends Castable>> castables = new ArrayList<>();
    try (BufferedReader is = IOUtils.reader(ChaosProperties.properties().getProperty("chaos.frequency.file", "chaos/resources/frequency.txt"))) {
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
            final Castable c1 = FrequencyTable.instantiate(clazz);
            if (c1 instanceof Actor) {
              if (c1 instanceof Wizard && !(c1 instanceof Wizard1)) {
                continue; // only keep 1 wizard for this ranking (they should all be the same)
              }
              if (c1 instanceof PowerWallHorizontal || c1 instanceof PowerWallVertical || c1 instanceof FenceVertical) {
                continue; // only keep 1 representative of these things as well
              }
              castables.add(clazz);
            }
          }
        }
      }
    }
    return castables;
  }

  private static void playOneRound(final Chaos chaos, final World world) {
    TeamUtils.smashTeams(world);
    for (final Wizard w : world.getWizardManager().getWizards()) {
      if (w != null && w.getState() == State.ACTIVE && world.getCell(w) != null) {
        w.bonusSelect();
        w.select(false);
      }
    }
    chaos.doCasting();
    chaos.doPostCastUpdate(null);
    chaos.doMovement(null);
    final int catLordOwner = world.isCatLordAlive();
    if (catLordOwner != Actor.OWNER_NONE) {
      final Wizard w = world.getWizardManager().getWizard(catLordOwner);
      MovementUtils.clearMovement(world, Cat.class, catLordOwner);
      chaos.getMoveMaster().dropAttention(w);
      chaos.getAI().moveAll(w);
    }
    // Mark all solars unmoved before trying to move them, cannot do in the
    // update loop because then they can move more than once.
    MovementUtils.clearMovement(world, Solar.class, -1);
    for (final Wizard w : world.getWizardManager().getWizards()) {
      if (w != null) {
        chaos.getAI().moveAll(w);
      }
    }
    chaos.getAI().moveAll(world.getWizardManager().getIndependent());
    CombatUtils.performSpecialCombat(world, chaos.getMoveMaster());
  }

  private static boolean isAlive(final World world, final int player) {
    for (int k = 0; k < world.size(); ++k) {
      final Actor a = world.actor(k);
      if (a != null && a.getOwner() == player && a.getState() == State.ACTIVE) {
        return true;
      }
    }
    return false;
  }

  private static int scoreTheFight(final Chaos chaos, final World world) {
    for (int k = MAX_FIGHT_TURNS; k > 0; --k) {
      playOneRound(chaos, world);
      final boolean firstAlive = isAlive(world, 1);
      final boolean secondAlive = isAlive(world, 2);
      if (!firstAlive && secondAlive) {
        return -k;
      } else if (firstAlive && !secondAlive) {
        return k;
      }
    }
    return 0; // draw
  }

  private static void setUpWizard(final Wizard wiz, final World world, final PlayerEngine engine, final CastMaster castMaster) {
    wiz.setState(State.ACTIVE);
    wiz.setPlayerEngine(engine);
    wiz.setCastableList(new CastableList(100, 24, 24));
    wiz.setSelector(new Strategiser(wiz, world, castMaster));
  }

  private static Wizard makeWizard(final Chaos chaos, final World world, final int k) {
    final Wizard wiz = world.getWizardManager().getWizard(k);
    setUpWizard(wiz, world, chaos.getAI(), chaos.getCastMaster());
    return wiz;
  }

  private static void addScore(final Map<Class<? extends Castable>, Integer> scores, final Actor actor, final int score) {
    final Class<? extends Castable> clazz = actor.getClass();
    final Integer sc = scores.get(clazz);
    scores.put(clazz, (sc == null ? 0 : sc) + score);
  }

  private static void addPairScore(final Map<Pair<Class<? extends Castable>, Class<? extends Castable>>, Integer> scores, final Actor first, final Actor second, final int score) {
    final Pair<Class<? extends Castable>, Class<? extends Castable>> key = new Pair<>(first.getClass(), second.getClass());
    final Integer sc = scores.get(key);
    scores.put(key, (sc == null ? 0 : sc) + score);
  }

  static Chaos adjacentConfiguration(final Actor first, final Actor second) {
    final Chaos chaos = new Chaos(new Configuration(-1, -1, true, 3, 4), false);
    final World world = chaos.getWorld();
    world.getCell(5).push(first);
    world.getCell(6).push(second);
    return chaos;
  }

  static Chaos radioactiveConfiguration(final Actor first, final Actor second) {
    final Chaos chaos = new Chaos(new Configuration(-1, -1, true, 3, 5), false);
    final World world = chaos.getWorld();
    for (int k = 0; k < world.size(); ++k) {
      final Nuked n = new Nuked();
      n.setOwner(-1);
      world.getCell(k).push(n);
    }
    world.getCell(6).pop();
    world.getCell(8).pop();
    world.getCell(6).push(first);
    world.getCell(8).push(second);
    return chaos;
  }

  static String getColor(final int sc) {
    if (sc < 0) {
      return Integer.toHexString(255 * -sc / 200) + "0000";
    } else {
      final String c = Integer.toHexString(255 * sc / 200);
      return "00" + (c.length() == 1 ? "0" + c : c) + "00";
    }
  }

  static String htmlName(final Class<? extends Castable> clazz) {
    final Castable c = FrequencyTable.instantiate(clazz);
    if (c instanceof Wizard) {
      return "Wizard";
    }
    return (c instanceof Named ? "Named&nbsp;" : "") + c.getName().replace(" ", "&nbsp;");
  }

  private static void dumpHeader(final PrintStream out) {
    out.println("<head><style type=\"text/css\">");
    out.println("table.my-spacing {\nborder-spacing:0;\nborder-collapse: collapse;\n}");
    out.println("th.rotate {\nheight: 140px;\nwhite-space: nowrap;\n}");
    out.println("th.rotate > div {\ntransform:\ntranslate(0px, 60px)\nrotate(315deg);\nwidth: 20px;\n}");
    out.println("th.rotate > div > span {\npadding: 5px 10px;\n}\n");
    out.println("</style></head>");
  }

  static void runRankingMode() throws IOException {
    final List<Class<? extends Castable>> castables = getCastables();
    Sound.getSoundEngine().setSoundLevel(Sound.SOUND_NONE);
    final HashMap<Class<? extends Castable>, Integer> totalScores = new HashMap<>();
    final HashMap<Pair<Class<? extends Castable>, Class<? extends Castable>>, Integer> pairScores = new HashMap<>();
    for (int k = 0; k < 2 * RUNS; ++k) {
      for (final Class<? extends Castable> clazz1 : castables) {
        for (final Class<? extends Castable> clazz2 : castables) {
          final Actor first = (Actor) FrequencyTable.instantiate(clazz1);
          final Actor second = (Actor) FrequencyTable.instantiate(clazz2);
          first.setState(State.ACTIVE);
          second.setState(State.ACTIVE);
          first.setOwner(1);
          second.setOwner(2);
          final Chaos chaos = (k & 1) == 0 ? adjacentConfiguration(first, second) : radioactiveConfiguration(first, second);
          final World world = chaos.getWorld();
          // If either of the combatants is not a wizard, we create an off-screen wizard for them
          if (first instanceof Wizard) {
            setUpWizard((Wizard) first, world, chaos.getAI(), chaos.getCastMaster());
            world.getWizardManager().setWizard(1, (Wizard) first);
          } else {
            makeWizard(chaos, world, 1);
          }
          if (second instanceof Wizard) {
            setUpWizard((Wizard) second, world, chaos.getAI(), chaos.getCastMaster());
            world.getWizardManager().setWizard(2, (Wizard) second);
          } else {
            makeWizard(chaos, world, 2);
          }
          final int score = scoreTheFight(chaos, world);
          addPairScore(pairScores, first, second, score);
          addScore(totalScores, first, score);
          addScore(totalScores, second, -score);
        }
      }
    }
    final double[] sc = new double[totalScores.size()];
    @SuppressWarnings("unchecked")
    final Class<? extends Castable>[] classes = (Class<? extends Castable>[]) new Class<?>[totalScores.size()];
    int k = 0;
    for (final Map.Entry<Class<? extends Castable>, Integer> e : totalScores.entrySet()) {
      sc[k] = e.getValue() / (2.0 * RUNS * totalScores.size());
      classes[k++] = e.getKey();
    }
    Sort.sort(sc, classes);

    // Dump huge HTML table, overall ranking by total strength
    System.out.println("<html>");
    dumpHeader(System.out);
    System.out.print("<body><table class=\"my-spacing\">");
    // Header row
    final StringBuilder header = new StringBuilder("<tr><th></th>");
    for (int i = sc.length; --i >= 0;) {
      final Class<? extends Castable> colClass = classes[i];
      final String colName = htmlName(colClass);
      header.append("<th class=\"rotate\"><div><span>").append(colName).append("</span></div></th>");
    }
    header.append("</tr>");
    System.out.println(header);
    // Data rows
    for (int j = sc.length; --j >= 0;) {
      final Class<? extends Castable> rowClass = classes[j];
      final String rowName = htmlName(rowClass);
      final StringBuilder row = new StringBuilder("<tr><th align=\"right\">").append(rowName).append("</th>");
      for (int i = sc.length; --i >= 0;) {
        final Class<? extends Castable> colClass = classes[i];
        row.append("<td bgcolor=\"#")
          .append(getColor(pairScores.get(new Pair<Class<? extends Castable>, Class<? extends Castable>>(rowClass, colClass))))
          .append("\"></td>");
      }
      row.append("<td>").append(DoubleUtils.NF2.format(sc[j])).append("</td></tr>");
      System.out.println(row.toString());
    }
    System.out.println("</table>");
    System.out.println("</body></html>");
  }
}
