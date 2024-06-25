package chaos.common;

import chaos.common.monster.Hydra;
import chaos.common.monster.Lion;
import chaos.common.monster.Ogre;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class CastableListTest extends TestCase {


  public void testBadConstruction() {
    try {
      new CastableList(1, 0, 2);
      fail("Bad params");
    } catch (final IllegalArgumentException e) {
      assertEquals("Maximum visible cannot exceed list maximum", e.getMessage());
    }
    try {
      new CastableList(0, 0, 0);
      fail("Bad params");
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad arguments to CastableList", e.getMessage());
    }
    try {
      new CastableList(1, 0, 0);
      fail("Bad params");
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad arguments to CastableList", e.getMessage());
    }
    try {
      new CastableList(0, 0, 1);
      fail("Bad params");
    } catch (final IllegalArgumentException e) {
      assertEquals("Maximum visible cannot exceed list maximum", e.getMessage());
    }
    try {
      new CastableList(1, -1, 1);
      fail("Bad params");
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad initial count", e.getMessage());
    }
    try {
      new CastableList(1, 2, 1);
      fail("Bad params");
    } catch (final IllegalArgumentException e) {
      assertEquals("Bad initial count", e.getMessage());
    }
  }

  private void use(final CastableList cl, final Castable c) {
    try {
      cl.use(c);
      fail();
    } catch (final RuntimeException e) {
      assertEquals("Bad castable use", e.getMessage());
    }
  }

  public void testEmptyInit() {
    final CastableList cl = new CastableList(5, 0, 3);
    assertEquals(3, cl.getMaximumVisible());
    assertEquals(0, cl.getCount());
    assertEquals(0, cl.getVisible().length);
    use(cl, new Lion());
    try {
      cl.use(null);
      fail("Used null");
    } catch (final NullPointerException e) {
      assertEquals("Cannot use null", e.getMessage());
    }
    try {
      cl.add(null);
      fail("Added null");
    } catch (final NullPointerException e) {
      assertEquals("Cannot add null", e.getMessage());
    }
    final Lion l1 = new Lion();
    cl.add(l1);
    assertEquals(1, cl.getCount());
    Castable[] v = cl.getVisible();
    assertEquals(1, v.length);
    assertEquals(l1, v[0]);
    use(cl, new Hydra());
    assertTrue(cl.has(new Lion(), true));
    assertTrue(cl.has(new Lion(), false));
    assertTrue(!cl.has(new Hydra(), true));
    assertTrue(!cl.has(new Hydra(), false));
    cl.use(l1);
    assertEquals(3, cl.getMaximumVisible());
    assertEquals(0, cl.getCount());
    assertEquals(0, cl.getVisible().length);
    assertTrue(!cl.has(new Lion(), true));
    assertTrue(!cl.has(new Lion(), false));
    assertTrue(!cl.has(new Hydra(), true));
    assertTrue(!cl.has(new Hydra(), false));
    use(cl, new Lion());
    use(cl, l1);
    // fill list
    final Lion l2 = new Lion();
    final Lion l3 = new Lion();
    cl.add(l1);
    cl.add(l2);
    cl.add(l3);
    final Hydra h1 = new Hydra();
    final Hydra h2 = new Hydra();
    cl.add(h1);
    cl.add(h2);
    final Ogre o1 = new Ogre();
    cl.add(o1);
    assertTrue(cl.has(new Lion(), true));
    assertTrue(cl.has(new Lion(), false));
    assertTrue(!cl.has(new Hydra(), true));
    assertTrue(cl.has(new Hydra(), false));
    assertTrue(!cl.has(new Ogre(), true));
    assertTrue(!cl.has(new Ogre(), false));
    assertEquals(5, cl.getCount());
    v = cl.getVisible();
    assertEquals(3, v.length);
    assertEquals(l1, v[0]);
    assertEquals(l2, v[1]);
    assertEquals(l3, v[2]);
    use(cl, o1);
    cl.use(l2);
    assertTrue(cl.has(new Lion(), true));
    assertTrue(cl.has(new Lion(), false));
    assertTrue(cl.has(new Hydra(), true));
    assertTrue(cl.has(new Hydra(), false));
    assertTrue(!cl.has(new Ogre(), true));
    assertTrue(!cl.has(new Ogre(), false));
    assertEquals(4, cl.getCount());
    v = cl.getVisible();
    assertEquals(3, v.length);
    assertEquals(l1, v[0]);
    assertEquals(l3, v[1]);
    assertEquals(h1, v[2]);
    use(cl, o1);
    cl.use(l1);
    assertTrue(cl.has(new Lion(), true));
    assertTrue(cl.has(new Lion(), false));
    assertTrue(cl.has(new Hydra(), true));
    assertTrue(cl.has(new Hydra(), false));
    assertTrue(!cl.has(new Ogre(), true));
    assertTrue(!cl.has(new Ogre(), false));
    assertEquals(3, cl.getCount());
    v = cl.getVisible();
    assertEquals(3, v.length);
    assertEquals(l3, v[0]);
    assertEquals(h1, v[1]);
    assertEquals(h2, v[2]);
    use(cl, o1);
    cl.use(h1);
    assertTrue(cl.has(new Lion(), true));
    assertTrue(cl.has(new Lion(), false));
    assertTrue(cl.has(new Hydra(), true));
    assertTrue(cl.has(new Hydra(), false));
    assertTrue(!cl.has(new Ogre(), true));
    assertTrue(!cl.has(new Ogre(), false));
    assertEquals(2, cl.getCount());
    v = cl.getVisible();
    assertEquals(2, v.length);
    assertEquals(l3, v[0]);
    assertEquals(h2, v[1]);
    cl.use(l3);
    assertTrue(!cl.has(new Lion(), true));
    assertTrue(!cl.has(new Lion(), false));
    assertTrue(cl.has(new Hydra(), true));
    assertTrue(cl.has(new Hydra(), false));
    assertTrue(!cl.has(new Ogre(), true));
    assertTrue(!cl.has(new Ogre(), false));
    assertEquals(1, cl.getCount());
    v = cl.getVisible();
    assertEquals(1, v.length);
    assertEquals(h2, v[0]);
    use(cl, l3);
    use(cl, l2);
    use(cl, l1);
  }

  public void testGeneralInit() {
    final CastableList cl = new CastableList(50, 40, 30);
    assertEquals(30, cl.getMaximumVisible());
    assertEquals(40, cl.getCount());
    Castable[] v = cl.getVisible();
    assertEquals(30, v.length);
    for (Castable aV : v) {
      assertTrue(FrequencyTable.DEFAULT.getFrequency(aV.getClass()) > 0);
    }
    // put some holes in the table, then test joker
    cl.use(v[5]);
    cl.use(v[7]);
    cl.use(v[9]);
    cl.use(v[11]);
    assertEquals(36, cl.getCount());
    v = cl.getVisible();
    assertEquals(30, v.length);
    cl.joker();
    assertEquals(36, cl.getCount());
    final Castable[] v2 = cl.getVisible();
    assertEquals(30, v2.length);
    int c = 0;
    for (int i = 0; i < v2.length; ++i) {
      if (v[i] == v2[i]) {
        c += 1;
      }
    }
    assertTrue("Too much commonality", c <= 7);
  }

  public void testJoker2() {
    int bad = 0;
    for (int i = 0; i < 20; ++i) {
      final CastableList cl = new CastableList(2, 2, 2);
      final Castable c = cl.getVisible()[0];
      cl.joker();
      if (c == cl.getVisible()[0]) {
        ++bad;
      }
    }
    if (bad > 5) {
      fail("Too many static points at zero");
    }
  }

  public void testUsePastEnd() {
    final CastableList cl = new CastableList(3, 0, 2);
    cl.add(new Lion());
    cl.add(new Lion());
    final Hydra h = new Hydra();
    cl.add(h);
    try {
      cl.use(h);
      fail();
    } catch (final RuntimeException e) {
      assertEquals("Bad castable use", e.getMessage());
    }
  }

  public void testNullPositioning() {
    final CastableList cl = new CastableList(5, 0, 3);
    final Lion l1 = new Lion();
    final Lion l2 = new Lion();
    final Lion l3 = new Lion();
    final Hydra h1 = new Hydra();
    cl.add(l1);
    cl.add(l2);
    cl.add(l3);
    cl.add(h1);
    cl.use(l1);
    cl.use(l2);
    cl.use(l3);
    cl.use(h1);
    assertEquals(0, cl.getCount());
  }

  public void testViz1() {
    final CastableList cl = new CastableList(5, 0, 1);
    final Lion l1 = new Lion();
    final Hydra h1 = new Hydra();
    cl.add(l1);
    cl.add(h1);
    cl.use(l1);
    assertEquals(1, cl.getCount());
    cl.clear();
    assertEquals(0, cl.getCount());
  }

}
