package chaos.util;

import chaos.board.Cell;
import chaos.common.Attribute;
import chaos.common.Castable;

/**
 * A cell effect event.
 *
 * @author Sean A. Irvine
 */
public class WeaponEffectEvent implements Event {

  /** The type of cell event */
  private final WeaponEffectType mEvent;
  /** The source cell. */
  private final int mSource;
  /** The larget cell. */
  private final int mTarget;
  /** A castable representing the instigation for the event. */
  private final Castable mCause;
  /** Attribute affected. */
  private final Attribute mAttr;

  /**
   * Construct a new weapon effect event.
   *
   * @param source source cell number
   * @param target target cell number
   * @param eventType the type of cell event
   * @param cause the instigation for the effect (may be null)
   * @param attr attribute this weapon affects
   */
  public WeaponEffectEvent(final int source, final int target, final WeaponEffectType eventType, final Castable cause, final Attribute attr) {
    mSource = source;
    mTarget = target;
    mEvent = eventType == null ? WeaponEffectType.NON_EVENT : eventType;
    mCause = cause;
    mAttr = attr;
  }

  /**
   * Construct a new weapon effect event.
   *
   * @param source source cell number
   * @param target target cell number
   * @param eventType the type of cell event
   * @param cause the instigation for the effect (may be null)
   */
  public WeaponEffectEvent(final int source, final int target, final WeaponEffectType eventType, final Castable cause) {
    this(source, target, eventType, cause, null);
  }

  /**
   * Construct a new weapon effect event.
   *
   * @param source source cell number
   * @param target target cell number
   * @param eventType the type of cell event
   */
  public WeaponEffectEvent(final int source, final int target, final WeaponEffectType eventType) {
    this(source, target, eventType, null);
  }

  /**
   * Construct a new weapon effect event.
   *
   * @param source source cell
   * @param target target cell number
   * @param eventType the type of cell event
   * @param cause the instigation for the effect (may be null)
   * @exception NullPointerException if <code>source</code> or <code>target</code> is null
   */
  public WeaponEffectEvent(final Cell source, final Cell target, final WeaponEffectType eventType, final Castable cause) {
    this(source.getCellNumber(), target.getCellNumber(), eventType, cause);
  }

  /**
   * Construct a new weapon effect event.
   *
   * @param source source cell
   * @param target target cell number
   * @param eventType the type of cell event
   * @param cause the instigation for the effect (may be null)
   * @param attr attribute this weapon affects
   * @exception NullPointerException if <code>source</code> or <code>target</code> is null
   */
  public WeaponEffectEvent(final Cell source, final Cell target, final WeaponEffectType eventType, final Castable cause, final Attribute attr) {
    this(source.getCellNumber(), target.getCellNumber(), eventType, cause, attr);
  }

  /**
   * Construct a new weapon effect event.
   *
   * @param source source cell
   * @param target target cell number
   * @param eventType the type of cell event
   * @exception NullPointerException if <code>source</code> or <code>target</code> is null
   */
  public WeaponEffectEvent(final Cell source, final Cell target, final WeaponEffectType eventType) {
    this(source.getCellNumber(), target.getCellNumber(), eventType, null, null);
  }

  /**
   * Return the type of this cell event.
   *
   * @return cell event type
   */
  public WeaponEffectType getEventType() {
    return mEvent;
  }

  /**
   * The source cell.
   *
   * @return cell number
   */
  public int getSource() {
    return mSource;
  }

  /**
   * The target cell.
   *
   * @return cell number
   */
  public int getTarget() {
    return mTarget;
  }

  /**
   * Get the cause of this event, if any.
   *
   * @return the cause or null
   */
  public Castable getCause() {
    return mCause;
  }

  /**
   * The attribute this event is for. May be null.
   *
   * @return the attribute
   */
  public Attribute getAttribute() {
    return mAttr;
  }
}
