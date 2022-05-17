package chaos.selector;

import chaos.board.CastMaster;
import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Castable;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.common.free.Arborist;
import chaos.common.free.MassResurrect;
import chaos.common.free.Repulsion;
import chaos.common.inanimate.MagicCastle;
import chaos.common.monster.HigherDevil;
import chaos.common.monster.Iridium;
import chaos.common.monster.Python;
import chaos.common.monster.Skeleton;
import chaos.common.spell.Bury;
import chaos.common.spell.RaiseDead;
import chaos.common.spell.Stop;
import chaos.common.wizard.Wizard1;

/**
 * Tests this selector.
 *
 * @author Sean A. Irvine
 */
public class OrdinarySelectorTest extends AbstractSelectorTest {

  @Override
  public Selector getSelector() {
    final World w = new World(5, 5);
    final Wizard1 wiz1 = new Wizard1();
    wiz1.setState(State.ACTIVE);
    w.getCell(2).push(wiz1);
    return new OrdinarySelector(wiz1, w, new CastMaster(w));
  }

  @Override
  public void testBonusSelect() {
    super.testBonusSelect();
    final Castable[] c = {
      new HigherDevil(),
      new Python()
    };
    final Castable[] d = getSelector().selectBonus(c, 1);
    assertEquals(1, d.length);
    assertEquals(c[0], d[0]);
  }

  private void checkScore(final OrdinarySelector s, final int[] sundry, final Cell c2, final Castable c, final int value, final int delta) {
    final int sc = s.getScore(c, sundry, c2);
    assertTrue(String.valueOf(sc), sc >= value - delta && sc <= value + delta);
  }

  private void checkScore(final OrdinarySelector s, final int[] sundry, final Cell c2, final Castable c, final int value) {
    checkScore(s, sundry, c2, c, value, 20);
  }

  public void testGetScore() {
    final World w = new World(5, 5);
    final Wizard1 wiz1 = new Wizard1();
    wiz1.setState(State.ACTIVE);
    final Cell c2 = w.getCell(2);
    c2.push(wiz1);
    final OrdinarySelector s = new OrdinarySelector(wiz1, w, new CastMaster(w));
    final int[] sundry = new int[OrdinarySelector.MAX_STAT];
    checkScore(s, sundry, c2, new Arborist(), 82);
    checkScore(s, sundry, c2, new Skeleton(), 0, 0);
    checkScore(s, sundry, c2, new Iridium(), 0, 0);
    wiz1.set(PowerUps.DOUBLE, 1);
    checkScore(s, sundry, c2, new Arborist(), 1, 0);
    wiz1.set(PowerUps.DOUBLE, 0);
    wiz1.set(PowerUps.TRIPLE, 1);
    checkScore(s, sundry, c2, new Iridium(), 0, 0);
    checkScore(s, sundry, c2, new Arborist(), 1, 0);
    wiz1.set(PowerUps.TRIPLE, 0);
    wiz1.set(PowerUps.ARBORIST, 1);
    checkScore(s, sundry, c2, new Arborist(), 1, 0);
    wiz1.set(PowerUps.ARBORIST, 0);
    sundry[OrdinarySelector.EMPTY] = 1;
    checkScore(s, sundry, c2, new Arborist(), 66);
    checkScore(s, sundry, c2, new Iridium(), 161);
    final int scC = s.getScore(new MagicCastle(), sundry, c2);
    sundry[OrdinarySelector.MEDITATIONS] = 400;
    assertTrue(s.getScore(new MagicCastle(), sundry, c2) < scC);
    final int scS = s.getScore(new Stop(), sundry, c2);
    sundry[OrdinarySelector.FASTEST] = 6;
    assertTrue(s.getScore(new Stop(), sundry, c2) > scS - 18);
    final int scR = s.getScore(new Repulsion(), sundry, c2);
    sundry[OrdinarySelector.EMPTY] = 1;
    assertTrue(s.getScore(new Repulsion(), sundry, c2) < scR + 18);
    final int scRD = s.getScore(new RaiseDead(), sundry, c2);
    final int scMR = s.getScore(new MassResurrect(), sundry, c2);
    final int scB = s.getScore(new Bury(), sundry, c2);
    sundry[OrdinarySelector.CORPSES] = 9;
    assertTrue(s.getScore(new RaiseDead(), sundry, c2) > scRD - 18);
    assertTrue(s.getScore(new MassResurrect(), sundry, c2) > scMR - 18);
    assertTrue(s.getScore(new Bury(), sundry, c2) > scB - 18);
  }

  public void testSelect() {
    final World w = new World(5, 5);
    final Wizard1 wiz1 = new Wizard1();
    final Cell c2 = w.getCell(2);
    c2.push(wiz1);
    final OrdinarySelector s = new OrdinarySelector(wiz1, w, new CastMaster(w));
    final Castable[] list = {new Iridium()};
    assertNull(s.select(list, false)[0]);
    wiz1.setState(State.ACTIVE);
    assertEquals(list[0], s.select(list, false)[0]);
    c2.pop();
    assertNull(s.select(list, false)[0]); // wizard no longer found
  }
}

