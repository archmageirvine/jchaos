package chaos.common;

import irvine.StandardIoTestCase;

/**
 * Tests the corresponding class.
 * @author Sean A. Irvine
 */
public class DumpCreatureTest extends StandardIoTestCase {

  private static final String LS = System.lineSeparator();

  public void test() throws Exception {
    DumpCreature.main(new String[] {"chaos.common.monster.Orc"});
    assertEquals("Orc" + LS
      + "The orc is typical of many exceedingly common species in having limited abilities. However, sometimes when an orc is cast he will con a few friends into joining your side. Up to eight orcs in total may be obtained. A group of orcs can be useful in the early stages of the game." + LS
      + "Frequency:          80" + LS
      + "LOS mask:           387e7e3e183800" + LS
      + "   +--------+" + LS
      + "   |........|" + LS
      + "   |..***...|" + LS
      + "   |.******.|" + LS
      + "   |.******.|" + LS
      + "   |..*****.|" + LS
      + "   |...**...|" + LS
      + "   |..***...|" + LS
      + "   |........|" + LS
      + "   +--------+" + LS
      + "Realm:              material" + LS
      + "Life:               4 0" + LS
      + "MR:                 57 0" + LS
      + "Intelligence:       33 0" + LS
      + "Agility:            27 0" + LS
      + "Combat:             2 0" + LS
      + "Movement:           1 0" + LS
      + "Ranged Combat:      0 0" + LS
      + "Range:              0 0" + LS
      + "Special Combat:     0 0" + LS
      + "Flying:             false" + LS
      + "Reincarnation:      chaos.common.monster.Spider->chaos.common.monster.GiantBeetle->null" + LS
      + "Promotion:          chaos.common.monster.MightyOrc 3" + LS
      + "Is a humanoid" + LS, getOut());
  }
}
