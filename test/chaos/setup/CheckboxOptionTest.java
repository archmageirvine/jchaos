package chaos.setup;

import javax.swing.JCheckBox;

import irvine.TestUtils;
import junit.framework.TestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class CheckboxOptionTest extends TestCase {

  public void testEnum() {
    TestUtils.testEnum(CheckboxOption.class, "[FSEM, TEXAS, COMBAT_CAP, LIFE_LEECH, RANDOM_PLAY_ORDER]");
  }

  public void testCheckBox() {
    final JCheckBox checkBox = CheckboxOption.TEXAS.getCheckBox();
    assertEquals("If selected, then a spell must be discarded for each spell selected.", checkBox.getToolTipText());
  }
}
