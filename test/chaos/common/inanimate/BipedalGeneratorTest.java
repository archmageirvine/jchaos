package chaos.common.inanimate;

import java.util.HashSet;

import chaos.common.AbstractGeneratorTest;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.monster.Azer;
import chaos.common.monster.Baboon;
import chaos.common.monster.Banderlog;
import chaos.common.monster.Bandit;
import chaos.common.monster.Derro;
import chaos.common.monster.Faun;
import chaos.common.monster.Goblin;
import chaos.common.monster.Gorilla;
import chaos.common.monster.GrayElf;
import chaos.common.monster.Halfling;
import chaos.common.monster.Ogre;
import chaos.common.monster.Orc;
import chaos.common.monster.Spriggan;
import chaos.common.monster.Troll;
import chaos.common.monster.WoodElf;

/**
 * Tests this inanimate.
 * @author Sean A. Irvine
 */
public class BipedalGeneratorTest extends AbstractGeneratorTest {

  @Override
  public Castable getCastable() {
    return new BipedalGenerator();
  }

  public void testWhatItProduces() {
    final BipedalGenerator a = new BipedalGenerator();
    assertEquals(7, a.get(Attribute.LIFE_RECOVERY));
    assertEquals(1, a.get(Attribute.MAGICAL_RESISTANCE_RECOVERY));
    assertEquals(12, a.getCastRange());
    assertEquals(3, a.getBonus());
    final HashSet<Class<? extends Castable>> h = new HashSet<>();
    for (int i = 0; i < 1000; ++i) {
      h.add(a.chooseWhatToGenerate().getClass());
    }
    assertTrue(h.contains(Faun.class));
    assertTrue(h.contains(Goblin.class));
    assertTrue(h.contains(Orc.class));
    assertTrue(h.contains(Troll.class));
    assertTrue(h.contains(Ogre.class));
    assertTrue(h.contains(Gorilla.class));
    assertTrue(h.contains(Halfling.class));
    assertTrue(h.contains(Baboon.class));
    assertTrue(h.contains(Derro.class));
    assertTrue(h.contains(Spriggan.class));
    assertTrue(h.contains(Azer.class));
    assertTrue(h.contains(Banderlog.class));
    assertTrue(h.contains(Bandit.class));
    assertTrue(h.contains(WoodElf.class));
    assertTrue(h.contains(GrayElf.class));
    assertEquals(15, h.size());
  }
}
