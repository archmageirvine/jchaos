package chaos.common.free;

import chaos.board.World;
import chaos.common.Attribute;
import chaos.common.Castable;
import chaos.common.PowerUps;
import chaos.common.wizard.Wizard1;
import chaos.util.CellEffectType;

/**
 * Tests this spell.
 *
 * @author Sean A. Irvine
 */
public class ChaosLordTest extends AbstractFreeCastableTest {

  @Override
  public Castable getCastable() {
    return new ChaosLord();
  }

  private void checkAfterState(final Wizard1 w) {
    assertEquals(1, w.get(PowerUps.BOW));
    assertEquals(1, w.get(PowerUps.FLYING));
    assertEquals(1, w.get(PowerUps.REFLECT));
    assertEquals(1, w.get(PowerUps.FIRE_SHIELD));
    assertEquals(1, w.get(PowerUps.FLOOD_SHIELD));
    assertEquals(1, w.get(PowerUps.EARTHQUAKE_SHIELD));
    assertEquals(1, w.get(PowerUps.ATTACK_ANY_REALM));
    assertEquals(6, w.get(PowerUps.TRIPLE));
    assertEquals(Attribute.LIFE.max(), w.get(Attribute.LIFE));
    assertEquals(Attribute.INTELLIGENCE.max(), w.get(Attribute.INTELLIGENCE));
    assertEquals(Attribute.AGILITY.max(), w.get(Attribute.AGILITY));
    assertEquals(Attribute.MAGICAL_RESISTANCE.max(), w.get(Attribute.MAGICAL_RESISTANCE));
    assertEquals(Attribute.SHOTS.max(), w.get(Attribute.SHOTS));
    assertEquals(16, w.get(Attribute.COMBAT));
    assertEquals(15, w.get(Attribute.RANGED_COMBAT));
    assertEquals(15, w.get(Attribute.RANGE));
    assertEquals(15, w.get(Attribute.MOVEMENT));
  }

  public void testCast() {
    final ChaosLord x = new ChaosLord();
    assertEquals(Castable.CAST_SINGLE, x.getCastFlags());
    final Wizard1 w = new Wizard1();
    final World world = new World(1, 1);
    castAndListenCheck(x, world, w, 0, CellEffectType.REDRAW_CELL, CellEffectType.AUDIO, CellEffectType.SHIELD_GRANTED);
    checkAfterState(w);
    assertEquals(7, w.get(Attribute.LIFE_RECOVERY));
    assertEquals(7, w.get(Attribute.INTELLIGENCE_RECOVERY));
    assertEquals(7, w.get(Attribute.AGILITY_RECOVERY));
    assertEquals(7, w.get(Attribute.MAGICAL_RESISTANCE_RECOVERY));
    assertTrue(w.is(PowerUps.FLYING));
    assertEquals(Attribute.LIFE, w.getCombatApply());
    assertEquals(Attribute.LIFE, w.getRangedCombatApply());
    w.set(Attribute.LIFE_RECOVERY, 8);
    w.set(Attribute.AGILITY_RECOVERY, 8);
    w.set(Attribute.INTELLIGENCE_RECOVERY, 8);
    w.set(Attribute.MAGICAL_RESISTANCE_RECOVERY, 8);
    w.setCombatApply(null);
    w.setRangedCombatApply(null);
    w.set(Attribute.COMBAT, 1);
    w.set(Attribute.RANGE, 0);
    w.set(Attribute.RANGED_COMBAT, 0);
    x.cast(world, w, world.getCell(0));
    checkAfterState(w);
    assertEquals(15, w.get(Attribute.LIFE_RECOVERY));
    assertEquals(15, w.get(Attribute.INTELLIGENCE_RECOVERY));
    assertEquals(15, w.get(Attribute.AGILITY_RECOVERY));
    assertEquals(15, w.get(Attribute.MAGICAL_RESISTANCE_RECOVERY));
    assertEquals(Attribute.LIFE, w.getCombatApply());
    assertEquals(Attribute.LIFE, w.getRangedCombatApply());
    nullParametersCastCheck(x, world, w);
  }

}
