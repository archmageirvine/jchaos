package chaos.common.inanimate;

import java.util.HashSet;
import java.util.Set;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Bonus;
import chaos.common.Caster;
import chaos.common.Growth;
import chaos.common.Inanimate;
import chaos.common.Monster;
import chaos.common.NoFlyOver;
import chaos.common.Realm;
import chaos.common.State;
import chaos.common.wizard.Wizard;
import chaos.util.CastUtils;
import chaos.util.CellEffectEvent;
import chaos.util.CellEffectType;
import chaos.util.PolycellEffectEvent;
import chaos.util.Random;
import chaos.util.WeaponEffectEvent;
import chaos.util.WeaponEffectType;

/**
 * Tempest.
 *
 * @author Sean A. Irvine
 * @author Nicole More
 */
public class Tempest extends Actor implements Inanimate, Bonus, NoFlyOver {

  /** Maximum movement of a tempest. */
  private static final int MOVES = 5;

  /**
   * Actually move a tempest in the world.
   *
   * @param world world containing tempest
   * @param c cell containing tempest
   * @param moves moves remaining
   */
  private void moveTempest(final World world, final Cell c, final int moves) {
    if (moves == 0 || world == null || c == null) {
      // No movement remains.
      return;
    }
    c.notify(new CellEffectEvent(c, CellEffectType.HIGHLIGHT_EVENT));
    // Choose new cell at random among the possible
    Cell t = null;
    int z = 1;
    for (final Cell cell : world.getCells(c.getCellNumber(), 1, 1, false)) {
      final Actor a = cell.peek();
      if ((a == null || a instanceof Monster || a instanceof Tempest || a instanceof Growth) && Random.nextInt(z++) == 0) {
        t = cell;
      }
    }
    if (t == null) {
      // No movement is possible, quickly exit
      return;
    }
    // Found somewhere to move
    final Actor a = t.peek();
    if (a instanceof Tempest) {
      // Two tempests collide, both are destroyed
      final Set<Cell> pc = new HashSet<>();
      pc.add(t);
      pc.add(c);
      world.notify(new PolycellEffectEvent(pc, CellEffectType.CORPSE_EXPLODE));
      t.reinstate();
      c.reinstate();
      world.notify(new PolycellEffectEvent(pc, CellEffectType.REDRAW_CELL));
      return;
    } else if (a != null) {
      // Cell contains something needing handling as part of the move
      if (a.getState() == State.DEAD) {
        // Destroy the corpse
        t.reinstate();
      } else {
        int rCell = -1;
        for (int tries = 0; tries < 20; ++tries) {
          final int r = Random.nextInt(world.size());
          if (world.actor(r) == null) {
            rCell = r;
            break;
          }
        }
        if (rCell == -1) {
          // Couldn't find a new home, content of the cell must be destroyed
          final Wizard w = world.getWizardManager().getWizard(c.peek().getOwner());
          CastUtils.handleScoreAndBonus(w, a, c);
          t.notify(new CellEffectEvent(t, CellEffectType.CORPSE_EXPLODE));
          while (!t.reinstate()) {
            // keep applying reinstate until the cell is empty
          }
        } else {
          final Cell rc = world.getCell(rCell);
          // Change position of target object
          world.notify(new WeaponEffectEvent(t, rc, WeaponEffectType.TREE_CAST_EVENT, this));
          rc.push(t.pop());
          rc.notify(new CellEffectEvent(rc, CellEffectType.REDRAW_CELL));
          t.notify(new CellEffectEvent(t, CellEffectType.REDRAW_CELL));
        }
      }
    }

    // In very rare circumstances the tempest could die as a result of trying
    // to move.  An example is, tempest tries to move a GoblinBomb, but cannot.
    // The GoblinBomb explodes killing the tempest.
    if (c.peek() != null) {
      // Finally move tempest and recursive call
      t.push(c.pop());
      t.notify(new CellEffectEvent(t, CellEffectType.REDRAW_CELL));
      c.notify(new CellEffectEvent(c, CellEffectType.REDRAW_CELL));
      moveTempest(world, t, moves - 1);
    }
  }

  {
    setDefault(Attribute.LIFE, 40);
    setDefault(Attribute.LIFE_RECOVERY, 2);
    setDefault(Attribute.MAGICAL_RESISTANCE, 44);
    setRealm(Realm.MATERIAL);
  }
  @Override
  public int getCastRange() {
    return 7;
  }
  @Override
  public long getLosMask() {
    return 0x18FFFFFFFF7F7E38L;
  }
  @Override
  public int getCastFlags() {
    return CAST_DEAD | CAST_EMPTY | CAST_LOS;
  }
  @Override
  public int getBonus() {
    return 2;
  }
  @Override
  public int getDefaultWeight() {
    return 0;
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
    // When this is called for the first time the tempest is already marked
    // as having been moved.  Therefore, we reverse the usual logic and
    // reset the movement flag AFTER movement.
    if (isMoved() && getState() == State.ACTIVE) {
      moveTempest(world, cell, MOVES);
      setMoved(false);
    }
    return super.update(world, cell);
  }
}
