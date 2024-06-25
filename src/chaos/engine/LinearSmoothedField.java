package chaos.engine;

import chaos.board.World;

/**
 * Field where the weight at a cell is a weight sum of all cells in the
 * specified underlying field.  Weight drops off linearly with distance.
 * @author Sean A. Irvine
 */
public class LinearSmoothedField implements ScalarField {

  private final World mWorld;
  private final ScalarField mField;
  private final int mSource;

  LinearSmoothedField(final ScalarField field, final World world, final int source) {
    mField = field;
    mWorld = world;
    mSource = source;
  }

  @Override
  public double weight(final int cell) {
    double weight = 0;
    for (int c = 0; c < mWorld.size(); ++c) {
      // Ignore contribution of item actually under consideration
      if (c != mSource) {
        weight += mField.weight(c) / (1.0 + mWorld.getSquaredDistance(c, cell));
      }
    }
    return weight / mWorld.size();
  }
}
