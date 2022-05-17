package chaos.board;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.Set;

import chaos.common.Actor;
import chaos.common.Attribute;
import chaos.common.Inanimate;
import chaos.common.Monster;
import chaos.common.State;
import chaos.common.monster.CatLord;
import chaos.util.Event;
import chaos.util.EventGenerator;
import chaos.util.EventListener;
import chaos.util.Random;
import irvine.world.CellFilter;
import irvine.world.FlatWorld;

/**
 * Represents a rectangular array of Cells. Provides constant time access
 * to individual cells in the world.<p>
 *
 * @author Sean A. Irvine
 */
public class World extends FlatWorld<Cell> implements EventGenerator {

  private final Team mTeam;
  private final WarpSpace mWarpSpace;
  private final WizardManager mWizardManager;

  /** List of objects listening to events. */
  private transient HashSet<EventListener> mListeners;

  /**
   * Construct a new world of specified width and height.
   *
   * @param width number of cells across the board
   * @param height number of cells down the board
   * @param team the Team object for this world
   * @exception NullPointerException if <code>team</code> is null
   */
  public World(final int width, final int height, final Team team) {
    super(width, height);
    mListeners = new HashSet<>();
    if (team == null) {
      throw new NullPointerException();
    }
    for (int i = 0; i < size(); ++i) {
      setCell(i, new Cell(i));
    }
    mTeam = team;
    mWarpSpace = new WarpSpace();
    mWizardManager = new WizardManager();
  }

  /**
   * Construct a new world of specified width and height and new team information.
   *
   * @param width number of cells across the board
   * @param height number of cells down the board
   * @exception NullPointerException if <code>team</code> is null
   */
  public World(final int width, final int height) {
    this(width, height, new Team());
  }

  private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    mListeners = new HashSet<>();
  }

  /**
   * Get the team information.
   *
   * @return team information
   */
  public Team getTeamInformation() {
    return mTeam;
  }

  /**
   * Get the wizard manager.
   *
   * @return wizard manager
   */
  public WizardManager getWizardManager() {
    return mWizardManager;
  }

  /**
   * Get the cell containing the specified actor.  This is quite
   * a slow operation and should be used sparingly.  It can be
   * used to locate wizards.  If the specified actor is not found
   * then null is returned.  Note the actor will be in the returned
   * cell, but may not be the top object.
   *
   * @param actor what to search for
   * @return the cell containing it
   */
  public Cell getCell(final Actor actor) {
    for (int i = 0; i < size(); ++i) {
      if (getCell(i).contains(actor)) {
        return getCell(i);
      }
    }
    return null;
  }

  /**
   * Get the actor at the specified position. Returns null if there
   * is no actor at the specified position.
   *
   * @param x x-coordinate
   * @param y y-coordinate
   * @return actor at (x,y)
   */
  public Actor actor(final int x, final int y) {
    final Cell c = getCell(x, y);
    return c == null ? null : c.peek();
  }

  /**
   * Get the actor at the specified position. Returns null if there
   * is no actor at the specified position.
   *
   * @param cell cell index
   * @return actor at given index
   */
  public Actor actor(final int cell) {
    final Cell c = getCell(cell);
    return c == null ? null : c.peek();
  }

  /** The highest nominal agility possible. */
  private static final int ENGAGE_ESCAPE = 100;

  /**
   * Test if the specified cell is engaged. Returns true if the cell is
   * engaged and false otherwise. The more enemy creatures surrounding a
   * cell the more likely it is to be engaged.
   *
   * @param cell number of cell to test.
   * @param compulsory force engagement if at all possible
   * @return true if cell is engaged
   */
  public boolean checkEngagement(final int cell, final boolean compulsory) {
    /*
     * Clearly, we weed out such things as empty spaces, creatures
     * owned by the same player, inanimate objects and dead objects.  A
     * probability test is made to determine engagement as appropriate.  A
     * true value is returned for engaged creatures, otherwise zero is
     * returned.  If compulsory is nonzero then engagement will always occur
     * if possible, otherwise engagement is determined using the agility
     * rating.
     */
    final Actor a = actor(cell);
    return a != null && checkEngagement(a, cell, compulsory);
  }

  /**
   * Assuming the specified actor moved into the specified cell would it be engaged.
   * @param actor the actor
   * @param cell the cell
   * @param compulsory true if engagement must occur
   * @return true iff actor would be engaged
   */
  public boolean checkEngagement(final Actor actor, final int cell, final boolean compulsory) {
    if (!(actor instanceof Monster)) {
      return false;
    }
    final int player = actor.getOwner();
    final int playerTeam = mTeam.getTeam(player);
    final int agility = actor.get(Attribute.AGILITY);

    // examine each adjacent cell in turn
    for (final Cell c : getCells(cell, 1, 1, false)) {
      final Actor a = c.peek();
      if (a == null) {
        continue;
      }
      // extra check on movement prevents engagement to Fungi, Shadow Wood etc.
      if (!(a instanceof Monster) || a.get(Attribute.MOVEMENT) == 0) {
        continue;
      }
      // handle case of actors which are both monsters and inanimates
      if (a instanceof Inanimate) {
        continue;
      }
      final State state = a.getState();
      if (state == State.DEAD) {
        continue;
      }
      if (state == State.ASLEEP) {
        continue;
      }
      if (mTeam.getTeam(a) == playerTeam) {
        continue;
      }
      // cell contains engageable Monster
      if (compulsory) {
        return true;
      }
      if (agility <= Random.nextInt(ENGAGE_ESCAPE)) {
        return true;
      }
    }

    return false;
  }


  /**
   * Return a set containing all cells within distance <code>maxRadius</code>
   * and at least <code>minRadius</code> from the specified <code>centre</code>.
   * If <code>los</code> is true, then only those cells for which line-of-sight
   * exists will be included in the set.  If no cells match the requirements
   * then the resulting set will be empty.
   * A typical call would be:
   *
   * <pre>
   * getCells(0, 0, 5, false);
   * </pre><p>
   *
   * to select all cells within distance 5 of cell 0 including cell 0 itself.
   *
   * @param centre cell number for the origin (assumed to be a valid cell)
   * @param minRadius minimum distance
   * @param maxRadius maximum distance
   * @param los true if line-of-sight is required
   * @return set of cells meeting requirements
   * @exception IllegalArgumentException if the minimum radius is negative
   * or the maximum radius is less than the minimum radius.
   */
  public Set<Cell> getCells(final int centre, final int minRadius, final int maxRadius, final boolean los) {
    if (!los) {
      return getCells(centre, minRadius, maxRadius, null);
    }
    final CellFilter<Cell> cf = new CellFilter<Cell>() {
      final LineOfSight mLOS = new LineOfSight(World.this);
      @Override
      public boolean accept(final Cell c) {
        return c != null && mLOS.isLOS(centre, c.getCellNumber());
      }
    };
    return getCells(centre, minRadius, maxRadius, cf);
  }

  /**
   * Convenience method to return all the active cells containing actors
   * belonging to the specified player.  If no cells match then an empty
   * set is returned.
   *
   * @param player player number to get cells for
   * @return set of matching cells
   */
  public Set<Cell> getCells(final int player) {
    final Set<Cell> r = new HashSet<>();
    for (int i = 0; i < size(); ++i) {
      final Actor a = actor(i);
      if (a != null && a.getOwner() == player && a.getState() == State.ACTIVE) {
        r.add(getCell(i));
      }
    }
    return r;
  }

  /**
   * Determine the owner of the first instance of the actor of given type.
   * Returns Actor.OWNER_NONE if there is no such Actor currently active.
   * @param clazz class of object to look for
   * @return owner of <code>clazz</code> or <code>Actor.OWNER_NONE</code>
   */
  public int isAlive(final Class<? extends Actor> clazz) {
    for (int i = 0; i < size(); ++i) {
      final Actor a = actor(i);
      if (clazz.isInstance(a) && a.getState() == State.ACTIVE) {
        return a.getOwner();
      }
    }
    return Actor.OWNER_NONE;
  }

  /**
   * Determine the owner of the Cat Lord assuming the Cat Lord is alive.
   * Returns Actor.OWNER_NONE if there is no Cat Lord currently active.
   * @return owner of Cat Lord or Actor.OWNER_NONE.
   */
  public int isCatLordAlive() {
    return isAlive(CatLord.class);
  }

  /**
   * Register the specified listener with every cell in the world,
   * and the world itself.
   *
   * @param el listener to register
   * @exception NullPointerException if listener is null
   */
  @Override
  public void register(final EventListener el) {
    if (el == null) {
      throw new NullPointerException();
    }
    synchronized (mListeners) {
      mListeners.add(el);
    }
    for (int i = 0; i < size(); ++i) {
      getCell(i).register(el);
    }
  }

  /**
   * Deregister the specified listener from every cell in the world,
   * and the world itself.
   *
   * @param el listener to deregister
   */
  @Override
  public void deregister(final EventListener el) {
    synchronized (mListeners) {
      mListeners.remove(el);
    }
    for (int i = 0; i < size(); ++i) {
      getCell(i).deregister(el);
    }
  }

  @Override
  public void notify(final Event event) {
    synchronized (mListeners) {
      for (final EventListener el : mListeners) {
        el.update(event);
      }
    }
  }

  /**
   * Return the warp space of this world.
   *
   * @return warp space
   */
  public WarpSpace getWarpSpace() {
    return mWarpSpace;
  }
}
