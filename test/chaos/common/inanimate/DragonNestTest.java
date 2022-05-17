package chaos.common.inanimate;

import java.util.HashSet;

import chaos.common.AbstractGeneratorTest;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.dragon.BlackDragon;
import chaos.common.dragon.BlueDragon;
import chaos.common.dragon.GoldenDragon;
import chaos.common.dragon.GreenDragon;
import chaos.common.dragon.PlatinumDragon;
import chaos.common.dragon.RedDragon;
import chaos.common.dragon.ShadowDragon;
import chaos.common.dragon.WhiteDragon;
import chaos.common.monster.Pseudodragon;

/**
 * Tests this inanimate.
 *
 * @author Sean A. Irvine
 */
public class DragonNestTest extends AbstractGeneratorTest {

  @Override
  public Castable getCastable() {
    return new DragonNest();
  }

  public void testWhatItProduces() {
    final DragonNest a = new DragonNest();
    assertEquals(7, a.get(Attribute.LIFE_RECOVERY));
    assertEquals(1, a.get(Attribute.MAGICAL_RESISTANCE_RECOVERY));
    assertEquals(12, a.getCastRange());
    assertEquals(3, a.getBonus());
    final HashSet<Class<? extends Actor>> h = new HashSet<>();
    int nullCount = 0;
    for (int i = 0; i < 10000; ++i) {
      final Actor g = a.chooseWhatToGenerate();
      if (g != null) {
        h.add(g.getClass());
      } else {
        ++nullCount;
      }
    }
    assertTrue(nullCount > 6500);
    assertTrue(nullCount < 9000);
    assertTrue(h.contains(RedDragon.class));
    assertTrue(h.contains(BlackDragon.class));
    assertTrue(h.contains(PlatinumDragon.class));
    assertTrue(h.contains(BlueDragon.class));
    assertTrue(h.contains(WhiteDragon.class));
    assertTrue(h.contains(GoldenDragon.class));
    assertTrue(h.contains(ShadowDragon.class));
    assertTrue(h.contains(GreenDragon.class));
    assertTrue(h.contains(Pseudodragon.class));
    assertEquals(9, h.size());
  }
}
