package chaos.util;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * JUnit tests for the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class DefaultEventGeneratorTest extends TestCase implements EventListener {


  private int mCount = 0;

  @Override
  public void update(final Event event) {
    Assert.assertTrue(event instanceof TextEvent);
    ++mCount;
  }

  public void test() {
    final DefaultEventGenerator g = new DefaultEventGenerator();
    final TextEvent t = new TextEvent("hello");
    g.notify(t);
    g.register(this);
    g.notify(t);
    assertEquals(1, mCount);
    g.deregister(this);
    g.notify(t);
    assertEquals(1, mCount);
  }
}
