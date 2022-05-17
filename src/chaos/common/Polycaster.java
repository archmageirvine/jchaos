package chaos.common;

import chaos.Chaos;
import chaos.board.Cell;
import chaos.util.Random;

/**
 * A type of Caster which periodically casts any of a set list of
 * spells.
 *
 * @author Sean A. Irvine
 */
public abstract class Polycaster extends Caster {

  /** Single Castable used by this Polycaster */
  protected final Class<? extends Castable>[] mCastClass;
  /** The expected delay between casts */
  protected final int mDelay;

  /**
   * A caster capable of casting multiple spells.
   *
   * @param delay delay between casts
   * @param castables list of castables
   */
  @SuppressWarnings("unchecked")
  public Polycaster(final int delay, final Class<? extends Castable>... castables) {
    super();
    mDelay = delay;
    mCastClass = castables;
  }

  /**
   * Get the next Castable or null if nothing is to be cast.
   *
   * @return Castable to be cast
   */
  @Override
  public Castable getCastable() {
    return Random.nextInt(mDelay) == 0 ? FrequencyTable.instantiate(mCastClass[Random.nextInt(mCastClass.length)]) : null;
  }

  /**
   * Perform any required casting using the standard AI engine.
   *
   * @param casterCell cell containing the caster, can be null
   */
  @Override
  public void doCasting(final Cell casterCell) {
    final Castable cast = getCastable();
    if (casterCell != null) {
      Chaos.getChaos().getAI().cast(this, cast, casterCell);
    }
  }

}
