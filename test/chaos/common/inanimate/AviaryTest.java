package chaos.common.inanimate;

import java.util.HashSet;

import chaos.common.AbstractGeneratorTest;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.monster.Achiyalabopa;
import chaos.common.monster.Bat;
import chaos.common.monster.BirdLord;
import chaos.common.monster.Eagle;
import chaos.common.monster.Falcon;
import chaos.common.monster.FireBat;
import chaos.common.monster.Harpy;
import chaos.common.monster.Phoenix;
import chaos.common.monster.Spectator;
import chaos.common.monster.Vulture;

/**
 * Tests this inanimate.
 * @author Sean A. Irvine
 */
public class AviaryTest extends AbstractGeneratorTest {

  @Override
  public Castable getCastable() {
    return new Aviary();
  }

  public void testWhatItProduces() {
    final Aviary a = new Aviary();
    assertEquals(7, a.get(Attribute.LIFE_RECOVERY));
    assertEquals(1, a.get(Attribute.MAGICAL_RESISTANCE_RECOVERY));
    assertEquals(12, a.getCastRange());
    assertEquals(3, a.getBonus());
    final HashSet<Class<? extends Castable>> h = new HashSet<>();
    for (int i = 0; i < 1000; ++i) {
      h.add(a.chooseWhatToGenerate().getClass());
    }
    assertTrue(h.contains(Achiyalabopa.class));
    assertTrue(h.contains(Bat.class));
    assertTrue(h.contains(BirdLord.class));
    assertTrue(h.contains(Eagle.class));
    assertTrue(h.contains(Falcon.class));
    assertTrue(h.contains(FireBat.class));
    assertTrue(h.contains(Harpy.class));
    assertTrue(h.contains(Spectator.class));
    assertTrue(h.contains(Vulture.class));
    assertTrue(h.contains(Phoenix.class));
    assertEquals(10, h.size());
  }
}
