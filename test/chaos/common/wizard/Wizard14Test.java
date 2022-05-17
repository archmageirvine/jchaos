package chaos.common.wizard;

import chaos.common.Castable;
import chaos.common.State;

/**
 * Tests this wizard.
 *
 * @author Sean A. Irvine
 */
public class Wizard14Test extends AbstractWizardTest {

  @Override
  public Castable getCastable() {
    final Wizard w = new Wizard14();
    w.setState(State.ACTIVE);
    return w;
  }

}
