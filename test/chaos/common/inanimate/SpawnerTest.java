package chaos.common.inanimate;

import java.util.HashSet;

import chaos.common.AbstractGeneratorTest;
import chaos.common.Actor;
import chaos.common.Castable;

/**
 * Tests this inanimate.
 * @author Sean A. Irvine
 */
public class SpawnerTest extends AbstractGeneratorTest {

  @Override
  public Castable getCastable() {
    return new Spawner();
  }

  public void testWhatItProduces() {
    final Spawner a = new Spawner();
    final HashSet<Class<? extends Actor>> h = new HashSet<>();
    int nullCount = 0;
    for (int i = 0; i < 100; ++i) {
      final Actor g = a.chooseWhatToGenerate();
      if (g != null) {
        h.add(g.getClass());
      } else {
        ++nullCount;
      }
    }
    assertTrue(nullCount > 0);
    assertEquals(1, h.size());
    assertTrue(h.contains(a.getSpawn().getClass()));
  }
}
