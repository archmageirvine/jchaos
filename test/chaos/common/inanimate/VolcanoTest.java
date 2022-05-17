package chaos.common.inanimate;

import chaos.common.growth.Magma;
import irvine.TestUtils;
import chaos.board.Cell;
import chaos.board.World;
import chaos.common.AbstractMonsterTest;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.State;
import chaos.common.monster.Lion;
import chaos.common.wizard.Wizard1;

/**
 * Tests this inanimate.
 *
 * @author Sean A. Irvine
 */
public class VolcanoTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Volcano();
  }

  @Override
  public void testReincarnation() {
    assertEquals(null, new Volcano().reincarnation());
  }

  public void test() {
    final Volcano v = new Volcano();
    v.cast(null, null, null, null);
    assertEquals(Attribute.AGILITY, v.getSpecialCombatApply());
    final World w = new World(3, 3);
    final Wizard1 wiz = new Wizard1();
    wiz.setOwner(2);
    wiz.setState(State.ACTIVE);
    w.getCell(0).push(wiz);
    final Cell c = w.getCell(4);
    v.cast(w, wiz, c, w.getCell(0));
    assertEquals(2, v.getOwner());
    assertEquals(v, c.pop());
    v.setOwner(0);
    final Lion l = new Lion();
    w.getCell(1).push(l);
    final Lion l2 = new Lion();
    l2.setState(State.DEAD);
    l2.set(Attribute.LIFE, 0);
    w.getCell(2).push(l2);
    int timeToDie = 0;
    while (timeToDie < 9000 && !v.update(w, c)) {
      ++timeToDie;
    }
    assertTrue(timeToDie < 8000);
    assertTrue(c.peek() instanceof Magma);
    assertEquals(c.peek().getOwner(), v.getOwner());
    assertEquals(2, l.get(Attribute.LIFE));
    assertEquals(2, wiz.get(Attribute.LIFE));
    assertEquals(0, l2.get(Attribute.LIFE));
  }

  public void testNasty() {
    final Object obj = TestUtils.getField("DEATH_EXPECTED", Volcano.class);
    assertTrue(obj instanceof Integer);
    assertTrue((Integer) obj > 0);
  }
}
