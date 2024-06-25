package chaos.common.inanimate;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Animateable;
import chaos.common.Attribute;
import chaos.common.Blocker;
import chaos.common.Bonus;
import chaos.common.Caster;
import chaos.common.Inanimate;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.Multiplicity;
import chaos.common.NoFlyOver;
import chaos.common.Realm;
import chaos.common.monster.Amphisbaena;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * Magic glass.
 * @author Sean A. Irvine
 */
public class MagicGlass extends MaterialMonster implements Inanimate, Animateable, Bonus, Multiplicity, Blocker, NoFlyOver {

  {
    setDefault(Attribute.LIFE, 63);
    setDefault(Attribute.MAGICAL_RESISTANCE, 100);
    setRealm(Realm.MATERIAL);
    setDefault(Attribute.SPECIAL_COMBAT, 2);
    setSpecialCombatApply(Attribute.INTELLIGENCE);
  }

  @Override
  public int getCastRange() {
    return 6;
  }

  @Override
  public long getLosMask() {
    return ~0L;
  }

  @Override
  public int getCastFlags() {
    return CAST_DEAD | CAST_EMPTY | CAST_LOS;
  }

  @Override
  public Actor getAnimatedForm() {
    final Actor a = new Amphisbaena();
    a.setOwner(getOwner());
    return a;
  }

  @Override
  public int getBonus() {
    return 1;
  }

  @Override
  public int getMultiplicity() {
    return 4;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell c, final Cell casterCell) {
    if (caster != null && c != null) {
      if (casterCell != null) {
        c.notify(new WeaponEffectEvent(casterCell, c, WeaponEffectType.TREE_CAST_EVENT, this));
      }
      c.notify(new CellEffectEvent(c, CellEffectType.MONSTER_CAST_EVENT, this));
      setOwner(caster.getOwner());
      c.push(this);
      c.notify(new CellEffectEvent(c, CellEffectType.REDRAW_CELL, this));
    }
    setDefault(Attribute.SPECIAL_COMBAT, 3);
  }

  @Override
  public Class<? extends Monster> reincarnation() {
    return null;
  }
}
