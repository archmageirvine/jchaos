package chaos.util;

import junit.framework.TestCase;

/**
 * Test the corresponding class.
 * @author Sean A. Irvine
 */
public class BooleanLockTest extends TestCase {

  public void testNoWait() {
    BooleanLock bl = new BooleanLock();
    assertTrue(!bl.isTrue());
    assertTrue(bl.isFalse());
    bl = new BooleanLock(false);
    assertTrue(!bl.isTrue());
    assertTrue(bl.isFalse());
    bl = new BooleanLock(true);
    assertTrue(bl.isTrue());
    assertTrue(!bl.isFalse());
    bl.setValue(false);
    assertTrue(!bl.isTrue());
    assertTrue(bl.isFalse());
    bl.setValue(true);
    assertTrue(bl.isTrue());
    assertTrue(!bl.isFalse());
    bl.setValue(true);
    assertTrue(bl.isTrue());
    assertTrue(!bl.isFalse());
    bl.setValue(false);
    assertTrue(!bl.isTrue());
    assertTrue(bl.isFalse());
  }

  public void testWaitNoWait() throws InterruptedException {
    final BooleanLock bl = new BooleanLock();
    assertTrue(bl.waitUntilFalse(20));
    assertTrue(!bl.waitUntilTrue(20));
    assertTrue(bl.waitUntilFalse(20));
    bl.setValue(true);
    assertTrue(!bl.waitUntilFalse(20));
    assertTrue(bl.waitUntilTrue(20));
    assertTrue(!bl.waitUntilFalse(20));
    assertTrue(bl.waitUntilTrue(0)); // should be ok
    assertTrue(!bl.waitUntilFalse(-20));
    assertTrue(bl.waitUntilTrue(-20));
    assertTrue(!bl.waitUntilFalse(-20));
  }

  public void testWaitState() throws InterruptedException {
    final BooleanLock bl = new BooleanLock();
    assertTrue(bl.waitUntilStateIs(false, 20));
    assertTrue(!bl.waitUntilStateIs(true, 20));
    assertTrue(bl.waitUntilStateIs(false, 20));
    bl.setValue(true);
    assertTrue(!bl.waitUntilStateIs(false, 20));
    assertTrue(bl.waitUntilStateIs(true, 20));
    assertTrue(!bl.waitUntilStateIs(false, 20));
  }

  public void testSet() throws InterruptedException {
    final BooleanLock bl = new BooleanLock();
    assertTrue(bl.waitToSetTrue(20));
    assertTrue(bl.isTrue());
    assertTrue(bl.waitToSetFalse(20));
    assertTrue(!bl.isTrue());
    assertTrue(!bl.waitToSetFalse(20));
    bl.setValue(true);
    assertTrue(!bl.waitToSetTrue(20));
  }

  private boolean mValue;

  public void testTricky1() throws InterruptedException {
    final BooleanLock bl = new BooleanLock(true);
    mValue = false;
    final Thread t = new Thread() {
        @Override
        public synchronized void run() {
          try {
            mValue = bl.waitToSetTrue(500);
          } catch (final InterruptedException e) {
            // ok -- leaves mValue false
          }
        }
      };
    t.start();
    Thread.sleep(50);
    bl.setValue(false);
    t.join();
    assertTrue(bl.isTrue());
    assertTrue(mValue);
  }

  public void testTricky2() throws InterruptedException {
    final BooleanLock bl = new BooleanLock(true);
    mValue = true;
    final Thread t = new Thread(new Runnable() {
        @Override
        public synchronized void run() {
          try {
            mValue = bl.waitToSetTrue(20);
          } catch (final InterruptedException e) {
            mValue = false;
          }
        }
      });
    t.start();
    t.join();
    assertTrue(bl.isTrue());
    assertTrue(!mValue);
  }

  public void testTricky3() throws InterruptedException {
    final BooleanLock bl = new BooleanLock(true);
    mValue = false;
    final Thread t = new Thread(new Runnable() {
        @Override
        public synchronized void run() {
          try {
            mValue = bl.waitUntilStateIs(false, 1000000L);
          } catch (final InterruptedException e) {
            // ok -- leaves mValue false
          }
          bl.setValue(true);
        }
      });
    t.start();
    Thread.sleep(50);
    synchronized (bl) {
      bl.notifyAll();
    }
    Thread.sleep(10);
    synchronized (bl) {
      bl.notifyAll();
    }
    Thread.sleep(30);
    bl.setValue(false);
    t.join();
    assertTrue(bl.isTrue());
    assertTrue(mValue);
  }

}
