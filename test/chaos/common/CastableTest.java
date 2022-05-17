package chaos.common;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.spell.Armour;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class CastableTest extends AbstractCastableTest {


  @Override
  public Castable getCastable() {
    return new Armour();
  }

  private static class MyCastable extends Castable {
    @Override
    public int getCastFlags() {
      return 0;
    }
    @Override
    public int getCastRange() {
      return 0;
    }
    @Override
    public void cast(final World w, final Caster c, final Cell cc, final Cell ccc) {
    }
  }

  public void testNonName() {
    assertEquals("", new MyCastable().getName());
  }
}
