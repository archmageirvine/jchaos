package chaos.util;

import irvine.StandardIoTestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class TextGridTest extends StandardIoTestCase {

  private static final String LS = System.lineSeparator();

  public void test() {
    TextGrid.main(new String[] {"7", "11", "5"});
    assertEquals("#=========#=========#=========#=========#=========#=========#=========#" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "#=========#=========#=========#=========#=========#=========#=========#" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "#=========#=========#=========#=========#=========#=========#=========#" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "#=========#=========#=========#=========#=========#=========#=========#" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "#=========#=========#=========#=========#=========#=========#=========#" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "#=========#=========#=========#=========#=========#=========#=========#" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "#=========#=========#=========#=========#=========#=========#=========#" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "#=========#=========#=========#=========#=========#=========#=========#" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "#=========#=========#=========#=========#=========#=========#=========#" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "#=========#=========#=========#=========#=========#=========#=========#" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "#=========#=========#=========#=========#=========#=========#=========#" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+" + LS
      + "\" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \" | | | | \"" + LS
      + "#=========#=========#=========#=========#=========#=========#=========#" + LS, getOut());
  }
}

