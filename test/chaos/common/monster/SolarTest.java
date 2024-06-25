package chaos.common.monster;

import chaos.board.World;
import chaos.common.AbstractMonsterTest;
import chaos.common.Attribute;
import chaos.common.Castable;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class SolarTest extends AbstractMonsterTest {


  @Override
  public Castable getCastable() {
    return new Solar();
  }

  /**
   * Check update clears movement flag. And stats don't go haywire
   */
  @Override
  public void testUpdate() {
    final Solar a = (Solar) mCastable;
    a.update(null, null);
    assertTrue(a.isMoved());
    assertEquals(a.getDefault(Attribute.LIFE), a.get(Attribute.LIFE));
    assertEquals(a.getDefault(Attribute.LIFE_RECOVERY), a.get(Attribute.LIFE_RECOVERY));
    assertEquals(a.getDefault(Attribute.MAGICAL_RESISTANCE), a.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(a.getDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY), a.get(Attribute.MAGICAL_RESISTANCE_RECOVERY));
    a.set(Attribute.LIFE, 0);
    a.set(Attribute.MAGICAL_RESISTANCE, 0);
    assertFalse(a.update(null, null));
    assertTrue(a.isMoved());
    assertEquals(Math.min(a.get(Attribute.LIFE_RECOVERY), a.getDefault(Attribute.LIFE)), a.get(Attribute.LIFE));
    assertEquals(Math.min(a.get(Attribute.MAGICAL_RESISTANCE_RECOVERY), a.getDefault(Attribute.MAGICAL_RESISTANCE)), a.get(Attribute.MAGICAL_RESISTANCE));
  }

  public void testCantMoveAfterCast() {
    final World w = new World(3, 3);
    final Solar solar = new Solar();
    solar.setOwner(1);
    solar.cast(w, null, w.getCell(1), null);
    assertTrue(w.actor(1) instanceof Solar);
    assertTrue(solar.isMoved());
  }
}
