package chaos.common.mythos;

import chaos.common.AbstractListCasterTest;
import chaos.common.Castable;

/**
 * Tests this monster.
 * @author Sean A. Irvine
 */
public class IrisTest extends AbstractListCasterTest {

  @Override
  public Castable getCastable() {
    return new Iris();
  }
}
