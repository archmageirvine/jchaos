package chaos.board;

import junit.framework.TestCase;

/**
 * JUnit tests for the CellEvent class.
 *
 * @author Sean A. Irvine
 */
public class CellEventTest extends TestCase {


  public void test() {
    final CellEvent ce = new CellEvent(6);
    assertEquals(6, ce.getCellNumber());
  }

}
