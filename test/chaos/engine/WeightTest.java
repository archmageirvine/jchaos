package chaos.engine;

import chaos.common.Attribute;
import chaos.common.State;
import chaos.common.dragon.EmeraldDragon;
import chaos.common.growth.Fire;
import chaos.common.inanimate.FenceHorizontal;
import chaos.common.inanimate.Generator;
import chaos.common.inanimate.Pentagram;
import chaos.common.inanimate.Pool;
import chaos.common.inanimate.ShadowCity;
import chaos.common.inanimate.WaspNest;
import chaos.common.inanimate.Web;
import chaos.common.monster.Aesculapius;
import chaos.common.monster.BasaltGolem;
import chaos.common.monster.BirdLord;
import chaos.common.monster.EarthElemental;
import chaos.common.monster.FireDemon;
import chaos.common.monster.Iridium;
import chaos.common.monster.Lion;
import chaos.common.monster.MindFlayer;
import chaos.common.monster.Solar;
import chaos.common.monster.WoodElf;
import chaos.common.mythos.MrStrong;
import chaos.common.wizard.Wizard1;
import junit.framework.TestCase;

/**
 * Test the corresponding class.
 * @author Sean A. Irvine
 */
public class WeightTest extends TestCase {

  public void test() {
    assertEquals(0, Weight.lethality(null), 1E-12);
    final Lion l = new Lion();
    assertEquals(19.90, Weight.lethality(l), 0.05);
    l.setState(State.DEAD);
    assertEquals(0.5, Weight.lethality(l), 0.05);
    l.setState(State.ACTIVE);
    l.set(Attribute.LIFE, 100);
    assertEquals(24.43, Weight.lethality(l), 0.05);
    assertEquals(0.64, Weight.lethality(new FenceHorizontal()), 0.05);
    final Wizard1 w = new Wizard1();
    w.setState(State.ACTIVE);
    assertEquals(152.58, Weight.lethality(w), 0.05);
    assertEquals(72.37, Weight.lethality(new Generator()), 0.05);
    assertEquals(68.16, Weight.lethality(new FireDemon()), 0.05);
    assertEquals(67.14, Weight.lethality(new EmeraldDragon()), 0.05);
    assertEquals(40.34, Weight.lethality(new BirdLord()), 0.05);
    assertEquals(29.47, Weight.lethality(new MrStrong()), 0.05);
    assertEquals(27.54, Weight.lethality(new Solar()), 0.05);
    assertEquals(28.24, Weight.lethality(new ShadowCity()), 0.05);
    assertEquals(47.07, Weight.lethality(new EarthElemental()), 0.05);
    assertEquals(5.80, Weight.lethality(new Pool()), 0.05);
    assertEquals(12.37, Weight.lethality(new Fire()), 0.05);
    assertEquals(17.02, Weight.lethality(new WoodElf()), 0.05);
    assertEquals(15.78, Weight.lethality(new MindFlayer()), 0.05);
    assertEquals(54.99, Weight.lethality(new Iridium()), 0.05);
    assertEquals(1.51, Weight.lethality(new Web()), 0.05);
    assertEquals(2.13, Weight.lethality(new Pentagram()), 0.05);
    assertEquals(8.75, Weight.lethality(new WaspNest()), 0.05);
    assertEquals(21.00, Weight.lethality(new Aesculapius()), 0.05);
    assertEquals(19.27, Weight.lethality(new BasaltGolem()), 0.05);
  }
}
