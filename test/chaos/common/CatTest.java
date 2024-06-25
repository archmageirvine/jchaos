package chaos.common;

import chaos.common.growth.Fire;
import chaos.common.monster.Bolter;
import chaos.common.monster.Eagle;
import chaos.common.monster.Jaguar;
import chaos.common.monster.Leopard;
import chaos.common.monster.Lion;
import chaos.common.monster.SnowLeopard;
import chaos.common.monster.Tiger;
import chaos.common.wizard.Wizard1;
import junit.framework.TestCase;


/**
 * Test the corresponding class.
 *
 * @author Sean A. Irvine
 */
public class CatTest extends TestCase {


  public void test() {
    assertTrue(new Lion() instanceof Cat);
    assertTrue(new Jaguar() instanceof Cat);
    assertTrue(new Leopard() instanceof Cat);
    assertTrue(new SnowLeopard() instanceof Cat);
    assertTrue(new Tiger() instanceof Cat);
    assertTrue(!(new Eagle() instanceof Cat));
    assertTrue(!(new Fire() instanceof Cat));
    assertTrue(!(new Bolter() instanceof Cat));
    assertTrue(!(new Object() instanceof Cat));
    assertTrue(!(new Wizard1() instanceof Cat));
  }
}
