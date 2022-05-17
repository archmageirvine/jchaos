package chaos.board;

import junit.framework.TestCase;

/**
 * JUnit tests for the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class TeamTest extends TestCase {


  public void test() {
    final Team t = new Team();
    for (int k = 1; k < WizardManager.WIZ_ARRAY_SIZE; ++k) {
      assertEquals(k + 2, t.getTeam(k));
      t.setTeam(k, 1);
      assertEquals(3, t.getTeam(k));
    }
    assertTrue(t.heraldicKey(3) != t.heraldicKey(4));
    int g = WizardManager.WIZ_ARRAY_SIZE + 1;
    for (int k = 11; k >= 0; --k) {
      t.separate(k);
      final int z = t.getTeam(k);
      assertEquals(++g, z);
      for (int j = 1; j < WizardManager.WIZ_ARRAY_SIZE; ++j) {
        if (k != j) {
          assertTrue(z != t.getTeam(j));
        }
      }
    }
    for (int k = 1; k < WizardManager.WIZ_ARRAY_SIZE; ++k) {
      t.separate(k);
      final int z = t.getTeam(k);
      for (int j = 1; j < WizardManager.WIZ_ARRAY_SIZE; ++j) {
        if (k != j) {
          assertTrue(z != t.getTeam(j));
        }
      }
    }
    assertEquals(0, t.getTeam(-2));
    try {
      t.getTeam(WizardManager.WIZ_ARRAY_SIZE);
      fail();
    } catch (final IndexOutOfBoundsException e) {
      // ok
    }
  }

}
