package chaos.common.free;

import java.util.HashSet;
import java.util.Random;

import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.monster.CatLord;
import chaos.common.monster.Lion;
import chaos.common.monster.Skeleton;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class MutateTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new Mutate();
  }

  public void test1() throws IllegalAccessException, InstantiationException {
    final Mutate a = new Mutate();
    assertEquals(Castable.CAST_SINGLE, a.getCastFlags());
    final World world = new World(1, 6);
    final Wizard1 w = new Wizard1();
    w.setOwner(3);
    a.cast(null, null, null);
    a.cast(world, null, null);
    a.cast(null, w, null);
    a.cast(world, w, null);
    final Skeleton sk = new Skeleton();
    world.getCell(0).push(sk);
    a.cast(world, w, world.getCell(5));
    assertFalse(world.actor(0) instanceof Skeleton);
    final HashSet<Class<? extends Actor>> res = new HashSet<>();
    final Random r = new Random();
    for (int k = 0; k < 500; ++k) {
      final Lion l = new Lion();
      l.setOwner(2);
      final boolean rein = r.nextBoolean();
      l.set(PowerUps.REINCARNATE, l.reincarnation() != null && rein ? 1 : 0);
      final int horror = r.nextInt(5);
      l.set(PowerUps.HORROR, horror);
      world.getCell(5).push(l);
      a.cast(world, w, world.getCell(5));
      final Actor z = world.actor(5);
      assertNotNull(z);
      assertFalse(z instanceof Lion);
      assertFalse(z instanceof CatLord);
      assertEquals(2, z.getOwner());
      if (z instanceof Monster) {
        final Monster mz = (Monster) z;
        if (mz.reincarnation() == null) {
          assertFalse(mz.is(PowerUps.REINCARNATE));
        } else {
          assertEquals(z.getName(), rein, mz.is(PowerUps.REINCARNATE));
        }
        final Actor t = mz.getClass().newInstance();
        assertEquals(horror + t.get(PowerUps.HORROR), mz.get(PowerUps.HORROR));
      }
      res.add(z.getClass());
    }
    assertTrue(res.size() > 20);
  }
}
