package chaos.util;

import junit.framework.TestCase;

/**
 * JUnit tests for the low-level event passing mechanism.
 * @author Sean A. Irvine
 */
public class EventsTest extends TestCase implements EventListener {


  private boolean mValue = false;

  private static class NullEvent implements Event {
  }

  @Override
  public void update(final Event e) {
    assertTrue(!mValue);
    mValue = true;
  }

  public boolean getValue() {
    return mValue;
  }

  public void setValue(final boolean value) {
    mValue = value;
  }

  public void testRegistration() {
    final DefaultEventGenerator eg = new DefaultEventGenerator();
    try {
      eg.register(null);
      fail("Accepted null registration");
    } catch (final NullPointerException e) {
      // ok
    }
    try {
      eg.deregister(null);
      eg.deregister(this);
    } catch (final Throwable t) {
      fail("Unexpected failure in degregister");
    }
    assertTrue(!getValue());
  }

  public void testEventPassingSynchronicity() {
    final DefaultEventGenerator eg = new DefaultEventGenerator();
    eg.register(this);
    for (int i = 0; i < 10; ++i) {
      eg.notify(new NullEvent());
      assertTrue(getValue());
      setValue(false);
    }
    eg.deregister(this);
    for (int i = 0; i < 10; ++i) {
      eg.notify(new NullEvent());
      assertTrue(!getValue());
    }
  }

  public void testDualRegisterSelf() {
    final DefaultEventGenerator eg = new DefaultEventGenerator();
    eg.register(this);
    eg.register(this);
    for (int i = 0; i < 10; ++i) {
      eg.notify(new NullEvent());
      assertTrue(getValue());
      setValue(false);
    }
    eg.deregister(this);
    for (int i = 0; i < 10; ++i) {
      eg.notify(new NullEvent());
      assertTrue(!getValue());
    }
  }

  private static class InnerListener implements EventListener {
    private boolean mValue = false;

    @Override
    public void update(final Event e) {
      assertTrue(!mValue);
      mValue = true;
    }

    public boolean getValue() {
      return mValue;
    }

    public void setValue(final boolean value) {
      mValue = value;
    }

  }

  public void testDualRegister() {
    final DefaultEventGenerator eg = new DefaultEventGenerator();
    eg.register(this);
    final InnerListener il = new InnerListener();
    eg.register(il);
    for (int i = 0; i < 10; ++i) {
      eg.notify(new NullEvent());
      assertTrue(getValue());
      assertTrue(il.getValue());
      setValue(false);
      il.setValue(false);
    }
    eg.deregister(this);
    for (int i = 0; i < 10; ++i) {
      eg.notify(new NullEvent());
      assertTrue(!getValue());
      assertTrue(il.getValue());
      il.setValue(false);
    }
    eg.deregister(il);
    for (int i = 0; i < 10; ++i) {
      eg.notify(new NullEvent());
      assertTrue(!getValue());
    }
  }

}
