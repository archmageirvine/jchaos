package chaos.common.wizard;

import chaos.common.Castable;
import chaos.common.State;

/**
 * Tests this wizard.
 * @author Sean A. Irvine
 */
public class Wizard1Test extends AbstractWizardTest {

  @Override
  public Castable getCastable() {
    final Wizard w = new Wizard1();
    w.setState(State.ACTIVE);
    return w;
  }

}
