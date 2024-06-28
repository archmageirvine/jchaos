package chaos;

import java.io.IOException;
import java.io.Serializable;

import chaos.board.CastMaster;
import chaos.board.World;
import chaos.board.WorldUtils;
import chaos.common.CastableList;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.wizard.Wizard;
import chaos.engine.AiEngine;
import chaos.engine.EngineFactory;
import chaos.engine.PlayerEngine;
import chaos.graphics.TextScoreDisplay;
import chaos.selector.Selector;
import chaos.selector.SelectorFactory;
import chaos.sound.Sound;
import irvine.util.MaximumSeparation;

/**
 * Experimental modes.  These are used to collecting training data for the AI.
 * @author Sean A. Irvine
 */
public final class ChaosExperimental implements Serializable {

  private ChaosExperimental() {
  }

  static void runExperimentalMode(final Chaos chaos, final String engineDescription, final String selectorDescription, final int turnLimit, final boolean verbose) throws IOException {
    // Note this test method does obey -W and -H parameters
    final World w = chaos.getWorld();
    //System.err.println(w.height() + "*" + w.width());
    final String[] engines = engineDescription.split(",");
    final String[] selectors = selectorDescription.split(",");
    Sound.getSoundEngine().setSoundLevel(Sound.SOUND_NONE);
    final int players = Math.max(engines.length, selectors.length);
    final CastMaster castMaster = chaos.getCastMaster();
    for (int i = 0; i < players; ++i) {
      final Wizard wiz = w.getWizardManager().getWizard(i + 1);
      final PlayerEngine engine = EngineFactory.createEngine("chaos.engine." + engines[engines.length > i ? i : 0] + "Engine", w, chaos.getMoveMaster(), castMaster, 3);
      final Selector selector = SelectorFactory.createSelector("chaos.selector." + selectors[selectors.length > i ? i : 0], wiz, w, castMaster);
      wiz.setCastableList(new CastableList(100, 100, 24));
      wiz.setState(State.ACTIVE);
      wiz.setPlayerEngine(engine);
      wiz.setSelector(selector);
    }
    // Compute initial position of wizards and place them on the board
    final int[][] pos = MaximumSeparation.separate(w.width(), w.height(), players, 10);
    for (int i = 0; i < players; ++i) {
      final Wizard wiz = w.getWizardManager().getWizard(i + 1);
      w.getCell(pos[0][i], pos[1][i]).push(wiz);
    }
    if (verbose) {
      chaos.setScoreDisplay(new TextScoreDisplay(w, w.getWizardManager().getWizards(), w.getWizardManager().getIndependent()));
    }
    chaos.playChaos(null, turnLimit);
    chaos.printFinalScores();
  }

  static void runSpecialMode(final Chaos chaos, final int turnLimit, final int wizards, final int generators, final boolean leech) throws IOException {
    final World world = chaos.getWorld();
    for (int k = 1; k <= wizards; ++k) {
      final Wizard w = world.getWizardManager().getWizard(k);
      w.setCastableList(new CastableList(100, 50, 24));
      w.setState(State.ACTIVE);
      w.setPlayerEngine(new AiEngine(world, chaos.getMoveMaster(), chaos.getCastMaster()));
      w.setSelector(SelectorFactory.randomSelector(w, world, chaos.getCastMaster()));
      if (leech) {
        w.set(PowerUps.LIFE_LEECH, 1);
      }
    }
    // Compute initial position of wizards and place them on the board
    final int[][] pos = MaximumSeparation.separate(world.width(), world.height(), 10, 10);
    for (int i = 0; i < wizards; ++i) {
      final Wizard w = world.getWizardManager().getWizard(i + 1);
      world.getCell(pos[0][i], pos[1][i]).push(w);
    }
    WorldUtils.insertGenerators(world, generators);
    chaos.playChaos(null, turnLimit);
    chaos.printFinalScores();
  }
}

