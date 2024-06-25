package chaos.common;

import java.io.IOException;
import java.io.InputStream;

import chaos.common.wizard.Wizard;
import chaos.util.NameUtils;
import chaos.util.RankingTable;
import junit.framework.TestCase;

/**
 * Tests basic functionality that all Castables should satisfy.
 * @author Sean A. Irvine
 */
public abstract class AbstractCastableTest extends TestCase {

  protected Castable mCastable;

  // implemented in subclasses to provide objects
  public abstract Castable getCastable();

  @Override
  public void setUp() throws Exception {
    super.setUp();
    mCastable = getCastable();
  }

  @Override
  public void tearDown() throws Exception {
    super.tearDown();
    mCastable = null;
  }

  /**
   * Test this castable has a valid name.
   */
  public void testGetName() {
    try {
      final String name = mCastable.getName();
      assertTrue(name != null);
      assertTrue(!name.isEmpty());
    } catch (final Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * Test this castable has a valid description.
   */
  public void testGetDescription() {
    try {
      final String desc = mCastable.getDescription();
      assertTrue(desc != null);
      assertTrue(!desc.isEmpty());
      // Descriptions which are too long go off the screen!
      assertTrue("Description too long: " + desc, desc.length() < 512);
    } catch (final Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * Testing the casting range is valid.
   */
  public void testCastRange() {
    assertTrue("Cast range error", mCastable.getCastRange() > 0);
  }

  /**
   * Test castable has a ranking.
   */
  public void testGetRanking() {
    if (FrequencyTable.DEFAULT.getFrequency(mCastable.getClass()) != 0) {
      assertTrue("No ranking found", RankingTable.getRanking(mCastable) >= 0);
    }
  }

  public void testHasBackdropImage() throws IOException {
    if (!(mCastable instanceof Wizard)) {
      try (InputStream is = getClass().getClassLoader().getResourceAsStream(NameUtils.getBackdropResource(mCastable))) {
        assertNotNull("No backdrop for " + mCastable.getName(), is);
      }
    }
  }
}
