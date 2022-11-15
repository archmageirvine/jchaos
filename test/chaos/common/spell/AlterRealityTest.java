package chaos.common.spell;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Random;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractCastableTest;
import chaos.common.Actor;
import chaos.common.Castable;
import chaos.common.Monster;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.monster.Bat;
import chaos.common.monster.Demon;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class AlterRealityTest extends AbstractCastableTest {

  @Override
  public Castable getCastable() {
    return new AlterReality();
  }

  public void testCast() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
    final World world = new World(20, 2);
    final AlterReality a = new AlterReality();
    assertEquals(5, a.getCastRange());
    assertEquals(Castable.CAST_LIVING | Castable.CAST_INANIMATE | Castable.CAST_LOS | Castable.CAST_GROWTH, a.getCastFlags());
    final Wizard1 w = new Wizard1();
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
      a.cast(world, w, world.getCell(5), world.getCell(0));
      final Actor z = world.actor(5);
      assertNotNull(z);
      assertFalse(z instanceof Lion);
      assertEquals(2, z.getOwner());
      if (z instanceof Monster) {
        final Monster mz = (Monster) z;
        if (mz.reincarnation() == null) {
          assertFalse(mz.is(PowerUps.REINCARNATE));
        } else {
          assertEquals(z.getName(), rein, mz.is(PowerUps.REINCARNATE));
        }
        // A few creatures have predefined horror -- so have to account for that
        final Actor t = mz.getClass().getDeclaredConstructor().newInstance();
        assertEquals(horror + t.get(PowerUps.HORROR), mz.get(PowerUps.HORROR));
      }
      res.add(z.getClass());
    }
    assertTrue(res.size() > 20);
    world.getCell(5).push(w);
    a.cast(world, w, world.getCell(5), world.getCell(0));
    assertEquals(w, world.actor(5));
  }

  public void testTargetSelection() {
    final AlterReality a = new AlterReality();
    final World world = new World(3, 3);
    final Wizard1 w = new Wizard1();
    w.setOwner(1);
    w.setState(State.ACTIVE);
    world.getCell(4).push(w);
    final HashSet<Cell> targets = new HashSet<>();
    final Demon d = new Demon();
    d.setOwner(1);
    final Cell cd = world.getCell(0);
    cd.push(d);
    targets.add(cd);
    a.filter(targets, w, world);
    assertEquals(0, targets.size());
    final Bat b = new Bat();
    b.setOwner(1);
    cd.push(b);
    targets.add(cd);
    a.filter(targets, w, world);
    assertEquals(1, targets.size());
    final Demon d2 = new Demon();
    d2.setOwner(2);
    final Cell cd2 = world.getCell(1);
    cd2.push(d2);
    targets.add(cd2);
    a.filter(targets, w, world);
    assertEquals(1, targets.size());
    assertTrue(targets.contains(cd2));
  }
}
