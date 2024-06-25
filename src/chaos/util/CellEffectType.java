package chaos.util;

/**
 * Enumeration of cell effect types.
 * @author Sean A. Irvine
 */
public enum CellEffectType {
  /** A cell effect event. */
  NON_EVENT,
  /** A cell effect event. */
  ATTACK_EVENT,
  /** A cell effect event. */
  MOVEMENT_EVENT,
  /** A cell effect event. */
  HIGHLIGHT_EVENT,
  /** A cell effect event. */
  MONSTER_CAST_EVENT,
  /** A cell effect event. */
  CORPSE_EXPLODE,
  /** A cell effect event. */
  TWIRL,
  /** A cell effect event. */
  FADE_TO_RED,
  /** A cell effect event. */
  POWERUP,
  /** A cell effect event. */
  ACQUISITION,
  /** A cell effect event. */
  WHITE_CIRCLE_EXPLODE,
  /** A cell effect event. */
  GREEN_CIRCLE_EXPLODE,
  /** A cell effect event for successful owner change. */
  OWNER_CHANGE,
  /** A cell effect event. */
  SHIELD_GRANTED,
  /** A cell effect event. */
  SHIELD_DESTROYED,
  /** A cell effect event. */
  TEAM_CHANGE,
  /** A cell effect event. */
  SPUNGER,
  /** A cell effect event. */
  DEATH,
  /** A cell effect event. */
  SLEEP,
  /** A cell effect event. */
  WARP_OUT,
  /** A cell effect event. */
  WARP_IN,
  /** A cell effect event. */
  REINCARNATE,
  /** A cell effect event. */
  CHANGE_REALM,
  /** A cell effect event. */
  WIZARD_EXPLODE,
  /** A cell effect event. */
  REDRAW_CELL,
  /** A cell effect event. */
  RAISE_DEAD,
  /** A cell effect event. */
  BONUS,
  /** A cell effect event for a bomb explosion. */
  BOMB,
  /** A cell effect event for an ice bomb explosion. */
  ICE_BOMB,
  /** Collapse of a meditation. */
  MEDITATION_COLLAPSE,
  /** Poisoning. */
  POISON,
  /** Gain of experience. */
  EXPERIENCE,
  /** Shake the screen. */
  EARTHQUAKE,
  /** Arbitrary named audio event. */
  AUDIO,
  /** A cell effect event. */
  SPELL_FAILED,
  /** Orange circle explosion. */
  ORANGE_CIRCLE_EXPLODE,
  /** Explode outwards in a fireball (e.g. volcano). */
  FIREBALL_EXPLODE,
  /** Ring centred on a cell. */
  RING
}
