package irvine.tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a default implementation of the <code>list()</code> method.
 * @author Sean A. Irvine
 */
public abstract class AbstractTileEffect implements TileEffect {

  @Override
  public List<TileImage> list() {
    final ArrayList<TileImage> list = new ArrayList<>();
    TileImage image;
    while ((image = next()) != null) {
      list.add(image);
    }
    return list;
  }
}
