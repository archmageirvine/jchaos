package chaos.engine;

/**
 * Represents a real-valued scalar field.
 *
 * @author Sean A. Irvine
 */
public interface ScalarField {

  /**
   * Return the weight for the specified cell.  By convention the weight should
   * be in the range [-1,1].
   * @param cell the cell to get the weight of
   * @return weight the weight of the cell.
   */
  double weight(int cell);
}
