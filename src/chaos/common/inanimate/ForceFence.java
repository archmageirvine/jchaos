package chaos.common.inanimate;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Animateable;
import chaos.common.Attribute;
import chaos.common.Blocker;
import chaos.common.Caster;
import chaos.common.Inanimate;
import chaos.common.Multiplicity;
import chaos.common.NoFlyOver;
import chaos.common.Realm;
import chaos.common.monster.Nilbog;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * Force fence.
 * @author Sean A. Irvine
 */
public class ForceFence extends Actor implements Inanimate, Animateable, Multiplicity, Blocker, NoFlyOver {

  {
    setDefault(Attribute.LIFE, 20);
    setDefault(Attribute.MAGICAL_RESISTANCE, 60);
    setRealm(Realm.SUBHYADIC);
  }

  @Override
  public int getCastRange() {
    return 25;
  }

  @Override
  public long getLosMask() {
    return 0L;
  }

  @Override
  public int getCastFlags() {
    return CAST_DEAD | CAST_EMPTY;
  }

  @Override
  public Actor getAnimatedForm() {
    final Actor a = new Nilbog();
    a.setOwner(getOwner());
    return a;
  }

  @Override
  public int getMultiplicity() {
    return 8;
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
  }
}
