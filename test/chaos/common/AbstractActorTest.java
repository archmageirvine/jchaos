package chaos.common;

import java.lang.reflect.Method;
import java.util.HashSet;

import chaos.common.inanimate.Tempest;
import chaos.common.inanimate.Vortex;
import chaos.common.monster.Lion;

/**
 * Tests basic functionality that all Actors should satisfy.
 *
 * @author Sean A. Irvine
 */
public abstract class AbstractActorTest extends AbstractCastableTest {

  protected Actor getActor() {
    return (Actor) getCastable();
  }

  /**
   * Test supplied Castable is really an Actor.
   */
  public void testInstanceOf() {
    assertTrue(mCastable instanceof Actor);
  }

  /**
   * Test realm is within valid range.
   */
  public void testRealm() {
    final Actor a = (Actor) mCastable;
    switch (a.getRealm()) {
      case NONE:
        fail("Real should probably not be REALM_NONE");
        break;
      case MATERIAL:
      case ETHERIC:
      case DRACONIC:
      case DEMONIC:
      case MYTHOS:
      case HYPERASTRAL:
      case SUBHYADIC:
      case CORE:
        break;
      default:
        fail("Invalid realm");
    }
  }

  /**
   * Test state is within valid range.
   */
  public void testState() {
    final Actor a = getActor();
    switch (a.getState()) {
    case ACTIVE:
    case DEAD:
    case ASLEEP:
      break;
    default:
      fail("Invalid state");
    }
    a.setState(State.DEAD);
    assertEquals(0, a.getWeight());
    a.setState(State.ASLEEP);
    assertEquals(1, a.getWeight());
    a.setState(State.ACTIVE);
    assertEquals(State.ACTIVE, a.getState());
  }

  /**
   * Test if the owner information is reliably retained.
   */
  public void testOwnerRetention() {
    final Actor a = (Actor) mCastable;
    a.setOwner(Actor.OWNER_INDEPENDENT);
    assertEquals(Actor.OWNER_INDEPENDENT, a.getOwner());
    a.setOwner(Actor.OWNER_NONE);
    assertEquals(Actor.OWNER_NONE, a.getOwner());
    a.setOwner(5);
    assertEquals(5, a.getOwner());
  }

  /**
   * Test initial life and life retention.
   */
  public void testLife() {
    final Actor a = (Actor) mCastable;
    assertTrue(a.get(Attribute.LIFE) >= 0);
    a.set(Attribute.LIFE, 100);
    assertEquals(100, a.get(Attribute.LIFE));
    a.set(Attribute.LIFE, 5);
    assertEquals(5, a.get(Attribute.LIFE));
  }

  /**
   * Test life recovery.
   */
  public void testLifeRecovery() {
    final Actor a = (Actor) mCastable;
    a.set(Attribute.LIFE_RECOVERY, 7);
    assertEquals(7, a.get(Attribute.LIFE_RECOVERY));
    a.set(Attribute.LIFE_RECOVERY, 5);
    assertEquals(5, a.get(Attribute.LIFE_RECOVERY));
  }

  /**
   * Test initial magical resistance and magical resistance retention.
   */
  public void testMagicalResistance() {
    final Actor a = (Actor) mCastable;
    assertTrue(a.get(Attribute.MAGICAL_RESISTANCE) >= 0);
    a.set(Attribute.MAGICAL_RESISTANCE, 100);
    assertEquals(100, a.get(Attribute.MAGICAL_RESISTANCE));
    a.set(Attribute.MAGICAL_RESISTANCE, 5);
    assertEquals(5, a.get(Attribute.MAGICAL_RESISTANCE));
  }

  /**
   * Test magical resistance recovery.
   */
  public void testMagicalResistanceRecovery() {
    final Actor a = (Actor) mCastable;
    a.set(Attribute.MAGICAL_RESISTANCE_RECOVERY, 7);
    assertEquals(7, a.get(Attribute.MAGICAL_RESISTANCE_RECOVERY));
    a.set(Attribute.MAGICAL_RESISTANCE_RECOVERY, 5);
    assertEquals(5, a.get(Attribute.MAGICAL_RESISTANCE_RECOVERY));
  }

  /**
   * Test defaults were correctly applied.
   */
  public void testDefaults() {
    final Actor a = (Actor) mCastable;
    assertEquals("life error", a.getDefault(Attribute.LIFE), a.get(Attribute.LIFE));
    assertEquals("life recovery error", a.getDefault(Attribute.LIFE_RECOVERY), a.get(Attribute.LIFE_RECOVERY));
    assertEquals("MR error", a.getDefault(Attribute.MAGICAL_RESISTANCE), a.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals("MR recovery error", a.getDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY), a.get(Attribute.MAGICAL_RESISTANCE_RECOVERY));
  }

  /**
   * If this monster implements bonus, check the bonus is &gt;0.
   */
  public void testBonus() {
    if (mCastable instanceof Bonus) {
      final Bonus b = (Bonus) mCastable;
      assertTrue("bonus wasn't positive", b.getBonus() > 0);
    }
  }

  public void testAnimateable() {
    if (mCastable instanceof Animateable) {
      assertNotNull(((Animateable) mCastable).getAnimatedForm());
    }
  }

  /**
   * If this monster implements multiplicity, check the multiplicity is &gt;1.
   */
  public void testMultiplicity() {
    if (mCastable instanceof Multiplicity) {
      final Multiplicity b = (Multiplicity) mCastable;
      assertTrue("multiplicity wasn't >1", b.getMultiplicity() > 1);
    }
  }

  public void testIsMoved() {
    // should be false be default at start
    final Actor a = (Actor) mCastable;
    assertTrue("isMoved was set unexpectedly", !a.isMoved());
    a.setMoved(true);
    assertTrue("isMoved was clear unexpectedly", a.isMoved());
  }

  public void testIsEngaged() {
    // should be false be default at start
    final Actor a = (Actor) mCastable;
    assertTrue("isEngaged was set unexpectedly", !a.isEngaged());
    a.setEngaged(true);
    assertTrue("isEngaged was clear unexpectedly", a.isEngaged());
  }

  /**
   * Test promotion if relevant
   */
  public void testPromotion() {
    if (mCastable instanceof Promotion) {
      final Promotion p = (Promotion) mCastable;
      assertTrue("Bad promo count", p.promotionCount() > 0);
      final Class<? extends Actor> pr = p.promotion();
      assertTrue("Null promo class", pr != null);
      assertTrue("Cannot promote to self", mCastable.getClass() != pr);
      try {
        final Object o = pr.newInstance();
        assertTrue(o instanceof Actor);
      } catch (final Exception e) {
        e.printStackTrace();
        fail(e.getMessage());
      }
    }
  }

  static final HashSet<String> ACCEPT = new HashSet<>();
  static {
    ACCEPT.add("getDefaultWeight");
    ACCEPT.add("getDefault");
  }

  /**
   * Tests, using reflection, that the <code>getDefault*</code> and certain other
   * method names are acceptable. It fails if, for example, <code>getBonus()</code>
   * is present yet the class does not implement Bonus or if there
   * are any <code>getDefault*()</code> methods beyond those expected (the presence
   * of such methods if probably a typo).
   */
  public void testMethodNames() {
    // we know methods cannot be null
    for (final Method method : mCastable.getClass().getMethods()) {
      final String name = method.getName();
      if (name.startsWith("getDefault") && !ACCEPT.contains(name)) {
        fail("Unexpected method named: " + name);
      } else if ("getBonus".equals(name) && !(mCastable instanceof Bonus)) {
        fail("Saw getBonus() without implementing Bonus");
      } else if ("promotionCount".equals(name) && !(mCastable instanceof Promotion)) {
        fail("Saw promotionCount() without implementing Promotion");
      } else if ("promotion".equals(name) && !(mCastable instanceof Promotion)) {
        fail("Saw promotion() without implementing Promotion");
      } else if ("getAnimatedForm".equals(name) && !(mCastable instanceof Animateable)) {
        fail("Saw getAnimatedForm() without implementing Animateable");
      } else if ("getMount".equals(name) && !(mCastable instanceof Conveyance)) {
        fail("Saw getMount() without implementing Conveyance");
      } else if ("setMount".equals(name) && !(mCastable instanceof Conveyance)) {
        fail("Saw setMount() without implementing Mountable");
      }
    }
  }

  /**
   * Test behaviour of mounting routines is correct.
   */
  public void testMounting() {
    if (!(mCastable instanceof Conveyance)) {
      return;
    }
    if (mCastable instanceof Mountable) {
      assertFalse(mCastable instanceof Rideable);
      assertFalse(mCastable instanceof Meditation);
    } else if (mCastable instanceof Rideable) {
      assertFalse(mCastable instanceof Meditation);
    }
    final Conveyance m = (Conveyance) mCastable;
    assertEquals(null, m.getMount());
    final Lion lion = new Lion();
    m.setMount(lion);
    assertEquals(lion, m.getMount());
    try {
      final Lion l2 = new Lion();
      m.setMount(l2);
      fail("Allowed multiple mount");
    } catch (final RuntimeException e) {
      assertEquals("Attempt to mount something already mounted", e.getMessage());
    }
    m.setMount(null);
    assertEquals(null, m.getMount());
    if (mCastable instanceof Actor) {
      try {
        m.setMount((Actor) mCastable);
        fail("Allowed self mount");
      } catch (final RuntimeException e) {
        assertEquals("Cannot mount self", e.getMessage());
      }
    }
  }

  /**
   * Test weight range.
   */
  public void testDefaultWeight() {
    final Actor a = (Actor) mCastable;
    final int w = a.getDefaultWeight();
    assertTrue(w >= -100);
    assertTrue(w <= 100);
  }

  public void testWeight() {
    final Actor a = (Actor) mCastable;
    final int v = a.getWeight();
    assertEquals(a.getDefaultWeight(), v);
    a.setState(State.DEAD);
    assertEquals(0, a.getWeight());
    a.setState(State.ASLEEP);
    assertEquals(1, a.getWeight());
    a.setState(State.ACTIVE);
    assertEquals(v, a.getWeight());
  }

  /**
   * If this is a growth check the growth type is valid.
   */
  public void testGrowthTypeAndRate() {
    if (mCastable instanceof Growth) {
      int t = ((Growth) mCastable).getGrowthType();
      assertTrue("Bad growth type", t == Growth.GROW_BY_COMBAT || t == Growth.GROW_OVER || t == Growth.GROW_FOUR_WAY);
      t = ((Growth) mCastable).growthRate();
      assertTrue(t > 0);
      assertTrue(t <= 100);
    }
  }

  /**
   * Check update clears movement flag. And stats don't go haywire
   */
  public void testUpdate() {
    final Actor a = (Actor) mCastable;
    a.setMoved(true);
    assertFalse(a.update(null, null));
    assertFalse(a.isMoved());
    if (a.getDefault(Attribute.LIFE_RECOVERY) < 0) {
      assertEquals(a.getDefault(Attribute.LIFE) + a.getDefault(Attribute.LIFE_RECOVERY), a.get(Attribute.LIFE));
    } else {
      assertEquals(a.getDefault(Attribute.LIFE), a.get(Attribute.LIFE));
    }
    assertEquals(a.getDefault(Attribute.LIFE_RECOVERY), a.get(Attribute.LIFE_RECOVERY));
    assertEquals(a.getDefault(Attribute.MAGICAL_RESISTANCE), a.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(a.getDefault(Attribute.MAGICAL_RESISTANCE_RECOVERY), a.get(Attribute.MAGICAL_RESISTANCE_RECOVERY));
    a.set(Attribute.LIFE, 0);
    a.set(Attribute.MAGICAL_RESISTANCE, 0);
    assertEquals(a.getDefault(Attribute.LIFE_RECOVERY) < 0, a.update(null, null));
    assertFalse(a.isMoved());
    assertEquals(Math.min(a.get(Attribute.LIFE_RECOVERY), a.getDefault(Attribute.LIFE)), a.get(Attribute.LIFE));
    assertEquals(Math.min(a.get(Attribute.MAGICAL_RESISTANCE_RECOVERY), a.getDefault(Attribute.MAGICAL_RESISTANCE)), a.get(Attribute.MAGICAL_RESISTANCE));
    a.setMoved(true);
    a.setState(State.DEAD);
    assertFalse(a.update(null, null));
    if (!(a instanceof Tempest) && !(a instanceof Vortex)) {
      assertTrue(a.isMoved());
    }
    a.setState(State.ACTIVE);
    assertEquals(a.getDefault(Attribute.LIFE_RECOVERY) < 0, a.update(null, null));
    assertFalse(a.isMoved());
  }

  /** Check sleepers die. */
  public void testUpdateAsleep() {
    final Actor a = (Actor) mCastable;
    a.setState(State.ASLEEP);
    for (int i = 0; i < 300; ++i) {
      if (a.update(null, null)) {
        return;
      }
    }
    fail("Sleeper persisted for more than 300 turns");
  }
}
