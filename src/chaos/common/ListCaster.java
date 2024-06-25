package chaos.common;

import chaos.Chaos;
import chaos.board.Cell;
import chaos.util.Random;

/**
 * A type of Caster which casts down a predefined list of spells.
 * The Caster will cast the items in the list in the order they are given.
 * The list may contain <code>null</code> to indicate a turn where nothing is cast.
 * Once the list is exhausted no further casts are made.
 * @author Sean A. Irvine
 */
public abstract class ListCaster extends Caster {

  /** Castables used by this caster */
  protected final Class<? extends Castable>[] mCastClass;
  private int mNext = 0;

  /**
   * A caster capable of casting multiple spells.
   * @param castables list of castables
   */
  @SuppressWarnings("unchecked")
  public ListCaster(final Class<? extends Castable>... castables) {
    super();
    mCastClass = castables;
  }

  /**
   * Get the next Castable or null if nothing is to be cast.
   * @return Castable to be cast
   */
  @Override
  public Castable getCastable() {
    if (mNext >= mCastClass.length) {
      return null;
    }
    final Class<? extends Castable> spell = mCastClass[mNext++];
    return spell == null ? null : FrequencyTable.instantiate(spell);
  }

  /**
   * Perform any required casting using the standard AI engine.
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
