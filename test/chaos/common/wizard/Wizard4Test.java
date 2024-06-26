package chaos.common.wizard;

import chaos.common.Castable;
import chaos.common.State;

/**
 * Tests this wizard.
 * @author Sean A. Irvine
 */
public class Wizard4Test extends AbstractWizardTest {

  @Override
  public Castable getCastable() {
    final Wizard w = new Wizard4();
    w.setState(State.ACTIVE);
    return w;
  }

}
