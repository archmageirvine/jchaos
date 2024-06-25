package chaos.common;

import chaos.common.growth.Fire;
import chaos.common.monster.Bolter;
import chaos.common.monster.Eagle;
import chaos.common.monster.Lion;
import chaos.common.monster.Ogre;
import chaos.common.monster.Orc;
import chaos.common.wizard.Wizard1;
import junit.framework.TestCase;


/**
 * Test the bipedal property is used in a few cases.
 *
 * @author Sean A. Irvine
 */
public class HumanoidTest extends TestCase {


  public void test() {
    assertTrue(!(new Lion() instanceof Humanoid));
    assertTrue(!(new Eagle() instanceof Humanoid));
    assertTrue(!(new Fire() instanceof Humanoid));
    assertTrue(!(new Bolter() instanceof Humanoid));
    assertTrue(!(new Object() instanceof Humanoid));
    assertTrue(new Orc() instanceof Humanoid);
    assertTrue(new Ogre() instanceof Humanoid);
    assertTrue(new Wizard1() instanceof Humanoid);
  }
}
