package chaos.common;

import java.awt.Color;
import java.util.Locale;

/**
 * The realms in Chaos.
 * @author Sean A. Irvine
 */
public enum Realm {
  /** Used to indicate actors that do not have a specific realm. */
  NONE(null),
  /** Ordinary material creatures, growths, inanimates, wizards, etc. */
  MATERIAL(Color.GREEN),
  /** Undeads. */
  ETHERIC(Color.MAGENTA),
  /** Dragons. */
  DRACONIC(Color.BLUE),
  /** Demons and devils. */
  DEMONIC(Color.RED),
  /** Creatures from fictional universes. */
  MYTHOS(Color.YELLOW),
  /** Deities. */
  HYPERASTRAL(Color.WHITE),
  /** Hyadic. */
  HYADIC(Color.GRAY),
  /** Subhyadic. */
  SUBHYADIC(Color.PINK),
  /** Objects that can attack anything and be attacked by anything. */
  CORE(Color.CYAN);

  // others will be added as needed

  /** Color associated with this realm. */
  private final Color mColor;

  /**
   * Construct the realm with the specified color.
   * @param color color
   */
  Realm(final Color color) {
    mColor = color;
  }

  /**
   * Return the color associated with this realm.  If the realm has no defined color
   * then null is returned.
   * @return realm color
   */
  public Color getColor() {
    return mColor;
  }

  @Override
  public String toString() {
    switch (this) {
      case ETHERIC:
        return "undead";
      case DRACONIC:
        return "dragon";
      case DEMONIC:
        return "demon";
      default:
        return super.toString().toLowerCase(Locale.getDefault());
    }
  }

  // @formatter:off
  /** Array specifying possible attacks. */
  private static final boolean[][] REALM_TABLE = {
    new boolean[] {false, false, false, false, false, false, false, false, false, false}, // NONE
    new boolean[] {false, true,  false, true,  false, true,  false, false, false, true},  // MATERIAL
    new boolean[] {false, true,  true,  false, false, false, false, false, false, true},  // ETHERIC
    new boolean[] {false, true,  false, true,  false, false, false, false, false, true},  // DRACONIC
    new boolean[] {false, true,  true,  false, true,  false, false, true,  false, true},  // DEMONIC
    new boolean[] {false, true,  false, false, true,  true,  true,  false, false, true},  // MYTHOS
    new boolean[] {false, false, false, false, true,  false, true,  false, true,  true},  // HYPERASTRAL
    new boolean[] {false, false, false, true,  false, false, false, true,  false, true},  // HYADIC
    new boolean[] {false, true,  false, true,  true,  true,  false, false, true , true},  // SUBHYADIC
    new boolean[] {false, true,  true,  true,  true,  true,  true,  true,  true,  true},  // CORE
  };
  // @formatter:on

  /**
   * Test if creature from realm <code>source</code> can legally attack
   * a creature from realm <code>target</code>.
   * @param source source realm
   * @param target target realm
   * @return true if attack is possible
   * @throws NullPointerException if either argument is null.
   */
  public static boolean realmCheck(final Realm source, final Realm target) {
    return REALM_TABLE[source.ordinal()][target.ordinal()];
  }

  /**
   * Test if source can attack the target according to realm constraints.
   * @param source source actor
   * @param target target actor
   * @return true if attack is possible
   * @throws NullPointerException if either argument is null.
   */
  public static boolean realmCheck(final Actor source, final Actor target) {
    return source.is(PowerUps.ATTACK_ANY_REALM) || realmCheck(source.getRealm(), target.getRealm());
  }

  /**
   * Dump the realm attack table as LaTeX.  Used by the documentation.
   * @param args ignored
   */
  public static void main(final String[] args) {
    System.out.println("\\begin{center}");
    System.out.print("\\begin{tabular}{l|");
    final Realm[] v = values();
    for (int k = 1; k < v.length; ++k) {
      System.out.print('c');
    }
    System.out.println('}');
    for (int k = 1; k < v.length; ++k) {
      System.out.print("&" + v[k]);
    }
    System.out.println("\\\\\\hline");
    for (int k = 1; k < v.length; ++k) {
      System.out.print(v[k].toString());
      for (int j = 1; j < v.length; ++j) {
        System.out.print("&$\\" + (realmCheck(v[k], v[j]) ? "bullet" : "circ") + '$');
      }
      if (k != v.length - 1) {
        System.out.println("\\\\");
      }
    }
    System.out.println();
    System.out.println("\\end{tabular}");
    System.out.println("\\end{center}");
  }
}
