package chaos.common.inanimate;

import java.util.HashSet;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Animateable;
import chaos.common.Attribute;
import chaos.common.Caster;
import chaos.common.Inanimate;
import chaos.common.MaterialMonster;
import chaos.common.Monster;
import chaos.common.NoFlyOver;
import chaos.common.Realm;
import chaos.common.State;
import chaos.common.monster.AirElemental;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.PolycellEffectEvent;
import chaos.util.Random;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * Vortex.
 *
 * @author Sean A. Irvine
 */
public class Vortex extends MaterialMonster implements Inanimate, Animateable, NoFlyOver {
  /** Maximum movement of a vortex. */
  private static final int MOVES = 2;

  /**
   * Actually move a vortex in the world.
   *
   * @param world world containing vortex
   * @param c cell containing vortex
   * @param moves moves remaining
   */
  private void moveVortex(final World world, final Cell c, final int moves) {
    if (moves == 0 || world == null || c == null) {
      // No movement remains.
      return;
    }
    // Choose new cell at random among the possible
    Cell t = null;
    int z = 1;
    for (final Cell cell : world.getCells(c.getCellNumber(), 1, 1, false)) {
      if (Random.nextInt(z++) == 0) {
        t = cell;
      }
    }
    if (t == null) {
      // No movement is possible, quickly exit
      return;
    }
    // Found somewhere to move
    final Actor a = t.peek();
    if (a instanceof Vortex) {
      // Two vortices collide, both are destroyed
      final Set<Cell> pc = new HashSet<>();
      pc.add(t);
      pc.add(c);
      world.notify(new PolycellEffectEvent(pc, CellEffectType.CORPSE_EXPLODE));
      t.reinstate();
      c.reinstate();
      world.notify(new PolycellEffectEvent(pc, CellEffectType.REDRAW_CELL));
      return;
    } else if (a != null) {
      world.getWarpSpace().warpOut(t, this);
    }
    // Finally move vortex and recursive call
    t.push(c.pop());
    t.notify(new CellEffectEvent(t, CellEffectType.REDRAW_CELL));
    moveVortex(world, t, moves - 1);
  }

  {
    setDefault(Attribute.LIFE, 40);
    setDefault(Attribute.LIFE_RECOVERY, 2);
    setDefault(Attribute.MAGICAL_RESISTANCE, 44);
    setDefault(Attribute.SPECIAL_COMBAT, -1);
    setRealm(Realm.MATERIAL);
    setSpecialCombatApply(Attribute.MOVEMENT);
  }
  @Override
  public int getCastRange() {
    return 7;
  }
  @Override
  public long getLosMask() {
    return 0x0038383838181810L;
  }
  @Override
  public int getCastFlags() {
    return CAST_DEAD | CAST_EMPTY | CAST_LOS;
  }
  @Override
  public int getDefaultWeight() {
    return 0;
  }
  @Override
  public Class<? extends Monster> reincarnation() {
    return null;
  }
  @Override
  public Actor getAnimatedForm() {
    final Actor a = new AirElemental();
    a.setOwner(getOwner());
    return a;
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
  @Override
  public boolean update(final World world, final Cell cell) {
    // When this is called for the first time the vortex is already marked
    // as having been moved.  Therefore, we reverse the usual logic and
    // reset the movement flag AFTER movement.
    if (isMoved() && getState() == State.ACTIVE) {
      moveVortex(world, cell, MOVES);
      setMoved(false);
    }
    return super.update(world, cell);
  }

}
