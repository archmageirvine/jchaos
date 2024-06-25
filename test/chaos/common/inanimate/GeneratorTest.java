package chaos.common.inanimate;

import chaos.common.AbstractGeneratorTest;
import chaos.common.Castable;
import chaos.common.Growth;
import chaos.common.Inanimate;
import chaos.common.Monster;
import chaos.common.wizard.Wizard;

/**
 * Tests this inanimate.
 * @author Sean A. Irvine
 */
public class GeneratorTest extends AbstractGeneratorTest {


  @Override
  public Castable getCastable() {
    return new Generator();
  }

  public void testWhatItProduces() {
    final Generator a = new Generator();
    for (int i = 0; i < 1000; ++i) {
      final Castable c = a.chooseWhatToGenerate();
      assertTrue(c instanceof Monster);
      assertFalse(c instanceof Growth);
      assertFalse(c instanceof Wizard);
      assertFalse(c instanceof Inanimate);
    }
  }
}
