package chaos.common.wizard;

import chaos.common.Castable;
import chaos.common.State;

/**
 * Tests this wizard.
 *
 * @author Sean A. Irvine
 */
public class Wizard12Test extends AbstractWizardTest {

  @Override
  public Castable getCastable() {
    final Wizard w = new Wizard12();
    w.setState(State.ACTIVE);
    return w;
  }

}
