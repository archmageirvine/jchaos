package irvine.tile;

import java.util.List;

/**
 * An effect which plays another effect in reverse.
 * @author Sean A. Irvine
 */
public class ReverseEffect extends AbstractTileEffect {

  /** The frames of the underlying effect. */
  private final List<TileImage> mFrames;

  /**
   * Construct a reversed effect.  This effect plays the given effect in
   * reverse.  For an effect with many many frames this could suck up a
   * fair amount of memory.
   * @param effect underlying effect
   * @throws NullPointerException if <code>effect</code> is null.
   */
  public ReverseEffect(final TileEffect effect) {
    mFrames = effect.list();
  }

  @Override
  public TileImage next() {
    final int size = mFrames.size() - 1;
    return size >= 0 ? mFrames.remove(size) : null;
  }
}
