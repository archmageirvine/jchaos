package irvine.tile;

import java.util.List;

/**
 * Interface defining a simple iterator to get dynamically generated images
 * of fixed size.  Implementations may limit the size of images produced
 * in an arbitrary manner, but each image in a given effect should be of
 * the same size.
 * @author Sean A. Irvine
 */
public interface TileEffect {

  /**
   * Return the next image in the sequence.  If no further images are
   * available then null is returned.
   * @return next image or null
   */
  TileImage next();

  /**
   * Convenience method to return all the remaining images of a sequence
   * as a list.  If <code>next()</code> has not been called, this is all
   * the images of the effect.
   * @return list of images
   */
  List<TileImage> list();
}
