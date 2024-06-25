package chaos.common;

import chaos.common.spell.Armour;

/**
 * Tests this spell.
 * @author Sean A. Irvine
 */
public class DummyShieldTest extends AbstractShieldTest {


  @Override
  public Castable getCastable() {
    return new Armour();
  }
}
