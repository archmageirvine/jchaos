package chaos.common;

import chaos.Chaos;
import chaos.board.Cell;
import chaos.util.Random;

/**
 * A type of Caster which periodically casts a particular spell.
 * @author Sean A. Irvine
 */
public abstract class Unicaster extends Caster {

  /** Single Castable used by this Unicaster */
  protected Class<? extends Castable> mCastClass = null;
  /** The expected delay between casts */
  protected int mDelay = 0;

  @Override
  public Castable getCastable() {
    return Random.nextInt(mDelay) == 0 ? FrequencyTable.instantiate(mCastClass) : null;
  }

  @Override
  public void doCasting(final Cell casterCell) {
    final Castable cast = getCastable();
    if (casterCell != null) {
      Chaos.getChaos().getAI().cast(this, cast, casterCell);
    }
  }

}
