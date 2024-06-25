package chaos.common;

import java.io.Serializable;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import chaos.board.Cell;
import chaos.board.World;
import chaos.util.NameUtils;

/**
 * This class encapsulates the idea of a castable and is the root
 * object for all spells in the game. All creatures, inanimates,
 * growth, magic spells, empty spaces, corpses, etc. representable
 * either as tiles in the world or as entities on a spell list are
 * instances of this class.<p>
 *
 * It provides the fundamental method <code>cast</code> which is
 * called when a player casts this object at the indicated cell.
 * Other methods provide access to information common to all
 * castables such as the name of the castable, its description,
 * and a hook for its spell table image.
 * @author Sean A. Irvine
 */
public abstract class Castable implements Serializable {

  /** Can be cast on empty cells. */
  public static final int CAST_EMPTY = 1;
  /** Can be cast on dead cells. */
  public static final int CAST_DEAD = 2;
  /** Requires line of sight to cast. */
  public static final int CAST_LOS = 4;
  /** Can be cast on any cell. */
  public static final int CAST_ANY = 8;
  /** Can be cast on the living. */
  public static final int CAST_LIVING = 32;
  /** Do not cast on exposed wizards. */
  public static final int CAST_NOEXPOSEDWIZARD = 64;
  /** Can be cast on the inanimate. */
  public static final int CAST_INANIMATE = 128;
  /** Can be cast on growths. */
  public static final int CAST_GROWTH = 256;
  /** Don't cast on the sleeping. */
  public static final int CAST_NOASLEEP = 512;
  /** Don't cast on cells containing wizards. */
  public static final int CAST_NOWIZARDCELL = 1024;
  /** Avoid casting this spell with double or triple. */
  public static final int CAST_SINGLE = 2048;
  /** Don't cast on independents. */
  public static final int CAST_NOINDEPENDENTS = 4096;
  /** Can only cast on undeads. */
  public static final int CAST_MUSTBEUNDEAD = 8192;

  /** Maximum range for castables. */
  public static final int MAX_CAST_RANGE = 100;

  /** The localized name for this Castable. */
  protected final String mName;
  /** The localized description for this Castable. */
  protected final String mDesc;

  {
    final String shortName = NameUtils.getShortName(this);
    mName = shortName + "Name";
    mDesc = shortName + "Desc";
  }

  /** A resource file for names and descriptions */
  private static final ResourceBundle CAST_TEXT_RESOURCE;

  static {
    try {
      CAST_TEXT_RESOURCE = ResourceBundle.getBundle("chaos/resources/CastResource");
    } catch (final MissingResourceException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Apply this Castable to the specified cell. In the case of a
   * creature this may cause a new instance of the creature to
   * appear in the specified cell. In the case of a magic spell
   * its effect will be actioned on the current contents of the
   * cell. The <code>cell</code> may be <code>null</code> in some
   * instances (e.g. spells affecting every cell).
   * @param world the world where this cast is going to occur
   * @param caster the thing doing the casting
   * @param cell the cell where this Castable is to be invoked
   * @param casterCell the cell containing the caster
   */
  public abstract void cast(final World world, final Caster caster, final Cell cell, final Cell casterCell);

  /**
   * Return the maximum distance this Castable can be cast.
   * @return casting range
   */
  public abstract int getCastRange();

  /**
   * Return the casting flags.
   * @return casting flags
   */
  public abstract int getCastFlags();

  /**
   * Attempt to get the named resource. If the resource is not
   * available then an empty string is returned.
   * @param name resource to retrieve
   * @return value of named resource
   */
  private static String getNamedResource(final String name) {
    try {
      return CAST_TEXT_RESOURCE.getString(name);
    } catch (final MissingResourceException e) {
      return "";
    }
  }

  /**
   * Return a (short) string representing the name of this Castable
   * in a form appropriate for the default locale.
   * @return the spell name
   */
  public String getName() {
    return getNamedResource(mName);
  }

  /**
   * Return a string describing this Castable
   * in a form appropriate for the default locale.
   * @return the spell description
   */
  public String getDescription() {
    return getNamedResource(mDesc);
  }

}
