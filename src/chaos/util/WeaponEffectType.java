package chaos.util;

/**
 * Enumeration of possible weapon effects.
 * @author Sean A. Irvine
 */
public enum WeaponEffectType {
  /** A weapon effect type. */
  NON_EVENT,
  /** A weapon effect type for monster casting. */
  MONSTER_CAST_EVENT,
  /** A weapon effect type for tree casting. */
  TREE_CAST_EVENT,
  /** A weapon effect type for stone casting. */
  STONE_CAST_EVENT,
  /** A weapon effect type for an intelligence attack. */
  BRAIN_BEAM_EVENT,
  /** A weapon effect type. */
  RANGED_COMBAT_EVENT,
  /** A weapon effect type. */
  LIGHTNING,
  /** A weapon effect type. */
  KILL_BEAM,
  /** A weapon effect type. */
  ICE_BEAM,
  /** A weapon effect type. */
  LINE,
  /** Remove a line (i.e. draw it in black. */
  UNLINE,
  /** Plasma beam weapon type. */
  PLASMA,
  /** A weapon effect type. */
  BALL,
  /** Glowing red hot fireball. */
  FIREBALL,
  /** Spinning red fireball. */
  SPINNER,
  /** Thunderbolt effect type. */
  THUNDERBOLT,
  /** A demotion in ability. */
  DEMOTION,
  /** A promotion in ability. */
  PROMOTION
}
