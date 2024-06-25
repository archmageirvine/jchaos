package chaos.common.free;

import chaos.common.Castable;
import chaos.common.CastableList;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class JokerTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Joker();
  }

  public void test1() {
    final Joker a = new Joker();
    assertEquals(Castable.CAST_SINGLE, a.getCastFlags());
    final Wizard1 w = new Wizard1();
    w.setCastableList(new CastableList(100, 100, 24));
    final int c = w.getCastableList().getCount();
    a.cast(null, null, null);
    int z = 0;
    for (int i = 0; i < 10; ++i) {
      final Castable cc = w.getCastableList().getVisible()[0];
      a.cast(null, w, null);
      assertEquals(c, w.getCastableList().getCount());
      if (cc == w.getCastableList().getVisible()[0]) {
        ++z;
      }
    }
    assertTrue(z < 3);
  }
}
