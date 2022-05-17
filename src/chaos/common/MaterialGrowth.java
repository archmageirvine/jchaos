package chaos.common;

import java.util.Set;

import chaos.Chaos;
import chaos.board.Cell;
import chaos.board.World;
import chaos.common.growth.Balefire;
import chaos.common.growth.Earthquake;
import chaos.common.growth.Fire;
import chaos.common.wizard.Wizard;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.Random;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * Convenience class to implement a standard growth.
 *
 * @author Sean A. Irvine
 */
public abstract class MaterialGrowth extends Actor implements Growth {

  @Override
  public long getLosMask() {
    return 0x0L;
  }

  {
    setRealm(Realm.MATERIAL);
  }

  @Override
  public int getCastFlags() {
    return Castable.CAST_LOS | Castable.CAST_EMPTY;
  }

  protected int getGrowthCombat() {
    return 15;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell c, final Cell casterCell) {
    if (c != null && caster != null) {
      setOwner(caster.getOwner());
      if (casterCell != null) {
        c.notify(new WeaponEffectEvent(casterCell, c, WeaponEffectType.TREE_CAST_EVENT, this));
      }
      c.notify(new CellEffectEvent(c, CellEffectType.MONSTER_CAST_EVENT, this));
      c.push(this);
      c.notify(new CellEffectEvent(c, CellEffectType.REDRAW_CELL));
    }
  }

  /**
   * Called to make special additional checks as to whether growth
   * can take place.  This is used to implement the fire shield etc.
   *
   * @param cell cell we are trying to grow into
   * @return true if we can grow
   */
  public abstract boolean canGrowOver(final Cell cell);

  /**
   * Grow this growth.  How growth happens depends on the type
   * of growth.  The input cell is assumed to be valid and it
   * is expected that this object is the top actor in the specified
   * cell. Growths do not grow on top of each other.
   *
   * @param cell the cell to be grown
   * @param world world to grow in
   */
  @Override
  public void grow(final int cell, final World world) {
    assert world.actor(cell) == this;

    // only active growths can grow
    if (getState() != State.ACTIVE || is(PowerUps.NO_GROW)) {
      return;
    }

    final int gType = getGrowthType();

    // choose a target randomly and fairly
    int j = 1;
    int target = -1;
    final int width = world.width();
    for (final Cell z : world.getCells(cell, 1, 1, false)) {
      final int t = z.getCellNumber();
      if ((gType != GROW_FOUR_WAY || t == cell - 1 || t == cell + 1 || t == cell - width || t == cell + width) && Random.nextInt(j++) == 0) {
        target = t;
      }
    }

    // quick exit if nowhere to grow
    if (target == -1) {
      return;
    }

    // what are we trying to grow over (may be null)
    final Actor a = world.actor(target);

    /*
      GROW_OVER only grow over monsters.  This means blobs will never grow
      over trees, walls, or other growths. GROW_OVER don't go over their
      owners monsters.  If the monster is later retrieved by killing the
      growth the monster will come back at half strength.  However, an
      attempt to grow over a wizard will result in an attempt to call
      reinstate() on the wizard---i.e. often the wizard will die.

      GROW_BY_COMBAT will nearly anything. Exceptions include: another
      active GROW_BY_COMBAT or GROW_FOUR_WAY actor owned by the same player,
      pits, radioactive land. The attack is performed with combat 15.

      GROW_FOUR_WAY is same as GROW_BY_COMBAT but the growth can only
      expand in the four cardinal directions.

      There are special exceptions in the case of fire shield, flood
      shield, and earthquake shield.  These are handled by the canGrowOver()
      function of the specific growths.

      All growths can go onto empty space, so no extra testing is needed
      in that case.
    */

    // check any special constraints
    final Cell targetCell = world.getCell(target);
    if (!canGrowOver(targetCell)) {
      return;
    }

    final boolean growUsingCombat = gType == GROW_BY_COMBAT || gType == GROW_FOUR_WAY;

    // check general constraints
    if (a != null) {
      if (gType == GROW_OVER) {
        if (!(a instanceof Monster) || a.getOwner() == getOwner()) {
          return;
        }
        // cells containing a wizard
        if (targetCell.contains(Wizard.class) && !targetCell.reinstate()) {
          return;
        }
      } else if (growUsingCombat) {
        final int gt;
        // This is the bit that prevents fires etc. from burning itself
        if (a instanceof Growth && a.getOwner() == getOwner() && a.getState() == State.ACTIVE && ((gt = ((Growth) a).getGrowthType()) == GROW_BY_COMBAT || gt == GROW_FOUR_WAY)) {
          return;
        }
        if (a instanceof Killer) {
          return;
        }

        // perform the combat (by-passes normal combat routines which would
        // prevent attack of own creatures)
        final int life = a.get(Attribute.LIFE) - getGrowthCombat();
        if (life <= 0) {
          if (!targetCell.reinstate()) {
            // target survived combat, via reincarnation etc.
            return;
          }
          CastUtils.handleScoreAndBonus(Chaos.getChaos().getWorld().getWizardManager().getWizard(getOwner()), a, null);
          if (this instanceof Fire && !(this instanceof Balefire) && a.getRealm() == Realm.ETHERIC) {
            // Promote a fire to balefire
            final Cell currentCell = world.getCell(cell);
            currentCell.pop();
            final Actor promotion = new Balefire();
            promotion.setOwner(this.getOwner());
            promotion.setState(this.getState());
            currentCell.push(promotion);
          }
          // Success, prepare cell for growth by removing all current contents of the cell
          while (targetCell.pop() != null) {
            // DO NOTHING
          }
        } else {
          // combat failed
          a.set(Attribute.LIFE, life);
          return;
        }
      }
    }

    // Earthquake screen shake
    if (a instanceof Earthquake) {
      targetCell.notify(new CellEffectEvent(targetCell, CellEffectType.EARTHQUAKE, a));
    }

    // prepare a new object of correct type and details and install it
    // in the target cell
    final Actor sprout = (Actor) FrequencyTable.instantiate(sproutClass());
    sprout.setOwner(getOwner());
    sprout.setState(getState());
    sprout.setRealm(getRealm());
    targetCell.push(sprout);
  }

  @Override
  public void filter(final Set<Cell> targets, final Caster caster, final World world) {
    GrowthHelper.filter(targets, caster, world);
  }

  @Override
  public Class<? extends Actor> sproutClass() {
    return getClass();
  }
}
