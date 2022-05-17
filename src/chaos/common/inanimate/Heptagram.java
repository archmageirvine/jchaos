package chaos.common.inanimate;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Animateable;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.CoreMonster;
import chaos.common.Inanimate;
import chaos.common.Monster;
import chaos.common.NoDeadImage;
import chaos.common.PowerUps;
import chaos.common.monster.Crocodile;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * Heptagram.
 * @author Sean A. Irvine
 */
public class Heptagram extends CoreMonster implements Inanimate, NoDeadImage, Animateable {

  {
    setDefault(Attribute.LIFE, 5);
    setDefault(Attribute.MAGICAL_RESISTANCE, 5);
    setDefault(Attribute.INTELLIGENCE, 5);
    setDefault(Attribute.SPECIAL_COMBAT, 2);
    setSpecialCombatApply(Attribute.RANGED_COMBAT);
    set(PowerUps.TALISMAN, 6);
  }

  @Override
  public long getLosMask() {
    return 0x0L;
  }

  @Override
  public int getCastRange() {
    return 3;
  }

  @Override
  public int getCastFlags() {
    return CAST_EMPTY | CAST_LOS;
  }

  @Override
  public Actor getAnimatedForm() {
    final Actor a = new Crocodile();
    a.setOwner(getOwner());
    return a;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell c, final Cell casterCell) {
    if (c != null) {
      if (caster != null) {
        setOwner(caster.getOwner());
      }
      if (casterCell != null) {
        c.notify(new WeaponEffectEvent(casterCell, c, WeaponEffectType.TREE_CAST_EVENT, this));
      }
      c.notify(new CellEffectEvent(c, CellEffectType.MONSTER_CAST_EVENT, this));
      c.push(this);
      c.notify(new CellEffectEvent(c, CellEffectType.REDRAW_CELL, this));
    }
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return null;
  }

  @Override
  public int getDefaultWeight() {
    return 60;
  }
}
