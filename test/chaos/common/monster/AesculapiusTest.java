package chaos.common.monster;

import chaos.Chaos;
import chaos.board.World;
import chaos.common.AbstractMonsterTest;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.PowerUps;
import chaos.common.State;
import chaos.util.CombatUtils;

/**
 * Tests this monster.
 *
 * @author Sean A. Irvine
 */
public class AesculapiusTest extends AbstractMonsterTest {

  @Override
  public Castable getCastable() {
    return new Aesculapius();
  }

  public void test() {
    final Aesculapius a = new Aesculapius();
    assertTrue(a.is(PowerUps.ARCHERY));
    a.update(null, null);
    assertEquals(a.getDefault(Attribute.RANGED_COMBAT), a.get(Attribute.RANGED_COMBAT));
    assertEquals(a.getDefault(Attribute.COMBAT), a.get(Attribute.COMBAT));
    a.set(Attribute.COMBAT, 6);
    a.update(null, null);
    assertEquals(6 - a.getDefault(Attribute.COMBAT_RECOVERY), a.get(Attribute.COMBAT));
  }

  public void testHealing() {
    final World w = Chaos.getChaos().getWorld();
    final Lion l = new Lion();
    l.set(Attribute.LIFE, 1);
    l.setOwner(1);
    w.getCell(0).push(l);
    final Lion x = new Lion();
    x.set(Attribute.LIFE, 3);
    x.setOwner(5);
    w.getCell(1).push(x);
    final Lion y = new Lion();
    y.set(Attribute.LIFE, 0);
    y.setOwner(1);
    y.setState(State.DEAD);
    w.getCell(w.width()).push(x);
    final Aesculapius a = new Aesculapius();
    a.setOwner(1);
    w.getCell(w.width() + 1).push(a);
    CombatUtils.performSpecialCombat(w, Chaos.getChaos().getMoveMaster());
    assertEquals(1 - a.get(Attribute.SPECIAL_COMBAT), l.get(Attribute.LIFE));
    assertEquals(3, x.get(Attribute.LIFE));
    assertEquals(0, y.get(Attribute.LIFE));
  }
}
