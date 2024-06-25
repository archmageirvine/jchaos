package irvine.heraldry;

import java.util.Random;

/**
 * Build a shield for display.
 * @author Sean A. Irvine
 */
public final class ShapeFactory {

  private ShapeFactory() {
  }

  /**
   * Return the shield for a given code.
   * @param shieldCode shield code
   * @return shield
   */
  public static Shape createShape(final int shieldCode) {
    final Random r = new Random(shieldCode);
    final Shape s;
    switch (r.nextInt(3)) {
      case 0:
        s = new Shield();
        break;
      case 1:
        s = new Cartouche();
        break;
      default:
        s = new Lozenge();
        break;
    }
    s.setFieldTincture(Tincture.values()[r.nextInt(Tincture.values().length)]);
    s.setOrdinary(Ordinary.values()[r.nextInt(Ordinary.values().length)]);
    s.setOrdinaryTincture(Tincture.values()[r.nextInt(Tincture.values().length)]);
    return s;
  }
}
