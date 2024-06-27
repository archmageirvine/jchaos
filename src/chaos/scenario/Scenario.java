package chaos.scenario;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chaos.Chaos;
import chaos.board.WizardManager;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.CastableList;
import chaos.common.FrequencyTable;
import chaos.common.State;
import chaos.common.inanimate.Exit;
import chaos.common.wizard.Wizard;
import chaos.engine.AiEngine;
import chaos.engine.HumanEngine;
import chaos.graphics.ChaosScreen;
import chaos.graphics.InformationPanel;
import chaos.selector.RandomAiSelector;
import chaos.util.BlockUntilEvent;
import irvine.util.Pair;
import irvine.util.string.StringUtils;

/**
 * Manager for a scenario read from a resource.
 * @author Sean A. Irvine
 */
public final class Scenario implements Serializable {

  private int mLineNumber = 0;
  private final int mWidth;
  private final int mHeight;
  private final List<String> mScenarioData = new ArrayList<>();
  private final Map<String, String> mHeader = new HashMap<>();

  private static void checkVersion(final String line) throws IOException {
    if (!"#Scenario=V1.0".equals(line)) {
      throw new IOException("Invalid scenario version: " + line);
    }
  }

  private static Pair<Integer, Integer> parsePosition(final String wizardDescriptor) {
    final int end = wizardDescriptor.indexOf(')');
    if (wizardDescriptor.length() < 5 || wizardDescriptor.charAt(0) != '(' || end == -1) {
      throw new IllegalArgumentException(wizardDescriptor);
    }
    final int comma = wizardDescriptor.indexOf(',');
    if (comma < 0) {
      throw new IllegalArgumentException(wizardDescriptor);
    }
    final int x = Integer.parseInt(wizardDescriptor.substring(1, comma));
    final int y = Integer.parseInt(wizardDescriptor.substring(comma + 1, end));
    return new Pair<>(x, y);
  }

  private static void updateCastables(final Wizard w, final String wizardDescriptor) {
    final int left = wizardDescriptor.indexOf('[');
    final int right = wizardDescriptor.indexOf(']', left);
    if (left > -1 && right > -1) {
      final String[] castableNames = wizardDescriptor.substring(left + 1, right).split(",");
      if (castableNames.length > 0) {
        if ("?".equals(castableNames[0])) {
          w.setCastableList(new CastableList(100, 100, 24));
          return;
        }
        final CastableList list = w.getCastableList();
        if (!"+".equals(castableNames[0])) {
          list.clear();
        }
        for (final String spell : castableNames) {
          if (!"+".equals(spell)) {
            final Castable castable = FrequencyTable.DEFAULT.getByPartialName(spell);
            if (castable != null) {
              list.add(castable);
            } else {
              StringUtils.message("Unknown in scenario castable list: " + spell);
            }
          }
        }
      }
    }
  }

  private int getWithDefault(final String property, final int defaultValue) {
    final String v = mHeader.get(property);
    return v == null ? defaultValue : Integer.parseInt(v);
  }

  private Scenario(final LineNumberReader reader) throws IOException {
    checkVersion(reader.readLine());
    String line;
    while ((line = reader.readLine()) != null) {
      if (!line.isEmpty()) {
        if (line.charAt(0) == '#') {
          final int eq = line.indexOf('=');
          if (eq >= 0) {
            final String key = line.substring(1, eq);
            final String value = line.substring(eq + 1);
            if (mHeader.put(key, value) != null) {
              throw new IOException(reader.getLineNumber() + ":Multiple definitions of " + key);
            }
          }
        } else {
          mScenarioData.add(line);
        }
      }
    }
    mWidth = getWithDefault("width", -1);
    mHeight = getWithDefault("height", -1);
  }

  /**
   * Start a new scenario from a named resource.
   * @param resource scenario resource
   * @return the scenario
   * @throws IOException if an I/O error occurs.
   */
  public static Scenario load(final String resource) throws IOException {
    try (final LineNumberReader reader = new LineNumberReader(new InputStreamReader(Scenario.class.getClassLoader().getResourceAsStream(resource)))) {
      return new Scenario(reader);
    }
  }

  /**
   * Start a new scenario from a file.
   * @param file scenario file
   * @return the scenario
   * @throws IOException if an I/O error occurs.
   */
  public static Scenario load(final File file) throws IOException {
    try (final LineNumberReader reader = new LineNumberReader(new FileReader(file))) {
      return new Scenario(reader);
    }
  }

  /**
   * Perform initialization for the scenario.  This includes setting up the wizards for the scenario.
   * @param chaos the universe
   */
  public void init(final Chaos chaos, final ChaosScreen screen) {
    chaos.setScenario(this);
    final World world = chaos.getWorld();

    if (getHeight() > 0) {
      importScenario(world);
    }

    final WizardManager wm = world.getWizardManager();
    int wiz = 0;
    String wizardDescriptor;
    while ((wizardDescriptor = mHeader.get("wizard" + ++wiz)) != null) {
      final Pair<Integer, Integer> pos = parsePosition(wizardDescriptor);
      // Negative values indicate distance from right or bottom
      final int x = pos.left() >= 0 ? pos.left() : world.width() - 1 + pos.left();
      final int y = pos.right() >= 0 ? pos.right() : world.height() - 1 + pos.right();
      final Wizard w = wm.getWizard(wiz);
      w.setState(State.ACTIVE);
      if (w.getPlayerEngine() == null) {
        // Only set a player engine if one is not already defined.  This it typically only
        // set once on the first scenario loaded.  This is important because the outside
        // caller is responsible for setting the screen etc. on this engine.
        if (wizardDescriptor.contains("*")) {
          //System.err.println("Turning on AI engine for " + w.getName());
          w.setPlayerEngine(new AiEngine(chaos.getWorld(), chaos.getMoveMaster(), chaos.getCastMaster()));
          w.setSelector(new RandomAiSelector());
        } else {
          w.setPlayerEngine(new HumanEngine(chaos, chaos.getTileManager().getWidthBits()));
        }
        w.setCastableList(new CastableList(100, 0, 24));
      }
      updateCastables(w, wizardDescriptor);
      //System.err.println(w.getCastableList());
      world.getCell(x, y).push(w);
    }
    // Make sure other wizards are disabled
    while (wiz < wm.getMaximumPlayerNumber()) {
      final Wizard wizard = wm.getWizard(wiz);
      if (wizard != null) {
        wizard.setState(State.DEAD);
      }
      ++wiz;
    }

    final String title = mHeader.get("title");
    if (screen != null && title != null) {
      synchronized (screen.lock()) {
        InformationPanel.scenarioTitleDisplay(screen, screen.getGraphics(), title, mHeader.getOrDefault("description", ""));
        BlockUntilEvent.blockUntilEvent(screen, 60000);
      }
    }
  }

  /**
   * Import an entire scenario into the world
   * @param world the world
   */
  public void importScenario(final World world) {
    assert getHeight() > 0;
    // A normal non-scrolling level, push in the entire contents into the world
    for (int y = 0; y < mScenarioData.size(); ++y) {
      final String[] names = mScenarioData.get(y).split(",");
      for (int x = 0; x < Math.min(names.length, world.width()); ++x) {
        insert(world, x, y, names[x].trim());
      }
    }
  }

  private void insert(final World world, final int x, final int y, final String name) {
    if (!name.isEmpty()) {
      final int colon = name.indexOf(':');
      final int owner;
      final String spell;
      if (colon != -1) {
        owner = Integer.parseInt(name.substring(colon + 1));
        spell = name.substring(0, colon);
      } else {
        owner = Actor.OWNER_INDEPENDENT;
        spell = name;
      }
      final Actor c;
      if (spell.startsWith("Exit")) {
        final int start = spell.indexOf('[');
        final int end = spell.indexOf(']', start);
        if (start < 0 || end < 0) {
          throw new RuntimeException("Malformed: " + spell);
        }
        final String next = spell.substring(start + 1, end);
        if (Chaos.isVerbose()) {
          System.err.println("Setting next level to " + next);
        }
        c = next.isEmpty() ? new Exit() : new Exit(next, Exit.ExitType.NO_REAL_ENEMY);
      } else {
        c = (Actor) FrequencyTable.ALL.getByPartialName(spell);
        if (c == null) {
          System.err.println("Skipping unknown: " + spell);
        }
      }
      if (c != null) {
        c.setOwner(owner);
        c.setState(State.ACTIVE);
        world.getCell(x, y).push(c);
      }
    }
  }

  /**
   * Return the width in cells of this scenario.
   * @return width
   */
  public int getWidth() {
    return mWidth;
  }

  /**
   * Return the height in cells of this scenario.
   * @return height
   */
  public int getHeight() {
    return mHeight;
  }

  /**
   * Reads next line from scenario and jams objects into the top row of the world.
   * @param world the world
   */
  public void update(final World world) {
    if (getHeight() == -1) {
      // Vertically scrolling level
      final String[] names = mScenarioData.get(mLineNumber++).split(",");
      for (int x = 0; x < Math.min(names.length, world.width()); ++x) {
        insert(world, x, 0, names[x].trim());
      }
    }
  }

  boolean isValid() {
    return getHeight() > 0 || mLineNumber < mScenarioData.size();
  }

  /**
   * Validate a scenario file.
   * @param args resource name
   * @throws IOException if an I/O error occurs.
   */
  public static void main(final String[] args) throws IOException {
    if (args == null || args.length == 0) {
      System.err.println("Scenario resource");
      return;
    }
    final Scenario s = load(args[0]);
    final int ww = s.getWidth();
    while (s.isValid()) {
      final World w = new World(ww, 1);
      s.update(w);
      for (int k = 0; k < ww; ++k) {
        final Actor a = w.actor(k);
        if (a != null) {
          final String c = a.getClass().getName();
          if (c.startsWith("chaos.common.monster.")) {
            System.out.print("!" + c.substring("chaos.common.monster.".length()));
          } else if (c.startsWith("chaos.common.inanimate.")) {
            System.out.print(c.substring("chaos.common.inanimate.".length()));
          } else {
            System.out.print(c);
          }
        }
        if (k != ww - 1) {
          System.out.print(',');
        }
      }
      System.out.println();
    }
  }
}
