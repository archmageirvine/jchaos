package chaos.common;

import chaos.board.World;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class PolycasterTest extends TestCase {

  private static final class MyPolycaster extends Polycaster {
    private final Castable mC;

    @SuppressWarnings("unchecked")
    MyPolycaster(final Castable c) {
      super(0);
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
    final Polycaster f = new MyPolycaster(lion);
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

  @SuppressWarnings("unchecked")
  private static final class YourPolycaster extends Polycaster {
    @Override
    public Class<? extends Monster> reincarnation() {
      return null;
    }

    @Override
    public long getLosMask() {
      return 0;
    }

    private YourPolycaster() {
      super(1, Lion.class);
    }
  }

  public void test2() {
    final Polycaster c = new YourPolycaster();
    assertTrue(c.getCastable() instanceof Lion);
    c.doCasting(null);
  }

  @SuppressWarnings("unchecked")
  private static final class HisPolycaster extends Polycaster {
    @Override
    public Class<? extends Monster> reincarnation() {
      return null;
    }

    @Override
    public long getLosMask() {
      return 0;
    }

    private HisPolycaster() {
      super(100000000, Lion.class);
    }
  }

  public void test3() {
    final Polycaster c = new HisPolycaster();
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
