package chaos.engine;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

import chaos.Chaos;
import chaos.board.CastMaster;
import chaos.board.Cell;
import chaos.board.MoveMaster;
import chaos.board.Team;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Conveyance;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.TargetFilter;
import chaos.common.dragon.GoldenDragon;
import chaos.common.free.Arborist;
import chaos.common.growth.GooeyBlob;
import chaos.common.inanimate.MagicCastle;
import chaos.common.inanimate.MagicWood;
import chaos.common.inanimate.Nuked;
import chaos.common.inanimate.Pit;
import chaos.common.inanimate.ShadowCity;
import chaos.common.inanimate.StandardWall;
import chaos.common.monster.Demon;
import chaos.common.monster.Horse;
import chaos.common.monster.Iridium;
import chaos.common.monster.Lion;
import chaos.common.monster.Orc;
import chaos.common.monster.Vampire;
import chaos.common.spell.Armour;
import chaos.common.wizard.Wizard;
import chaos.common.wizard.Wizard1;
import chaos.common.wizard.Wizard2;
import chaos.util.Event;
import chaos.util.EventListener;
import chaos.util.MovementUtils;
import chaos.util.TextEvent;
import irvine.util.io.IOUtils;
import junit.framework.TestCase;

/**
 * Test the corresponding class.
 * @author Sean A. Irvine
 */
public class AiEngineTest extends TestCase {

  private static class MyListener implements EventListener {
    private final StringBuilder mBuf = new StringBuilder();

    @Override
    public void update(final Event e) {
      if (e instanceof TextEvent) {
        mBuf.append(e);
      }
    }

    String message() {
      return mBuf.toString();
    }
  }

  public void testArmourCast() {
    final Team t = new Team();
    final World w = new World(5, 5, t);
    final MoveMaster mm = new MoveMaster(w);
    final CastMaster cm = new CastMaster(w);
    final AiEngine ai = new AiEngine(w, mm, cm);
    final Wizard1 w1 = new Wizard1();
    w1.setOwner(1);
    w1.setState(State.ACTIVE);
    w.getCell(1).push(w1);
    final Wizard2 w2 = new Wizard2();
    w2.setOwner(2);
    w2.setState(State.ACTIVE);
    w.getCell(2).push(w2);
    final int l = w2.get(Attribute.LIFE);
    assertFalse(t.getTeam(w1.getOwner()) == t.getTeam(w2.getOwner()));
    assertTrue(new Armour() instanceof TargetFilter);
    final MyListener listen = new MyListener();
    w.register(listen);
    for (int i = 0; i < 20; ++i) {
      ai.cast(w1, new Armour(), w.getCell(1));
      w.deregister(listen);
    }
    assertTrue(w1.get(Attribute.LIFE) > w1.getDefault(Attribute.LIFE));
    assertEquals(l, w2.get(Attribute.LIFE));
    assertEquals("Armour [100]", listen.message());
  }

  public void testArboristCast() {
    final World w = new World(1, 1);
    final AiEngine ai = new AiEngine(w, new MoveMaster(w), new CastMaster(w));
    final Wizard1 w1 = new Wizard1();
    w1.setOwner(1);
    w1.setState(State.ACTIVE);
    w.getCell(0).push(w1);
    final MyListener listen = new MyListener();
    w.register(listen);
    ai.cast(w1, new Arborist(), w.getCell(0));
    assertEquals("Arborist [0]", listen.message());
  }

  private HashSet<Castable> getCastables() throws IOException {
    final HashSet<Castable> set = new HashSet<>();
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
              set.add((Castable) Class.forName(clazz).getDeclaredConstructor().newInstance());
            } catch (final ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
              // too bad
            }
          }
        }
      }
    }
    return set;
  }

  public void testMissingTargetting() throws IOException {
    final Team t = new Team();
    final World w = new World(5, 5, t);
    final MoveMaster mm = new MoveMaster(w);
    final CastMaster cm = new CastMaster(w);
    final AiEngine ai = new AiEngine(w, mm, cm);
    ai.moveAll(null); // test a previous bug
    final Wizard1 w1 = new Wizard1();
    w1.setOwner(1);
    w1.setState(State.ACTIVE);
    final Cell cell = w.getCell(1);
    cell.push(w1);
    final PrintStream oldErr = System.err;
    try {
      final ByteArrayOutputStream output = new ByteArrayOutputStream();
      System.setErr(new PrintStream(output));
      try {
        for (final Castable c : getCastables()) {
          ai.cast(w1, c, cell);
        }
      } finally {
        output.close();
        final String s = output.toString();
        assertTrue(s, s.isEmpty());
      }
    } finally {
      System.setErr(oldErr);
    }
  }

  public void checkAIMounts(final Actor a, final boolean ride) {
    final Team t = new Team();
    final World w = new World(5, 5, t);
    final MoveMaster mm = new MoveMaster(w);
    final CastMaster cm = new CastMaster(w);
    final AiEngine ai = new AiEngine(w, mm, cm);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(5);
    wiz.set(PowerUps.RIDE, ride ? 1 : 0);
    w.getCell(0).push(wiz);
    a.setOwner(5);
    w.getCell(1).push(a);
    ai.moveAll(wiz);
    assertEquals(wiz, ((Conveyance) a).getMount());
  }

  public void testAIMounts1() {
    checkAIMounts(new Horse(), false);
    checkAIMounts(new Horse(), true);
    checkAIMounts(new ShadowCity(), false);
    checkAIMounts(new GoldenDragon(), true);
  }

  public void testDugOutMoves() {
    final Team t = new Team();
    final World w = new World(5, 5, t);
    final MoveMaster mm = new MoveMaster(w);
    final CastMaster cm = new CastMaster(w);
    final AiEngine ai = new AiEngine(w, mm, cm);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(5);
    w.getCell(3).push(wiz);
    final GooeyBlob b = new GooeyBlob();
    b.setOwner(1);
    b.set(Attribute.LIFE, 1);
    final Vampire v = new Vampire();
    v.setOwner(5);
    final Vampire vBuried = new Vampire();
    vBuried.setOwner(5);
    w.getCell(1).push(v);
    w.getCell(0).push(vBuried);
    w.getCell(0).push(b);
    ai.moveAll(wiz);
    assertTrue(vBuried.isMoved());
  }

  public void testShootFromConveyance() {
    final Team t = new Team();
    final World w = new World(5, 5, t);
    final MoveMaster mm = new MoveMaster(w);
    final CastMaster cm = new CastMaster(w);
    final AiEngine ai = new AiEngine(w, mm, cm);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(5);
    wiz.increment(Attribute.RANGE, 10);
    wiz.increment(Attribute.RANGED_COMBAT, 20);
    wiz.increment(Attribute.SHOTS, 1);
    final Horse h = new Horse();
    h.setOwner(5);
    h.setMount(wiz);
    w.getCell(3).push(h);
    final Orc o = new Orc();
    w.getCell(0).push(o);
    ai.moveAll(wiz);
    assertEquals(State.DEAD, o.getState());
    assertEquals(1, wiz.getShotsMade());
  }

  public void testShoot1() {
    final Team t = new Team();
    final World w = new World(2, 2, t);
    final MoveMaster mm = new MoveMaster(w);
    final CastMaster cm = new CastMaster(w);
    final AiEngine ai = new AiEngine(w, mm, cm);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(5);
    wiz.increment(Attribute.RANGE, 10);
    wiz.increment(Attribute.RANGED_COMBAT, 20);
    wiz.increment(Attribute.SHOTS, 1);
    wiz.decrement(Attribute.COMBAT, 1);
    w.getCell(0).push(wiz);
    w.getCell(1).push(new Pit());
    w.getCell(2).push(new Nuked());
    final Orc o = new Orc();
    w.getCell(3).push(o);
    ai.moveAll(wiz);
    assertEquals(State.DEAD, o.getState());
    assertEquals(1, wiz.getShotsMade());
  }

  public void testShoot2() {
    final Team t = new Team();
    final World w = new World(2, 2, t);
    final MoveMaster mm = new MoveMaster(w);
    final CastMaster cm = new CastMaster(w);
    final AiEngine ai = new AiEngine(w, mm, cm);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(5);
    wiz.increment(Attribute.RANGE, 10);
    wiz.increment(Attribute.RANGED_COMBAT, 20);
    wiz.increment(Attribute.SHOTS, 1);
    wiz.decrement(Attribute.COMBAT, 1);
    w.getCell(0).push(wiz);
    final GoldenDragon g = new GoldenDragon();
    g.setState(State.DEAD);
    w.getCell(1).push(g);
    final Orc o = new Orc();
    w.getCell(3).push(o);
    ai.moveAll(wiz);
    assertEquals(State.DEAD, o.getState());
    assertEquals(1, wiz.getShotsMade());
  }

  public void testShoot3() {
    final Team t = new Team();
    final World w = new World(1, 1, t);
    final MoveMaster mm = new MoveMaster(w);
    final CastMaster cm = new CastMaster(w);
    final AiEngine ai = new AiEngine(w, mm, cm);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(5);
    wiz.increment(Attribute.RANGE, 10);
    wiz.increment(Attribute.RANGED_COMBAT, 20);
    w.getCell(0).push(wiz);
    ai.moveAll(wiz);
    assertEquals(0, wiz.getShotsMade());
    assertEquals(wiz.getDefault(Attribute.LIFE), wiz.get(Attribute.LIFE));
  }

  public void testShoot4() {
    final Team t = new Team();
    final World w = new World(1, 3, t);
    final MoveMaster mm = new MoveMaster(w);
    final CastMaster cm = new CastMaster(w);
    final AiEngine ai = new AiEngine(w, mm, cm);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(5);
    wiz.increment(Attribute.RANGE, 10);
    wiz.increment(Attribute.RANGED_COMBAT, 20);
    wiz.increment(Attribute.SHOTS, 1);
    w.getCell(0).push(wiz);
    w.getCell(1).push(new StandardWall());
    final Orc o = new Orc();
    w.getCell(2).push(o);
    ai.moveAll(wiz);
    assertEquals(1, wiz.getShotsMade());
    assertEquals(State.ACTIVE, o.getState());
    wiz.resetShotsMade();
    wiz.set(PowerUps.ARCHERY, 1);
    ai.moveAll(wiz);
    assertEquals(1, wiz.getShotsMade());
    assertEquals(State.DEAD, o.getState());
  }

  public void testShootRangeLimit() {
    final Team t = new Team();
    final World w = new World(3, 3, t);
    final MoveMaster mm = new MoveMaster(w);
    final CastMaster cm = new CastMaster(w);
    final AiEngine ai = new AiEngine(w, mm, cm);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(5);
    wiz.increment(Attribute.RANGE, 2);
    wiz.increment(Attribute.RANGED_COMBAT, 20);
    wiz.increment(Attribute.SHOTS, 1);
    wiz.decrement(Attribute.MOVEMENT, 1);
    w.getCell(0).push(wiz);
    w.getCell(1).push(new StandardWall());
    final Orc o = new Orc();
    w.getCell(w.size() - 1).push(o);
    ai.moveAll(wiz);
    assertEquals(State.ACTIVE, o.getState());
    wiz.increment(Attribute.RANGE, 1);
    wiz.resetShotsMade();
    wiz.setMoved(false);
    ai.moveAll(wiz);
    assertEquals(State.DEAD, o.getState());
  }

  public void testWizardMovesFirst() {
    // World is: pit orc wizard castle
    // The wizard should move first and get in the castle
    // Then the orc should move away from the pit
    final Team t = new Team();
    final World w = new World(4, 1, t);
    final MoveMaster mm = new MoveMaster(w);
    final CastMaster cm = new CastMaster(w);
    final AiEngine ai = new AiEngine(w, mm, cm);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(5);
    w.getCell(2).push(wiz);
    final Orc o = new Orc();
    o.setOwner(5);
    w.getCell(1).push(o);
    w.getCell(0).push(new Pit());
    final MagicCastle castle = new MagicCastle();
    castle.setOwner(5);
    w.getCell(3).push(castle);
    ai.moveAll(wiz);
    assertEquals(wiz, castle.getMount());
    assertEquals(o, w.actor(2));
  }

  public void checkShootingPreference(final Actor a, final Actor b) {
    final Team t = new Team();
    final World w = new World(1, 3, t);
    final MoveMaster mm = new MoveMaster(w);
    final CastMaster cm = new CastMaster(w);
    final AiEngine ai = new AiEngine(w, mm, cm);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(5);
    wiz.increment(Attribute.RANGE, 10);
    wiz.set(Attribute.RANGED_COMBAT, Attribute.RANGED_COMBAT.max());
    wiz.set(Attribute.SHOTS, 1);
    wiz.decrement(Attribute.MOVEMENT, 1);
    w.getCell(1).push(wiz);
    a.setState(State.ACTIVE);
    a.setOwner(1);
    w.getCell(0).push(a);
    if (b != null) {
      w.getCell(2).push(b);
    }
    ai.moveAll(wiz);
    assertEquals(1, wiz.getShotsMade());
    final Actor v = w.actor(0);
    if (v != null) {
      assertEquals(State.DEAD, a.getState());
      assertEquals(a, v);
    }
  }

  public void testShootingPreference() {
    checkShootingPreference(new Wizard1(), new Lion());
    checkShootingPreference(new Lion(), null);
    checkShootingPreference(new Lion(), new Orc());
    checkShootingPreference(new GooeyBlob(), new Orc());
    checkShootingPreference(new GooeyBlob(), null);
    checkShootingPreference(new GooeyBlob(), new StandardWall());
    checkShootingPreference(new StandardWall(), null);
    for (int k = 0; k < 5; ++k) {
      final Orc o = new Orc();
      o.set(Attribute.LIFE, 99);
      checkShootingPreference(o, new StandardWall());
    }
  }

  public void testBalancedShot() {
    int left = 0;
    for (int k = 0; k < 100; ++k) {
      final Team t = new Team();
      final World w = new World(1, 3, t);
      final MoveMaster mm = new MoveMaster(w);
      final CastMaster cm = new CastMaster(w);
      final AiEngine ai = new AiEngine(w, mm, cm);
      final Wizard1 wiz = new Wizard1();
      wiz.setState(State.ACTIVE);
      wiz.setOwner(5);
      wiz.increment(Attribute.RANGE, 10);
      wiz.increment(Attribute.RANGED_COMBAT, 100);
      wiz.set(Attribute.SHOTS, 1);
      wiz.decrement(Attribute.MOVEMENT, 1);
      w.getCell(1).push(wiz);
      final Orc a = new Orc();
      a.setState(State.ACTIVE);
      a.setOwner(1);
      w.getCell(0).push(a);
      final Orc b = new Orc();
      b.setState(State.ACTIVE);
      b.setOwner(1);
      w.getCell(2).push(b);
      ai.moveAll(wiz);
      assertEquals(1, wiz.getShotsMade());
      if (a.getState() == State.DEAD) {
        ++left;
        assertEquals(State.ACTIVE, b.getState());
      } else {
        assertEquals(State.ACTIVE, a.getState());
        assertEquals(State.DEAD, b.getState());
      }
    }
    assertTrue(left > 20 && left < 80);
  }

  public void testMountToMeditation() {
    final Team t = new Team();
    final World w = new World(5, 5, t);
    final MoveMaster mm = new MoveMaster(w);
    final CastMaster cm = new CastMaster(w);
    final AiEngine ai = new AiEngine(w, mm, cm);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(5);
    final Horse h = new Horse();
    h.setOwner(5);
    h.setMount(wiz);
    w.getCell(0).push(h);
    final MagicWood mw = new MagicWood();
    mw.setOwner(5);
    w.getCell(1).push(mw);
    ai.moveAll(wiz);
    assertEquals(wiz, mw.getMount());
    assertNull(h.getMount());
  }

  public void testMoveAwayFromGrowthsOneCell() {
    final Team t = new Team();
    final World w = new World(1, 5, t);
    final MoveMaster mm = new MoveMaster(w);
    final CastMaster cm = new CastMaster(w);
    final AiEngine ai = new AiEngine(w, mm, cm);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(5);
    w.getCell(1).push(wiz);
    final GooeyBlob blob = new GooeyBlob();
    blob.setOwner(1);
    w.getCell(0).push(blob);
    ai.moveAll(wiz);
    assertNull(w.actor(1));
    assertEquals(wiz, w.actor(2));
  }

  public void testMountedMoveAwayFromGrowthsOneCell() {
    final Team t = new Team();
    final World w = new World(1, 5, t);
    final MoveMaster mm = new MoveMaster(w);
    final CastMaster cm = new CastMaster(w);
    final AiEngine ai = new AiEngine(w, mm, cm);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(5);
    final Horse h = new Horse();
    h.setOwner(5);
    h.setMount(wiz);
    h.set(Attribute.MOVEMENT, 2);
    w.getCell(1).push(h);
    final GooeyBlob blob = new GooeyBlob();
    blob.setOwner(1);
    w.getCell(0).push(blob);
    ai.moveAll(wiz);
    assertNull(w.actor(1));
    assertEquals(h, w.actor(3));
  }

  public void testMoveAwayFromGrowthsTwoCell() {
    final Team t = new Team();
    final World w = new World(1, 5, t);
    final MoveMaster mm = new MoveMaster(w);
    final CastMaster cm = new CastMaster(w);
    final AiEngine ai = new AiEngine(w, mm, cm);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(5);
    w.getCell(2).push(wiz);
    final GooeyBlob blob = new GooeyBlob();
    blob.setOwner(1);
    w.getCell(0).push(blob);
    ai.moveAll(wiz);
    assertNull(w.actor(2));
    assertEquals(wiz, w.actor(3));
  }

  public void testOrcField() {
    final Team t = new Team();
    final World w = new World(32, 24, t);
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(1);
    // Set up a board with the top left quarter packed with orcs belonging to player 1 and nothing else on the board.
    for (int y = 0; y < 12; ++y) {
      for (int x = 0; x < 16; ++x) {
        final Orc o = new Orc();
        o.setOwner(1);
        w.getCell(x, y).push(o);
      }
    }
    final MoveMaster mm = new MoveMaster(w);
    final CastMaster cm = new CastMaster(w);
    final AiEngine ai = new AiEngine(w, mm, cm);
    // Iterate movement. Orcs should spread out to occupy entire board.
    for (int k = 0; k < 50; ++k) {
      MovementUtils.clearMovement(w, wiz, false);
      ai.moveAll(wiz);
    }
    // More than half of them should be in initially empty region
    int o = 0;
    for (int y = 0; y < 24; ++y) {
      for (int x = 0; x < 32; ++x) {
        if ((y >= 12 || x >= 16) && w.actor(x, y) instanceof Orc) {
          ++o;
        }
      }
    }
    assertTrue(o > (12 * 16) / 2);
    // Now introduce an enemy iridium at the top left.  All the orcs should run back to the top left
    final Iridium ir = new Iridium();
    ir.setOwner(2);
    w.getCell(0).push(ir);
    for (int k = 0; k < 30; ++k) {
      ir.set(Attribute.LIFE, 100);
      MovementUtils.clearMovement(w, wiz, false);
      ai.moveAll(wiz);
    }
    for (int y = 0; y < 24; ++y) {
      for (int x = 0; x < 32; ++x) {
        if (y >= 16 || x >= 20) {
          assertNull(w.actor(x, y));
        }
      }
    }
    // Now introduce a second iridium at the lower right.  Some of orcs should run to attack the new enemy
    final Iridium ir2 = new Iridium();
    ir2.setOwner(2);
    w.getCell(w.size() - 1).push(ir2);
    for (int k = 0; k < 30; ++k) {
      ir.set(Attribute.LIFE, 100);
      ir2.set(Attribute.LIFE, 100);
      MovementUtils.clearMovement(w, wiz, false);
      ai.moveAll(wiz);
    }
    assertTrue(w.actor(w.size() - 2) instanceof Orc);
    assertTrue(w.actor(w.size() - 33) instanceof Orc);
  }

  public void testLazyKeyReflectorHandling() {
    final World w = new World(1, 2, new Team());
    final Wizard wiz1 = new Wizard1();
    wiz1.setState(State.ACTIVE);
    wiz1.setOwner(1);
    wiz1.setPlayerEngine(new HumanEngine(Chaos.getChaos(), 4));
    wiz1.set(Attribute.RANGE, 5);
    wiz1.set(Attribute.RANGED_COMBAT, 5);
    wiz1.set(Attribute.COMBAT, 0);
    wiz1.setMoved(false);
    wiz1.set(Attribute.SHOTS, 1);
    w.getWizardManager().setWizard(1, wiz1);
    w.getCell(0).push(wiz1);
    final Wizard wiz2 = new Wizard2();
    wiz2.setState(State.ACTIVE);
    wiz2.setOwner(2);
    wiz2.set(PowerUps.REFLECT, 1);
    wiz2.set(Attribute.COMBAT, 0);
    w.getWizardManager().setWizard(2, wiz2);
    w.getCell(1).push(wiz2);
    final AiEngine engine = new AiEngine(w, new MoveMaster(w), null);
    engine.moveAll(wiz1);
    assertEquals(wiz2.getDefault(Attribute.LIFE), wiz2.get(Attribute.LIFE));
    assertEquals(wiz1.getDefault(Attribute.LIFE), wiz1.get(Attribute.LIFE));
  }

  public void testWalksIntoPit() {
    final World w = new World(1, 2, new Team());
    final Wizard wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(2);
    final Pit pit = new Pit();
    pit.setOwner(1);
    w.getCell(0).push(pit);
    final Orc orc = new Orc();
    orc.setOwner(2);
    w.getCell(1).push(orc);
    final AiEngine engine = new AiEngine(w, new MoveMaster(w), null);
    for (int k = 0; k < 1000; ++k) {
      orc.setMoved(false);
      engine.moveAll(wiz);
      if (w.actor(0) != pit) {
        return;
      }
    }
    fail();
  }

  public void testWalksIntoPitRate() {
    int c = 0;
    long sk = 0;
    final Wizard wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.setOwner(2);
    for (int j = 0; j < 1000; ++j) {
      final World w = new World(1, 2, new Team());
      final Pit pit = new Pit();
      pit.setOwner(1);
      w.getCell(0).push(pit);
      final Demon demon = new Demon();
      demon.setOwner(2);
      w.getCell(1).push(demon);
      final AiEngine engine = new AiEngine(w, new MoveMaster(w), null);
      for (int k = 0; k < 50; ++k) {
        demon.setMoved(false);
        engine.moveAll(wiz);
        if (w.actor(0) != pit) {
          ++c;
          sk += k;
          break;
        }
      }
    }
    //System.out.println("sk=" + sk + " c="+ c);
    assertTrue(c > 0);
    assertTrue(c < 500);
    assertTrue(sk > 1000);
  }
}
