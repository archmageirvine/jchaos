package chaos.common.inanimate;

import java.util.HashSet;

import chaos.common.AbstractGeneratorTest;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.Realm;
import chaos.common.monster.Skeleton;
import chaos.common.monster.SkeletonWarrior;
import chaos.common.monster.Vampire;

/**
 * Tests this inanimate.
 * @author Sean A. Irvine
 */
public class TombstoneTest extends AbstractGeneratorTest {

  @Override
  public Castable getCastable() {
    return new Tombstone();
  }

  public void testWhatItProduces() {
    final Tombstone a = new Tombstone();
    assertEquals(7, a.get(Attribute.LIFE_RECOVERY));
    assertEquals(1, a.get(Attribute.MAGICAL_RESISTANCE_RECOVERY));
    assertEquals(12, a.getCastRange());
    assertEquals(3, a.getBonus());
    assertEquals(Realm.ETHERIC, a.getRealm());
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
    assertTrue(h.contains(Skeleton.class));
    assertTrue(h.contains(SkeletonWarrior.class));
    assertTrue(h.contains(Vampire.class));
    assertEquals(15, h.size());
  }
}
