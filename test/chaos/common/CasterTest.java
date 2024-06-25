package chaos.common;

import chaos.Chaos;
import chaos.board.Cell;
import chaos.board.World;
import chaos.common.monster.Horse;
import chaos.common.monster.Lion;
import chaos.common.spell.Teleport;
import chaos.common.wizard.Wizard;
import chaos.common.wizard.Wizard1;
import chaos.engine.PlayerEngine;
import chaos.selector.RandomAiSelector;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class CasterTest extends TestCase {

  private boolean mCast = false;

  private void setCast() {
    mCast = true;
  }

  private boolean getCast() {
    return mCast;
  }

  private class MyEngine implements PlayerEngine {
    @Override
    public boolean cast(final Caster caster, final Castable c, final Cell cell) {
      Assert.assertTrue(c instanceof Lion);
      setCast();
      return true;
    }

    @Override
    public void moveAll(final Wizard w) {
    }
  }

  private static class LionCaster extends Caster {
    @Override
    public Castable getCastable() {
      return new Lion();
    }

    @Override
    public Class<? extends Monster> reincarnation() {
      return null;
    }

    @Override
    public long getLosMask() {
      return 0;
    }
  }

  public void test() {
    final MyEngine eng = new MyEngine();
    final Caster f = new LionCaster();
    assertEquals(Realm.MATERIAL, f.getRealm());
    assertNull(f.getPlayerEngine());
    //f.doCasting(new Cell(0));
    f.setPlayerEngine(eng);
    assertEquals(eng, f.getPlayerEngine());
    f.doCasting(new Cell(0));
    assertTrue(getCast());
  }

  private static class NullCaster extends Caster {
    @Override
    public Castable getCastable() {
      return null;
    }

    @Override
    public Class<? extends Monster> reincarnation() {
      return null;
    }

    @Override
    public long getLosMask() {
      return 0;
    }
  }

  public void test2() {
    new NullCaster().doCasting(null);
  }

  private static final class TeleportEngine implements PlayerEngine {

    private final World mWorld;
    private int mN = 0;

    private TeleportEngine(final World world) {
      mWorld = world;
    }

    @Override
    public boolean cast(final Caster caster, final Castable c, final Cell cell) {
      c.cast(mWorld, caster, mWorld.getCell(++mN), cell);
      return true;
    }

    @Override
    public void moveAll(final Wizard w) {
    }
  }

  public void testDoubleTeleport() {
    final World world = Chaos.getChaos().getWorld(); //new World(1, 3, new Team());
    final Wizard1 wiz = new Wizard1();
    wiz.setState(State.ACTIVE);
    wiz.increment(PowerUps.DOUBLE);
    wiz.setPlayerEngine(new TeleportEngine(world));
    final CastableList castableList = new CastableList(1, 1, 1);
    castableList.clear();
    castableList.add(new Teleport());
    wiz.setCastableList(castableList);
    wiz.setSelector(new RandomAiSelector());
    wiz.select(false);
    world.getCell(0).push(wiz);
    world.getCell(1).push(new Lion());
    world.getCell(2).push(new Horse());
    wiz.doCasting(world.getCell(0));
    assertTrue(world.actor(0) instanceof Lion);
    assertTrue(world.actor(1) instanceof Horse);
    assertEquals(wiz, world.actor(2));
  }

}
