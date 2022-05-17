package chaos.engine;

/**
 * Weighted combination of two fields.
 *
 * @author Sean A. Irvine
 */
public class CombinedField implements ScalarField {

  private final ScalarField mFieldA;
  private final ScalarField mFieldB;
  private final double mWeight;

  CombinedField(final ScalarField fieldA, final ScalarField fieldB, final double weightA) {
    mFieldA = fieldA;
    mFieldB = fieldB;
    mWeight = weightA;
  }

  @Override
  public double weight(final int cell) {
    return mFieldA.weight(cell) * mWeight + mFieldB.weight(cell) * (1 - mWeight);
  }
}
