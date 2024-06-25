package chaos.common.spell;

import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Monster;
import chaos.common.monster.Lion;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class RestorationTest extends HealTest {

  @Override
  public Castable getCastable() {
    return new Restoration();
  }

  @Override
  public void testFlags() {
    final Castable x = getCastable();
    assertEquals(Castable.CAST_LIVING | Castable.CAST_INANIMATE | Castable.CAST_NOWIZARDCELL, x.getCastFlags());
    assertEquals(Castable.MAX_CAST_RANGE, x.getCastRange());
  }

  @Override
  protected void checkState(final Monster m) {
    assertEquals(m.get(Attribute.INTELLIGENCE), m.getDefault(Attribute.INTELLIGENCE));
  }

  @Override
  public void testCast() {
    check(new Lion());
  }
}
