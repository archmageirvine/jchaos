package chaos;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import chaos.board.CastMaster;
import chaos.board.Cell;
import chaos.board.Team;
import chaos.board.WizardManager;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.CastableList;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard;
import chaos.common.wizard.Wizard1;
import chaos.common.wizard.Wizard2;
import chaos.engine.AiEngine;
import chaos.engine.HumanEngine;
import chaos.engine.PlayerEngine;
import chaos.graphics.MockScreen;
import chaos.graphics.ScoreDisplay;
import chaos.graphics.TextScoreDisplay;
import chaos.scenario.Scenario;
import chaos.scenario.ScenarioUtils;
import chaos.selector.OrdinarySelector;
import chaos.selector.Strategiser;
import chaos.sound.Sound;
import chaos.util.Event;
import chaos.util.EventListener;
import irvine.StandardIoTestCase;
import irvine.TestUtils;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class ChaosTest extends StandardIoTestCase {

  /* Used to simulate an unserializable listener. */
  private static class TestListener implements EventListener {
    @Override
    public void update(final Event event) {
    }
  }

  public void testSerialization() throws Exception {
    final Chaos c = Chaos.getChaos();
    c.mCurrentTurn = 73;
    final World w = Chaos.getChaos().getWorld();
    w.register(new TestListener());
    final Wizard1 wiz1 = new Wizard1();
    wiz1.setState(State.ACTIVE);
    wiz1.setOwner(1);
    wiz1.setCastableList(new CastableList(100, 100, 24));
    wiz1.setPlayerEngine(new AiEngine(w, c.getMoveMaster(), c.getCastMaster()));
    c.getMoveMaster().register(new TestListener());
    wiz1.setSelector(new OrdinarySelector(wiz1, w, c.getCastMaster()));
    w.getCell(0).push(wiz1);
    final Wizard2 wiz2 = new Wizard2();
    wiz2.setState(State.ACTIVE);
    wiz2.setOwner(2);
    wiz2.setCastableList(new CastableList(100, 100, 24));
    wiz2.setPlayerEngine(new HumanEngine(c, 4));
    w.getCell(1).push(wiz2);
    final Lion l = new Lion();
    l.setOwner(Actor.OWNER_INDEPENDENT);
    w.getCell(3).push(l);
    assertEquals(w.getWizardManager().getIndependent(), w.getWizardManager().getWizard(l));
    assertNull(w.getWizardManager().getIndependent().getPlayerEngine());
    // Bug#271 Possible cause of movement bug is that different move masters where surviving
    // across a save game.  So try and move a lion before saving.  But take care to put it
    // back in the expected cell.
    Chaos.getChaos().getAI().moveAll(w.getWizardManager().getIndependent());
    assertTrue(l.isMoved());
    final Cell fix = w.getCell(l);
    w.getCell(3).push(fix.pop());
    try {
      Chaos.saveGame(c, "junitsg");
      Chaos.setChaos(Chaos.loadGame("junitsg"));
      assertNotNull(Chaos.getChaos());
      final World ww = Chaos.getChaos().getWorld();
      assertFalse(ww == w);
      assertTrue(ww.actor(0) instanceof Wizard1);
      final Wizard1 wwiz1 = (Wizard1) ww.actor(0);
      assertEquals(State.ACTIVE, wwiz1.getState());
      assertEquals(1, wwiz1.getOwner());
      assertTrue(wwiz1.getPlayerEngine() instanceof AiEngine);
      final Wizard2 wwiz2 = (Wizard2) ww.actor(1);
      assertEquals(State.ACTIVE, wwiz2.getState());
      assertEquals(2, wwiz2.getOwner());
      assertTrue(wwiz2.getPlayerEngine() instanceof HumanEngine);
      final Actor ll = ww.actor(3);
      assertTrue(ll instanceof Lion);
      assertEquals(Actor.OWNER_INDEPENDENT, ll.getOwner());
      final Wizard indpt = w.getWizardManager().getIndependent();
      assertEquals(indpt, w.getWizardManager().getWizard(ll));
      assertEquals("Independent", indpt.getPersonalName());
      assertNull(indpt.getPlayerEngine());
      final PlayerEngine engine = Chaos.getChaos().getAI();
      assertNotNull(engine);
      final World www = (World) TestUtils.getField("mWorld", engine);
      assertEquals(ww, www);
      // test lion moves
      ll.setMoved(false);
      Chaos.getChaos().getAI().moveAll(indpt);
      assertTrue(ll.isMoved());
      assertEquals(73, Chaos.getChaos().mCurrentTurn);
    } finally {
      assertTrue(new File("junitsg").delete());
    }
    Chaos.setChaos(c); // restore default for later tests
  }

  private void addFiles(final ArrayList<File> files, final File f) {
    if (f.isDirectory()) {
      final File[] flist = f.listFiles();
      if (flist != null) {
        for (final File file : flist) {
          addFiles(files, file);
        }
      }
    } else {
      files.add(f);
    }
  }

  public void testSoundFilesAreCorrectType() throws Exception {
    final File directory;
    try {
      final ClassLoader cld = Thread.currentThread().getContextClassLoader();
      if (cld == null) {
        System.out.println("Skipping sound file types test.");
        return;
      }
      final URL resource = cld.getResource("chaos/resources/sound");
      directory = new File(resource.getFile());
    } catch (final NullPointerException x) {
      System.out.println("Skipping sound file types test.");
      return;
    }
    final ArrayList<File> candidates = new ArrayList<>();
    if (directory.exists()) {
      addFiles(candidates, directory);
    } else {
      System.out.println("Skipping sound file types test.");
      return;
    }
    for (final File f : candidates) {
      final String path = f.getPath();
      if (path.contains("CVS") || path.endsWith(".txt")) {
        continue;
      }
      final String s = f.getPath();
      try (final BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f))) {
        assertEquals(s, 'R', bis.read());
        assertEquals(s, 'I', bis.read());
        assertEquals(s, 'F', bis.read());
        assertEquals(s, 'F', bis.read());
        assertEquals(4, bis.skip(4));
        assertEquals(s, 'W', bis.read());
        assertEquals(s, 'A', bis.read());
        assertEquals(s, 'V', bis.read());
        assertEquals(s, 'E', bis.read());
        assertEquals(s, 'f', bis.read());
        assertEquals(s, 'm', bis.read());
        assertEquals(s, 't', bis.read());
        assertEquals(s, ' ', bis.read());
        final int x = bis.read();
        assertTrue(s, x == 16 || x == 18); // PCM
        assertEquals(s, 0, bis.read());
        assertEquals(s, 0, bis.read());
        assertEquals(s, 0, bis.read());
        assertEquals(s, 1, bis.read()); // PCM
        assertEquals(s, 0, bis.read());
      }
    }
  }

  public void testSerializationWithScenario() throws Exception {
    final Chaos c = Chaos.getChaos();
    c.mScenario = Scenario.load("chaos/scenario/default.dat");
    final World w = Chaos.getChaos().getWorld();
    w.register(new TestListener());
    final Wizard1 wiz1 = new Wizard1();
    wiz1.setState(State.ACTIVE);
    wiz1.setOwner(1);
    wiz1.setCastableList(new CastableList(100, 100, 24));
    wiz1.setPlayerEngine(new AiEngine(w, c.getMoveMaster(), c.getCastMaster()));
    c.getMoveMaster().register(new TestListener());
    wiz1.setSelector(new OrdinarySelector(wiz1, w, c.getCastMaster()));
    assertTrue(ScenarioUtils.update(w, Chaos.getChaos().mScenario));
    w.getCell(0).push(wiz1);
    try {
      Chaos.saveGame(c, "junitsg");
      Chaos.setChaos(Chaos.loadGame("junitsg"));
      assertNotNull(Chaos.getChaos());
      final Scenario scenario = Chaos.getChaos().mScenario;
      assertNotNull(scenario);
      final World ww = Chaos.getChaos().getWorld();
      assertFalse(ww == w);
      assertTrue(ww.actor(0) instanceof Wizard1);
      final Wizard1 wwiz1 = (Wizard1) ww.actor(0);
      assertEquals(State.ACTIVE, wwiz1.getState());
      assertEquals(1, wwiz1.getOwner());
      assertTrue(wwiz1.getPlayerEngine() instanceof AiEngine);
      final World www = (World) TestUtils.getField("mWorld", Chaos.getChaos().getAI());
      assertEquals(ww, www);
      assertTrue(ScenarioUtils.update(ww, scenario));
    } finally {
      assertTrue(new File("junitsg").delete());
    }
    Chaos.setChaos(c); // restore default for later tests
  }

  public void testPlay() throws IOException {
    final Configuration config = new Configuration(2, 2);
    final Chaos chaos = new Chaos(config, true);
    final MockScreen screen = new MockScreen();
    final World world = chaos.getWorld();
    final WizardManager wm = world.getWizardManager();
    final Wizard wiz1 = wm.getWizard(1);
    final Wizard wiz2 = wm.getWizard(2);
    final ScoreDisplay scoreDisplay = new TextScoreDisplay(world, new Wizard[] {wiz1, wiz2}, new Wizard1());
    chaos.playChaos(screen, scoreDisplay, 1);
    assertTrue(getOut().contains("Every wizard is dead"));
    assertEquals("", screen.toString());
    assertEquals(0, chaos.getCurrentTurn());
    Sound.getSoundEngine().setSoundLevel(Sound.SOUND_NONE);
    wiz1.setState(State.ACTIVE);
    wiz2.setState(State.ACTIVE);
    final CastMaster castMaster = chaos.getCastMaster();
    wiz1.setSelector(new Strategiser(wiz1, world, castMaster));
    wiz2.setSelector(new Strategiser(wiz2, world, castMaster));
    wiz1.setCastableList(new CastableList(1, 1, 1));
    wiz2.setCastableList(new CastableList(1, 1, 1));
    wiz1.setPlayerEngine(new AiEngine(world, chaos.getMoveMaster(), castMaster));
    wiz2.setPlayerEngine(new AiEngine(world, chaos.getMoveMaster(), castMaster));
    final Team team = world.getTeamInformation();
    team.separate(1);
    wiz1.increment(PowerUps.FROZEN);
    wiz2.increment(PowerUps.FROZEN);
    chaos.playChaos(screen, scoreDisplay, 1);
    final String s = screen.toString();
    //mOldOut.println(s);
    TestUtils.containsAll(s,
      "writePhase(GENERATORS,null)",
      "writePhase(GROWTH,null)",
      "writePhase(UPDATE,null)",
      "writePhase(MOVEMENT,INDEPENDENTS)",
      "writePhase(MOVEMENT,SOLARS)",
      "writePhase(SPECIAL,COMBAT)",
      "writePhase(SCORES,null)"
    );
    //mOldOut.println(getOut());
    TestUtils.containsAll(getOut(),
      "score",
      "life",
      "ACTIVE",
      "Turn number: 0"
    );
    assertEquals(1, chaos.getCurrentTurn());
  }
}
