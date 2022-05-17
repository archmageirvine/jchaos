package chaos.board;

import java.io.Serializable;

/**
 * Remembers details of a move in progress.
 *
 * @author Sean A. Irvine
 */
class AttentionDetail implements Serializable {
  /** Constant indicating engagement status is unknown. */
  static final int UNKNOWN = -1;
  /** -1 not tested, 0 not engaged, 1 engaged */
  int mEngaged = UNKNOWN;
  /** true if engagement test must be compulsory */
  boolean mEngageIsCompulsory = false;
  /** Remaining movement points squared */
  int mSquaredMovementPoints = UNKNOWN;
}

