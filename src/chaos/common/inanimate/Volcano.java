package chaos.common.inanimate;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Animateable;
import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Caster;
import chaos.common.Inanimate;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.NoFlyOver;
import chaos.common.State;
import chaos.common.growth.Magma;
import chaos.common.monster.GoblinBomb;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.ChaosProperties;
import chaos.util.Random;

/**
 * Volcano.
 * @author Sean A. Irvine
 * @author Stephen Smith
 */
public class Volcano extends MaterialMonster implements Inanimate, Bonus, Animateable, NoFlyOver {

  /**
   * Mean turns to expected death.
   */
  private static final int DEATH_EXPECTED = ChaosProperties.properties().getIntProperty("chaos.volcano.expected", 40);

  {
    setDefault(Attribute.LIFE, 63);
    setDefault(Attribute.LIFE_RECOVERY, 7);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setDefault(Attribute.SPECIAL_COMBAT, -10);
    setSpecialCombatApply(Attribute.AGILITY);
  }

  @Override
  public int getCastRange() {
    return 13;
  }

  @Override
  public long getLosMask() {
    return 0x0L;
  }

  @Override
  public int getBonus() {
    return 2;
  }

  @Override
  public int getCastFlags() {
    return CAST_GROWTH | CAST_EMPTY | CAST_LOS | CAST_DEAD;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell c, final Cell casterCell) {
    CastUtils.castStone(this, caster, c, casterCell);
  }

  @Override
  public int getDefaultWeight() {
    return 0;
  }

  @Override
  public Actor getAnimatedForm() {
    final Actor a = new GoblinBomb();
    a.setOwner(getOwner());
    return a;
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return null;
  }

  @Override
  public boolean update(final World world, final Cell cell) {
    if (cell != null && Random.nextInt(DEATH_EXPECTED) == 0) {
      // Death of volcano.
      cell.notify(new CellEffectEvent(cell, CellEffectType.FIREBALL_EXPLODE));
      for (final Cell c : world.getCells(cell.getCellNumber(), 1, 1, false)) {
        final Actor a = c.peek();
        if (a != null && a.getState() != State.DEAD) {
          c.notify(new CellEffectEvent(c, CellEffectType.ORANGE_CIRCLE_EXPLODE));
          a.set(Attribute.LIFE, 2);
          c.notify(new CellEffectEvent(c, CellEffectType.REDRAW_CELL));
        }
      }
      cell.pop();
      final Magma magma = new Magma();
      magma.setOwner(getOwner());
      cell.push(magma);
      return true;
    }
    return super.update(world, cell);
  }

}
