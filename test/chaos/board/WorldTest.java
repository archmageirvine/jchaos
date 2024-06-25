package chaos.board;

import java.util.Random;
import java.util.Set;

import chaos.common.Attribute;
import chaos.common.State;
import chaos.common.growth.VioletFungi;
import chaos.common.inanimate.MagicWood;
import chaos.common.inanimate.WaspNest;
import chaos.common.monster.Bolter;
import chaos.common.monster.CatLord;
import chaos.common.monster.Lion;
import junit.framework.TestCase;

/**
 * JUnit tests for the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class WorldTest extends TestCase {

  public void testDodgyConstruction() {
    try {
      new World(5, 5, null);
      fail("Accepted null teams");
    } catch (final NullPointerException e) {
      // ok
    }
    final Team t = new Team();
    try {
      new World(-1, 5, t);
      fail("Accepted dodgy width");
    } catch (final IllegalArgumentException e) {
      // ok
    }
    try {
      new World(0, 5, t);
      fail("Accepted dodgy width");
    } catch (final IllegalArgumentException e) {
      // ok
    }
    try {
      new World(5, -1, t);
      fail("Accepted dodgy height");
    } catch (final IllegalArgumentException e) {
      // ok
    }
    try {
      new World(5, 0, t);
      fail("Accepted dodgy height");
    } catch (final IllegalArgumentException e) {
      // ok
    }
  }

  public void testCellNumbering() {
    final World w = new World(5, 5);
    for (int i = 0; i < w.size(); ++i) {
      assertEquals(i, w.getCell(i).getCellNumber());
    }
  }

  public void testBasicOperation() {
    final Team team = new Team();
    final World w = new World(5, 7, team);
    assertEquals(5, w.width());
    assertEquals(7, w.height());
    assertEquals(35, w.size());
    assertNotNull(w.getWizardManager());
    assertEquals(team, w.getTeamInformation());
    // check all cells on board are empty
    for (int x = 0; x < w.width(); ++x) {
      for (int y = 0; y < w.height(); ++y) {
        assertTrue(w.getCell(x, y) != null);
        assertEquals(null, w.actor(x, y));
      }
    }
    for (int i = 0; i < w.size(); ++i) {
      assertTrue(w.getCell(i) != null);
      assertEquals(null, w.actor(i));
    }
    // asking for off board cells should give null
    assertEquals(null, w.actor(-1));
    assertEquals(null, w.actor(36));
    assertEquals(null, w.actor(3006));
    assertEquals(null, w.actor(-1, -1));
    assertEquals(null, w.actor(-1, 3));
    assertEquals(null, w.actor(3, -1));
    assertEquals(null, w.actor(36, 36));
    assertEquals(null, w.actor(9, 2));
    assertEquals(null, w.getCell(-1));
    assertEquals(null, w.getCell(36));
    assertEquals(null, w.getCell(3006));
    assertEquals(null, w.getCell(-1, -1));
    assertEquals(null, w.getCell(-1, 3));
    assertEquals(null, w.getCell(3, -1));
    assertEquals(null, w.getCell(36, 36));
    assertEquals(null, w.getCell(9, 2));
    assertNotNull(w.getWarpSpace());
  }

  public void testUnicity() {
    final World w = new World(5, 7);
    final Cell c = w.getCell(3, 3);
    final Lion lion = new Lion();
    c.push(lion);
    // check all cells on board are empty
    for (int x = 0; x < w.width(); ++x) {
      for (int y = 0; y < w.height(); ++y) {
        if (x != 3 || y != 3) {
          assertEquals(null, w.actor(x, y));
        } else {
          assertEquals(lion, w.actor(x, y));
        }
      }
    }
  }

  public void testEngagement() {
    final World w = new World(5, 7);
    final Lion lion = new Lion();
    lion.setOwner(1);
    final int lionAgility = lion.getDefault(Attribute.AGILITY);
    w.getCell(18).push(lion);

    // empty cells should not be engaged
    assertTrue(!w.checkEngagement(0, false));
    assertTrue(!w.checkEngagement(1, false));
    assertTrue(!w.checkEngagement(2, false));
    assertTrue(!w.checkEngagement(3, false));
    assertTrue(!w.checkEngagement(-1, false));
    assertTrue(!w.checkEngagement(0, true));
    assertTrue(!w.checkEngagement(1, true));
    assertTrue(!w.checkEngagement(2, true));
    assertTrue(!w.checkEngagement(3, true));
    assertTrue(!w.checkEngagement(-1, true));

    // when lion is only thing on board it should not get engaged
    for (int i = 0; i < 10; ++i) {
      if (w.checkEngagement(18, false) || w.checkEngagement(18, true)) {
        fail("Lion was unexpectedly engaged");
      }
    }

    // add an inanimate, should also have no effect
    w.getCell(17).push(new MagicWood());
    for (int i = 0; i < 10; ++i) {
      if (w.checkEngagement(18, false) || w.checkEngagement(18, true)) {
        fail("Lion was unexpectedly engaged");
      }
    }
    for (int i = 0; i < 10; ++i) {
      if (w.checkEngagement(17, false) || w.checkEngagement(17, true)) {
        fail("MagicWood was unexpectedly engaged");
      }
    }
    // add an inanimate, should also have no effect
    w.getCell(17).push(new WaspNest());
    for (int i = 0; i < 10; ++i) {
      if (w.checkEngagement(18, false) || w.checkEngagement(18, true)) {
        fail("Lion was unexpectedly engaged");
      }
    }
    w.getCell(17).pop();

    // lion on left
    w.getCell(17).pop();
    w.getCell(17).push(new Lion());
    // compulsory should now always be engaged
    for (int i = 0; i < 10; ++i) {
      if (!w.checkEngagement(18, true)) {
        fail("Lion was unengaged");
      }
    }
    // other times at expected rate
    int ecount = 0;
    for (int i = 0; i < 200; ++i) {
      if (w.checkEngagement(18, false)) {
        ++ecount;
      }
    }
    ecount = 100 * ecount / 200;
    assertTrue("Lion engagement rate too low", ecount + 20 > lionAgility);
    assertTrue("Lion engagement rate too high", ecount - 20 < lionAgility);

    // lion on right
    w.getCell(17).pop();
    w.getCell(19).push(new Lion());
    // compulsory should now always be engaged
    for (int i = 0; i < 10; ++i) {
      if (!w.checkEngagement(18, true)) {
        fail("Lion was unengaged");
      }
    }
    // other times at expected rate
    ecount = 0;
    for (int i = 0; i < 200; ++i) {
      if (w.checkEngagement(18, false)) {
        ++ecount;
      }
    }
    ecount = 100 * ecount / 200;
    assertTrue("Lion engagement rate too low", ecount + 20 > lionAgility);
    assertTrue("Lion engagement rate too high", ecount - 20 < lionAgility);

    // lion on both sides, should engage twice as often
    w.getCell(17).push(new Lion());
    // compulsory should now always be engaged
    for (int i = 0; i < 10; ++i) {
      if (!w.checkEngagement(18, true)) {
        fail("Lion was unengaged");
      }
    }
    // other times at expected rate
    ecount = 0;
    // longer loop for accurate stats
    for (int i = 0; i < 400; ++i) {
      if (w.checkEngagement(18, false)) {
        ++ecount;
      }
    }
    ecount = 100 * ecount / 800;
    assertTrue("Lion engagement rate too low", ecount + 20 > lionAgility);
    assertTrue("Lion engagement rate too high", ecount - 20 < lionAgility);
  }

  public void testEngagementSleepingDead() {
    final World w = new World(5, 7);
    final Lion lion = new Lion();
    lion.setOwner(1);
    w.getCell(18).push(lion);

    // sleeping lion on left
    final Lion l2 = new Lion();
    l2.setState(State.ASLEEP);
    w.getCell(17).pop();
    w.getCell(17).push(l2);
    for (int i = 0; i < 10; ++i) {
      if (w.checkEngagement(18, false) || w.checkEngagement(18, true)) {
        fail("Lion was engaged");
      }
    }

    // dead lion on left
    l2.setState(State.DEAD);
    for (int i = 0; i < 10; ++i) {
      if (w.checkEngagement(18, false) || w.checkEngagement(18, true)) {
        fail("Lion was engaged");
      }
    }
  }

  public void testEngagementFungi() {
    final World w = new World(5, 7);
    final Lion lion = new Lion();
    lion.setOwner(1);
    w.getCell(18).push(lion);

    // fungi on left
    final VioletFungi l2 = new VioletFungi();
    w.getCell(17).push(l2);
    for (int i = 0; i < 10; ++i) {
      if (w.checkEngagement(18, false) || w.checkEngagement(18, true)) {
        fail("Lion was engaged");
      }
    }
  }

  public void testGetActor() {
    final World w = new World(3, 4);
    final Lion l = new Lion();
    assertNull(w.getCell(l));
    for (int i = 0; i < w.size(); ++i) {
      w.getCell(i).push(l);
      for (int j = 0; j < w.size(); ++j) {
        if (i == j) {
          assertTrue(w.getCell(l) == w.getCell(j));
        } else {
          assertTrue(w.getCell(l) != w.getCell(j));
        }
      }
      w.getCell(i).pop();
    }
  }

  private void checkSet(final Set<Cell> s, final World w) {
    assertTrue(!s.contains(w.getCell(0)));
    assertTrue(!s.contains(w.getCell(1)));
    assertTrue(!s.contains(w.getCell(2)));
    assertTrue(!s.contains(w.getCell(3)));
    assertTrue(!s.contains(w.getCell(4)));
    assertTrue(!s.contains(w.getCell(5)));
    assertTrue(!s.contains(w.getCell(6)));
    assertTrue(!s.contains(w.getCell(7)));
    assertTrue(!s.contains(w.getCell(8)));
    assertTrue(s.contains(w.getCell(9)));
    assertTrue(s.contains(w.getCell(10)));
    assertTrue(s.contains(w.getCell(11)));
    assertTrue(!s.contains(w.getCell(12)));
    assertTrue(!s.contains(w.getCell(13)));
    assertTrue(!s.contains(w.getCell(14)));
    assertTrue(s.contains(w.getCell(15)));
    assertTrue(s.contains(w.getCell(16)));
    assertTrue(s.contains(w.getCell(17)));
    assertTrue(s.contains(w.getCell(18)));
    assertTrue(!s.contains(w.getCell(20)));
    assertTrue(!s.contains(w.getCell(21)));
    assertTrue(s.contains(w.getCell(22)));
    assertTrue(s.contains(w.getCell(23)));
    assertTrue(s.contains(w.getCell(24)));
    assertTrue(s.contains(w.getCell(25)));
    assertTrue(!s.contains(w.getCell(27)));
    assertTrue(!s.contains(w.getCell(28)));
    assertTrue(s.contains(w.getCell(29)));
    assertTrue(s.contains(w.getCell(30)));
    assertTrue(s.contains(w.getCell(31)));
    assertTrue(s.contains(w.getCell(32)));
    assertTrue(!s.contains(w.getCell(34)));
    assertTrue(!s.contains(w.getCell(35)));
    assertTrue(!s.contains(w.getCell(36)));
    assertTrue(s.contains(w.getCell(37)));
    assertTrue(s.contains(w.getCell(38)));
    assertTrue(s.contains(w.getCell(39)));
    assertTrue(!s.contains(w.getCell(40)));
    assertTrue(!s.contains(w.getCell(41)));
    assertTrue(!s.contains(w.getCell(42)));
    assertTrue(!s.contains(w.getCell(43)));
    assertTrue(!s.contains(w.getCell(44)));
    assertTrue(!s.contains(w.getCell(45)));
    assertTrue(!s.contains(w.getCell(46)));
    assertTrue(!s.contains(w.getCell(47)));
    assertTrue(!s.contains(w.getCell(48)));
    assertTrue(!s.contains(w.getCell(49)));
  }

  public void testGetCellSet() {
    /*
      +-+-+-+-+-+-+-+
      | | | | | | | |
      +-+-+-+-+-+-+-+
      | | |#|#|#| | |
      +-+-+-+-+-+-+-+
      | |#|#|#|#|#| |
      +-+-+-+-+-+-+-+
      | |#|#|O|#|#| |
      +-+-+-+-+-+-+-+
      | |#|#|#|#|#| |
      +-+-+-+-+-+-+-+
      | | |#|#|#| | |
      +-+-+-+-+-+-+-+
      | | | | | | | |
      +-+-+-+-+-+-+-+
    */

    final World w = new World(7, 7);
    Set<Cell> s = w.getCells(24, 0, 2, false);
    checkSet(s, w);
    assertTrue(s.contains(w.getCell(19)));
    assertTrue(s.contains(w.getCell(26)));
    assertTrue(s.contains(w.getCell(33)));

    // test centre exclusion, radius = 1
    s = w.getCells(24, 1, 1, false);
    for (int i = 0; i < 49; ++i) {
      if (i == 16 || i == 17 || i == 18 || i == 23
          || i == 25 || i == 30 || i == 31 || i == 32) {
        assertTrue(s.contains(w.getCell(i)));
      } else {
        assertTrue(String.valueOf(i), !s.contains(w.getCell(i)));
      }
    }

    // test top-left
    s = w.getCells(0, 0, 1, false);
    for (int i = 0; i < 49; ++i) {
      if (i == 0 || i == 1 || i == 7 || i == 8) {
        assertTrue(s.contains(w.getCell(i)));
      } else {
        assertTrue(!s.contains(w.getCell(i)));
      }
    }

    // test bottom-right
    s = w.getCells(48, 1, 1, false);
    for (int i = 0; i < 49; ++i) {
      if (i == 47 || i == 41 || i == 40) {
        assertTrue(s.contains(w.getCell(i)));
      } else {
        assertTrue(!s.contains(w.getCell(i)));
      }
    }

    // test bad radius
    try {
      w.getCells(0, -1, 1, false);
      fail("Bad radii");
    } catch (final IllegalArgumentException e) {
      // ok
    }
    try {
      w.getCells(0, 1, 0, false);
      fail("Bad radii");
    } catch (final IllegalArgumentException e) {
      // ok
    }
    try {
      w.getCells(0, 1, -1, false);
      fail("Bad radii");
    } catch (final IllegalArgumentException e) {
      // ok
    }

    // test empty radius
    for (int j = 0; j < 49; ++j) {
      s = w.getCells(j, 0, 0, false);
      assertEquals(1, s.size());
      assertTrue(s.contains(w.getCell(j)));
    }
    for (int j = 0; j < 49; ++j) {
      s = w.getCells(j, 0, 0, true);
      assertEquals(1, s.size());
      assertTrue(s.contains(w.getCell(j)));
    }

    // test large radius
    for (int j = 0; j < 49; ++j) {
      assertEquals(49, w.getCells(j, 0, 15, false).size());
    }
    for (int j = 0; j < 49; ++j) {
      assertEquals(49, w.getCells(j, 0, 9, false).size());
    }
    for (int j = 0; j < 49; ++j) {
      assertEquals(48, w.getCells(j, 1, 15, false).size());
    }
    for (int j = 0; j < 49; ++j) {
      assertEquals(48, w.getCells(j, 1, 15, true).size());
    }
  }

  public void testGetCellSetWithBolter() {
    /*
      +-+-+-+-+-+-+-+
      | | | | | | | |
      +-+-+-+-+-+-+-+
      | | |#|#|#| | |
      +-+-+-+-+-+-+-+
      | |#|#|#|#|.| |
      +-+-+-+-+-+-+-+
      | |#|#|O|B|.| |
      +-+-+-+-+-+-+-+
      | |#|#|#|#|.| |
      +-+-+-+-+-+-+-+
      | | |#|#|#| | |
      +-+-+-+-+-+-+-+
      | | | | | | | |
      +-+-+-+-+-+-+-+
    */

    final World w = new World(7, 7);
    w.getCell(25).push(new Bolter());
    final Set<Cell> s = w.getCells(24, 0, 2, true);
    checkSet(s, w);
    assertTrue(!s.contains(w.getCell(19)));
    assertTrue(!s.contains(w.getCell(26)));
    assertTrue(!s.contains(w.getCell(33)));
  }

  public void testGetCellSetSkinny() {
    World w = new World(1, 7);
    for (int i = 0; i < 15; ++i) {
      assertEquals(Math.min(i + 1, 7), w.getCells(0, 0, i, false).size());
    }
    w = new World(7, 1);
    for (int i = 0; i < 15; ++i) {
      assertEquals(Math.min(i + 1, 7), w.getCells(0, 0, i, false).size());
    }
  }

  public void testGetCellSetRandom() {
    final World w = new World(25, 25);
    final int z = w.size();
    final Random r = new Random();
    for (int i = 0; i < 1000; ++i) {
      final int c = r.nextInt(z);
      final int q = r.nextInt(30);
      final Set<Cell> s = w.getCells(c, 0, q, false);
      final int qq = (int) ((q + 0.5) * (q + 0.5));
      for (final Cell value : s) {
        final int d = value.getCellNumber();
        final int f = w.getSquaredDistance(c, d);
        assertTrue(/* cell(w, c) + " " + cell(w, d) + " sq=" + f + " cf. " + qq, */ f <= qq);
      }
      for (int k = 0; k < 30; ++k) {
        final int d = r.nextInt(z);
        final int f = w.getSquaredDistance(c, d);
        if (f < qq && f != -1) {
          assertTrue(/* "c=" + cell(w, c) + " d=" + cell(w, d) + " root=" + f + " cf. " + qq, */ s.contains(w.getCell(d)));
        } else {
          assertTrue(!s.contains(w.getCell(z)));
        }
      }
    }
  }

  public void testGetCellsBelongingToAPlayer() {
    final World w = new World(25, 25);
    for (int i = -5; i < 15; ++i) {
      assertEquals(0, w.getCells(i).size());
    }
    final Lion l = new Lion();
    l.setOwner(2);
    l.setState(State.ACTIVE);
    w.getCell(75).push(l);
    for (int i = -5; i < 15; ++i) {
      if (i != 2) {
        assertEquals(0, w.getCells(i).size());
      } else {
        final Set<Cell> s = w.getCells(i);
        assertEquals(1, s.size());
        assertEquals(75, s.iterator().next().getCellNumber());
      }
    }
    l.setState(State.ASLEEP);
    for (int i = -5; i < 15; ++i) {
      assertEquals(0, w.getCells(i).size());
    }
  }

  public void testGetCellsBelongingToAPlayerRandom() {
    final World w = new World(25, 25);
    final int[] c = new int[10];
    final Random r = new Random();
    for (int i = 0; i < w.size(); ++i) {
      final Lion l = new Lion();
      final int p = r.nextInt(c.length);
      l.setOwner(p);
      l.setState(State.ACTIVE);
      w.getCell(i).push(l);
      c[p]++;
    }
    for (int i = 0; i < c.length; ++i) {
      assertEquals(c[i], w.getCells(i).size());
    }
  }

  public void testIsCatLordAlive() {
    final World w = new World(5, 5);
    assertEquals(-1, w.isCatLordAlive());
    final CatLord cl = new CatLord();
    for (int i = 0; i < w.size(); ++i) {
      w.getCell(i).push(cl);
      for (int j = -2; j < 3; ++j) {
        cl.setOwner(j);
        assertEquals(j, w.isCatLordAlive());
      }
      w.getCell(i).pop();
    }
    cl.setState(State.DEAD);
    w.getCell(3).push(cl);
    assertEquals(-1, w.isCatLordAlive());
  }

  public void testRegister() {
    try {
      new World(5, 5).register(null);
      fail();
    } catch (final NullPointerException e) {
      // ok
    }
  }

  public void testBiggerWorld() {
    final World w = new World(40, 30);
    final Set<Cell> c = w.getCells(39, 0, 1, false);
    assertEquals(4, c.size());
  }

  /*
  public void testGetCellSetccccRandom() {
    final World w = new World(25, 25);
    System.out.println(dumpSet(w.getCells(262, 0, 7, false), 262));
    System.out.println(dumpSet(w.getCells(269, 1, 11, false), 269));
    System.out.println(dumpSet(w.getCells(262, 4, 7, false), 262));
  }
  */
}

