package chaos.selector;

import chaos.board.CastMaster;
import chaos.board.Cell;
import chaos.board.World;
import chaos.common.Castable;
import chaos.common.Monster;
import chaos.common.wizard.Wizard;

/**
 * Prefers creatures to anything else.
 * @author Sean A. Irvine
 */
public class Creaturologist extends Strategiser {

  /**
   * Construct a selector for the specified wizard and world.
   * @param wizard the wizard
   * @param world the world
   * @param castMaster casting rules
   */
  public Creaturologist(final Wizard wizard, final World world, final CastMaster castMaster) {
    super(wizard, world, castMaster);
  }

  @Override
  protected int getScore(final Castable c, final int[] s, final Cell casterCell) {
    if (c instanceof Monster && s[EMPTY] != 0) {
      return 1000;
    }
    return super.getScore(c, s, casterCell);
  }
}
