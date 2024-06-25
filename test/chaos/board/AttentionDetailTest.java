package chaos.board;

import junit.framework.TestCase;

/**
 * JUnit tests for the AttentionDetail class.
 * @author Sean A. Irvine
 */
public class AttentionDetailTest extends TestCase {


  public void test() {
    assertEquals(-1, AttentionDetail.UNKNOWN);
    final AttentionDetail d = new AttentionDetail();
    assertEquals(AttentionDetail.UNKNOWN, d.mEngaged);
    assertEquals(AttentionDetail.UNKNOWN, d.mSquaredMovementPoints);
    assertFalse(d.mEngageIsCompulsory);
  }
}
