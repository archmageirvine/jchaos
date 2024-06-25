package chaos.util;

import chaos.board.Cell;
import chaos.common.monster.Lion;
import junit.framework.TestCase;

/**
 * JUnit tests for the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class AudioEventTest extends TestCase {

  public void test0() {
    final Lion l = new Lion();
    final AudioEvent e = new AudioEvent(1, l, "sound");
    assertEquals(CellEffectType.AUDIO, e.getEventType());
    assertEquals(1, e.getCellNumber());
    assertEquals("sound", e.getSoundEffect());
  }

  public void test1() {
    final Lion l = new Lion();
    final AudioEvent e = new AudioEvent(new Cell(1), l, "sound");
    assertEquals(CellEffectType.AUDIO, e.getEventType());
    assertEquals(1, e.getCellNumber());
    assertEquals("sound", e.getSoundEffect());
  }

}
