package chaos.common;

import java.awt.Color;
import java.io.PrintWriter;
import java.io.StringWriter;

import chaos.util.ChaosProperties;

/**
 * Enumeration of actor attributes.
 * @author Sean A. Irvine
 */
public enum Attribute {
  /** Life recovery. */
  LIFE_RECOVERY(new Color(0x7F0000), null, -100, 100),
  /** Life. */
  LIFE(Color.RED, LIFE_RECOVERY, -100, 100),
  /** Movement recovery. */
  MOVEMENT_RECOVERY(new Color(0x00007F), null, -100, 100),
  /** Movement. */
  MOVEMENT(Color.BLUE, MOVEMENT_RECOVERY, 0, 15),
  /** Intelligence recovery. */
  INTELLIGENCE_RECOVERY(new Color(0x7C6049), null, -100, 100),
  /** Intelligence. */
  INTELLIGENCE(new Color(0xECC089), INTELLIGENCE_RECOVERY, 0, 100),
  /** Agility recovery. */
  AGILITY_RECOVERY(new Color(0x007F7F), null, -100, 100),
  /** Agility. */
  AGILITY(Color.CYAN, AGILITY_RECOVERY, 0, 100),
  /** Magical resistance recovery. */
  MAGICAL_RESISTANCE_RECOVERY(new Color(0x7F7F00), null, -100, 100),
  /** Magical resistance. */
  MAGICAL_RESISTANCE(Color.YELLOW, MAGICAL_RESISTANCE_RECOVERY, 0, 100),
  /** Combat recovery. */
  COMBAT_RECOVERY(new Color(0x003F00), null, -100, 100),
  /** Combat. */
  COMBAT(new Color(0x007F00), COMBAT_RECOVERY, -CombatLimit.MAX_COMBAT, CombatLimit.MAX_COMBAT),
  /** Range recovery. */
  RANGE_RECOVERY(new Color(0x007F00), null, -100, 100),
  /** Range. */
  RANGE(Color.GREEN, RANGE_RECOVERY, 0, 100),
  /** Ranged combat recovery. */
  RANGED_COMBAT_RECOVERY(new Color(0x007F00), null, -100, 100),
  /** Ranged combat. */
  RANGED_COMBAT(Color.GREEN, RANGED_COMBAT_RECOVERY, -CombatLimit.MAX_COMBAT, CombatLimit.MAX_COMBAT),
  /** Special combat recovery. */
  SPECIAL_COMBAT_RECOVERY(new Color(0x7F007F), null, -100, 100),
  /** Special combat. */
  SPECIAL_COMBAT(Color.MAGENTA, SPECIAL_COMBAT_RECOVERY, -CombatLimit.MAX_COMBAT, CombatLimit.MAX_COMBAT),
  /** Maximum number of ranged weapon shots per turn. */
  SHOTS(Color.GREEN, null, 0, 3);

  // This contortion needed because can't reference a simple static constant during enum init
  private static final class CombatLimit {
    private CombatLimit() {
    }

    private static final int MAX_COMBAT;

    static {
      final StringWriter sw = new StringWriter();
      new Throwable().printStackTrace(new PrintWriter(sw));
      if (sw.toString().contains("junit")) {
        MAX_COMBAT = 100;
      } else {
        MAX_COMBAT = ChaosProperties.properties().getBooleanProperty(ChaosProperties.COMBAT_CAP_PROPERTY, false) ? 15 : 100;
      }
    }
  }

  private final Color mColor;
  private final Attribute mRecovery;
  private final int mMin;
  private final int mMax;

  Attribute(final Color color, final Attribute recovery, final int min, final int max) {
    mColor = color;
    mRecovery = recovery;
    mMin = min;
    mMax = max;
  }

  /**
   * Return the color associated with this attribute.  If the attribute has no defined color
   * then null is returned.
   * @return attribute color
   */
  public Color getColor() {
    return mColor;
  }

  /**
   * Return the recovery attribute for this attribute.  If this attribute does not have
   * a recovery attribute, then null is returned.
   * @return recovery attribute
   */
  public Attribute recovery() {
    return mRecovery;
  }

  /**
   * The maximum value that can be taken on by this attribute.
   * @return maximum value
   */
  public int max() {
    return mMax;
  }

  /**
   * The minimum value that can be taken on by this attribute.
   * @return minimum value
   */
  public int min() {
    return mMin;
  }
}
