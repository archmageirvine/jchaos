package chaos.common;

import chaos.board.World;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class UnicasterTest extends TestCase {


  private static class MyUnicaster extends Unicaster {
    private final Castable mC;
    MyUnicaster(final Castable c) {
      mC = c;
    }
    @Override
    public Castable getCastable() {
      return mC;
    }
    @Override
    public Class<? extends Monster> reincarnation() {
      return null;
    }
    @Override
    public long getLosMask() {
      return 0;
    }
    @Override
    public int getOwner() {
      return 1;
    }
  }

  public void test() {
    final Lion lion = new Lion();
    final Unicaster f = new MyUnicaster(lion);
    assertEquals(Realm.MATERIAL, f.getRealm());
    assertEquals(lion, f.getCastable());
    assertNull(f.getPlayerEngine());
    final World world = new World(1, 2);
    final Wizard1 wiz = new Wizard1();
    world.getWizardManager().setWizard(1, wiz);
    wiz.setState(State.ACTIVE);
    world.getCell(0).push(wiz);
    f.doCasting(world.getCell(0));
    f.doCasting(null);
  }

  private static class YourUnicaster extends Unicaster {
    @Override
    public Class<? extends Monster> reincarnation() {
      return null;
    }
    @Override
    public long getLosMask() {
      return 0;
    }
    {
      Assert.assertEquals(0, mDelay);
      mDelay = 1;
      Assert.assertNull(mCastClass);
      mCastClass = Lion.class;
    }
  }

  public void test2() {
    final Unicaster c = new YourUnicaster();
    assertTrue(c.getCastable() instanceof Lion);
    c.doCasting(null);
  }

  private static class HisUnicaster extends Unicaster {
    @Override
    public Class<? extends Monster> reincarnation() {
      return null;
    }
    @Override
    public long getLosMask() {
      return 0;
    }
    {
      mDelay = 100000000;
      mCastClass = Lion.class;
    }
  }

  public void test3() {
    final Unicaster c = new HisUnicaster();
    final World world = new World(1, 2);
    int nullCount = 0;
    for (int i = 0; i < 50; ++i) {
      c.doCasting(world.getCell(0));
      if (c.getCastable() == null) {
        ++nullCount;
      }
    }
    assertTrue(nullCount > 46);
  }

}
