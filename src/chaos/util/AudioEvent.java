package chaos.util;

import chaos.board.Cell;
import chaos.common.Actor;

/**
 * An audible cell effect event.
 * @author Sean A. Irvine
 */
public class AudioEvent extends CellEffectEvent {

  private final String mName;
  private final Actor mCause;

  /**
   * An audio event for the specified cell.
   * @param cell the cell affected
   * @param actor the actor this event is for
   * @param name name of the audio event (used for sound selection)
   * @param cause the actor causing the event
   */
  public AudioEvent(final int cell, final Actor actor, final String name, final Actor cause) {
    super(cell, CellEffectType.AUDIO, actor);
    mName = name;
    mCause = cause;
  }

  /**
   * An audio event for the specified cell.
   * @param cell the cell affected
   * @param actor the actor this event is for
   * @param name name of the audio event (used for sound selection)
   */
  public AudioEvent(final int cell, final Actor actor, final String name) {
    this(cell, actor, name, null);
  }

  /**
   * An audio event for the specified cell.
   * @param cell the cell affected
   * @param actor the actor this event is for
   * @param name name of the audio event (used for sound selection)
   * @param cause the actor causing the event
   */
  public AudioEvent(final Cell cell, final Actor actor, final String name, final Actor cause) {
    this(cell.getCellNumber(), actor, name, cause);
  }

  /**
   * An audio event for the specified cell.
   * @param cell the cell affected
   * @param actor the actor this event is for
   * @param name name of the audio event (used for sound selection)
   */
  public AudioEvent(final Cell cell, final Actor actor, final String name) {
    this(cell, actor, name, null);
  }

  /**
   * A general audio event.
   * @param name name of the audio event (used for sound selection)
   */
  public AudioEvent(final String name) {
    this(0, null, name);
  }

  /**
   * The attribute this event is for.
   * @return the name of the audio effect
   */
  public String getSoundEffect() {
    return mName;
  }

  /**
   * Get the cause of this sound.
   * @return the cause
   */
  public Actor getCause() {
    return mCause;
  }
}
