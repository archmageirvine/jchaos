package chaos.common.free;

import java.util.HashSet;

import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Actor;
import chaos.common.Caster;
import chaos.common.FreeCastable;
import chaos.common.FrequencyTable;
import chaos.common.Monster;
import chaos.common.State;
import chaos.util.CellEffectType;
import chaos.util.PolycellEffectEvent;

/**
 * Horde.
 * @author Sean A. Irvine
 */
public class Horde extends FreeCastable {

  private final Monster mSpawn;

  /**
   * Construct a new horde of specified type
   * @param spawn monster type
   */
  public Horde(final Monster spawn) {
    mSpawn = spawn;
  }

  /** Construct a new horde of random type. */
  public Horde() {
    this(FrequencyTable.DEFAULT.getRandomMonster());
  }

  /**
   * Get the type of monster produced by this spawner.
   * @return type of monster
   */
  public Monster getSpawn() {
    return mSpawn;
  }

  @Override
  public int getCastFlags() {
    return CAST_SINGLE;
  }

  @Override
  public void cast(final World world, final Caster caster, final Cell casterCell) {
    if (world != null && casterCell != null) {
      final HashSet<Cell> affected = new HashSet<>();
      for (final Cell c : world.getCells(casterCell.getCellNumber(), 1, 3, false)) {
        if (c.peek() == null || c.peek().getState() == State.DEAD) {
          affected.add(c);
        }
      }
      world.notify(new PolycellEffectEvent(affected, CellEffectType.MONSTER_CAST_EVENT, getSpawn())); // cause to get realm in graphics
      for (final Cell c : affected) {
        final Actor a = (Actor) FrequencyTable.instantiate(getSpawn().getClass());
        a.setOwner(caster.getOwner());
        c.push(a);
      }
      world.notify(new PolycellEffectEvent(affected, CellEffectType.REDRAW_CELL, caster));
    }
  }
}
