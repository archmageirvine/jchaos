package chaos.util;

import junit.framework.TestCase;

/**
 * JUnit tests for the corresponding class.
 * @author Sean A. Irvine
 */
public class IntegerQueueTest extends TestCase {


  public void testBad() {
    try {
      new IntegerQueue(-1);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("Size must be nonnegative", e.getMessage());
    }
  }

  public void testGood() {
    final IntegerQueue q = new IntegerQueue(100);
    assertEquals(-1, q.dequeue());
    assertEquals(-1, q.dequeue());
    try {
      q.enqueue(-1);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("n is out of range", e.getMessage());
    }
    try {
      q.enqueue(100);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("n is out of range", e.getMessage());
    }
    try {
      q.enqueue(101);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("n is out of range", e.getMessage());
    }
    q.enqueue(5);
    try {
      q.enqueue(5);
      fail();
    } catch (final IllegalArgumentException e) {
      assertEquals("n is already in queue", e.getMessage());
    }
    assertEquals(5, q.dequeue());
    assertEquals(-1, q.dequeue());
    assertEquals(-1, q.dequeue());
    q.enqueue(5);
    try {
      q.enqueue(5);
      fail();
    } catch (final IllegalArgumentException e) {
      // ok
    }
    assertEquals(5, q.dequeue());
    assertEquals(-1, q.dequeue());
    assertEquals(-1, q.dequeue());
    q.enqueue(5);
    q.enqueue(3);
    q.enqueue(99);
    q.enqueue(17);
    try {
      q.enqueue(5);
      fail();
    } catch (final IllegalArgumentException e) {
      // ok
    }
    try {
      q.enqueue(99);
      fail();
    } catch (final IllegalArgumentException e) {
      // ok
    }
    assertEquals(5, q.dequeue());
    assertEquals(3, q.dequeue());
    assertEquals(99, q.dequeue());
    q.enqueue(3);
    assertEquals(17, q.dequeue());
    assertEquals(3, q.dequeue());
    assertEquals(-1, q.dequeue());
    assertEquals(-1, q.dequeue());
  }

}
