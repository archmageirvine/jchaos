package chaos;

import java.io.IOException;

import irvine.StandardIoTestCase;
import irvine.TestUtils;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class ChaosExperimentalTest extends StandardIoTestCase {

  public void test() throws IOException {
    final Chaos chaos = new Chaos(new Configuration(), false);
    ChaosExperimental.runExperimentalMode(chaos, "Ai,Ai", "Strategiser,Strategiser", 5, false);
    final String s = getOut();
    //mOldOut.println(s);
    TestUtils.containsAll(s,
      "ACTIVE",
      "DEAD",
      "id",
      "name",
      "state",
      "score",
      "life",
      "-------------",
      "Turn number: 5");
  }
}
