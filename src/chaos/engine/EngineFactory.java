package chaos.engine;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import chaos.Chaos;
import chaos.board.CastMaster;
import chaos.board.MoveMaster;
import chaos.board.World;

/**
 * Construct player engines using reflection.
 *
 * @author Sean A. Irvine
 */
public final class EngineFactory {

  private EngineFactory() { }

  /**
   * Construct a new engine.  Since different engines require different parameters,
   * this method looks at the constructors to choose an appropriate one.
   *
   * @param className a <code>String</code> value
   * @param world a <code>World</code> value
   * @param moveMaster a <code>MoveMaster</code> value
   * @param castMaster a <code>CastMaster</code> value
   * @param widthBits an <code>int</code> value
   * @return a <code>PlayerEngine</code> value
   */
  public static PlayerEngine createEngine(final String className, final World world, final MoveMaster moveMaster, final CastMaster castMaster, final int widthBits) {
    try {
      final Class<? extends PlayerEngine> clazz = Class.forName(className).asSubclass(PlayerEngine.class);
      try {
        final Constructor<? extends PlayerEngine> c1 = clazz.getConstructor(World.class, MoveMaster.class, CastMaster.class);
        return c1.newInstance(world, moveMaster, castMaster);
      } catch (final NoSuchMethodException e) {
        // try next one
      }
      try {
        final Constructor<? extends PlayerEngine> c2 = clazz.getConstructor(Chaos.class, int.class);
        return c2.newInstance(Chaos.getChaos(), widthBits);
      } catch (final NoSuchMethodException e) {
        // try next one
      }
      // fall back to zero arg constructor
      return clazz.getDeclaredConstructor().newInstance();
    } catch (final ClassNotFoundException | InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }
}
