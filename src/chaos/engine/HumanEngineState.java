package chaos.engine;

enum HumanEngineState {
  /** Waiting for player to select a creature. */
  AWAITING_SELECT,
  /** Move in progress. */
  MOVE_IN_PROGRESS,
  /** Waiting for click during casting. */
  CAST_CLICK,
  /** Input is just idling, waiting for another thread. */
  IDLE,
  /** Waiting for click during shooting. */
  SHOOTING,
  /** Waiting for click during mounted shooting. */
  MOUNTED_SHOOTING,
  /** Is attempting a dismount. */
  DISMOUNTING
}
