package chaos.engine;

import chaos.board.Cell;
import chaos.common.Castable;
import chaos.common.Caster;
import chaos.common.wizard.Wizard;

/**
 * Methods a player engine must support.
 *
 * @author Sean A. Irvine
 */
public interface PlayerEngine {

  /**
   * Specified caster is casting.
   *
   * @param caster the caster
   * @param castable what is to be cast
   * @param cell the cell containing the caster
   * @return true if the cast was attempted, false for abort
   */
  boolean cast(Caster caster, Castable castable, Cell cell);

  /**
   * Called when the game engine decides it is the indicated
   * player's turn to move.
   *
   * @param player turn to move
   */
  void moveAll(Wizard player);

}
