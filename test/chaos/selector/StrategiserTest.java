package chaos.selector;

import chaos.board.CastMaster;
import chaos.board.World;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.monster.Lion;
import chaos.common.spell.Bury;
import chaos.common.spell.RaiseDead;
import chaos.common.wizard.Wizard1;

/**
 * Tests this selector.
 * @author Sean A. Irvine
 */
public class StrategiserTest extends AbstractSelectorTest {

  @Override
  public Selector getSelector() {
    final World w = new World(5, 5);
    final Wizard1 wiz1 = new Wizard1();
    wiz1.setState(State.ACTIVE);
    w.getCell(2).push(wiz1);
    return new Strategiser(wiz1, w, new CastMaster(w));
  }

  public void testGetScore1() {
    final World w = new World(1, 13);
    final Wizard1 wiz1 = new Wizard1();
    wiz1.setState(State.ACTIVE);
    wiz1.set(PowerUps.WAND, 12);
    w.getCell(2).push(wiz1);
    final Strategiser c = new Strategiser(wiz1, w, new CastMaster(w));
    final int[] s = new int[OrdinarySelector.MAX_STAT];
    s[OrdinarySelector.EMPTY] = 0;
    assertEquals(0, c.getScore(new Lion(), s, w.getCell(2)));
    assertEquals(0, c.getScore(new RaiseDead(), s, w.getCell(2)));
    final Lion l = new Lion();
    l.setState(State.DEAD);
    w.getCell(10).push(l);
    int cnt = 0;
    while (cnt++ < 100 && c.getScore(new RaiseDead(), s, w.getCell(2)) == 0) {
      // do nothing
    }
    assertTrue(cnt < 100);
  }

  public void testGetScore2() {
    final World w = new World(1, 13);
    final Wizard1 wiz1 = new Wizard1();
    wiz1.setState(State.ACTIVE);
    w.getCell(2).push(wiz1);
    final Strategiser c = new Strategiser(wiz1, w, new CastMaster(w));
    final int[] s = new int[OrdinarySelector.MAX_STAT];
    s[OrdinarySelector.EMPTY] = 0;
    assertEquals(0, c.getScore(new Lion(), s, w.getCell(2)));
    assertEquals(0, c.getScore(new RaiseDead(), s, w.getCell(2)));
    final Lion l = new Lion();
    l.setState(State.DEAD);
    w.getCell(6).push(l);
    assertTrue(c.getScore(new RaiseDead(), s, w.getCell(2)) > 0);
    w.getCell(5).push(new Lion());
    assertEquals(0, c.getScore(new RaiseDead(), s, w.getCell(2)));
  }

  public void testGetScore3() {
    final World w = new World(1, 13);
    final Wizard1 wiz1 = new Wizard1();
    wiz1.setState(State.ACTIVE);
    w.getCell(2).push(wiz1);
    final Strategiser c = new Strategiser(wiz1, w, new CastMaster(w));
    final int[] s = new int[OrdinarySelector.MAX_STAT];
    final Lion l = new Lion();
    l.setState(State.DEAD);
    w.getCell(6).push(l);
    final int score = c.getScore(new Bury(), s, w.getCell(2));
    assertTrue(String.valueOf(score), score > 0);
    w.getCell(5).push(new Lion());
    int cnt = 0;
    while (cnt++ < 100 && score == 0) {
      // do nothing
    }
    assertTrue(cnt < 100);
  }

}

