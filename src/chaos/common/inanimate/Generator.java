package chaos.common.inanimate;

import chaos.common.AbstractGenerator;
import chaos.common.Actor;
import chaos.common.Bonus;
import chaos.common.Castable;
import chaos.common.FrequencyTable;
import chaos.common.Growth;
import chaos.common.Inanimate;
import chaos.common.Monster;

/**
 * Standard generator.
 * @author Sean A. Irvine
 */
public class Generator extends AbstractGenerator implements Bonus {
  @Override
  public int getBonus() {
    return 10;
  }

  @Override
  public Actor chooseWhatToGenerate() {
    Castable c;
    while (!((c = FrequencyTable.instantiate(FrequencyTable.DEFAULT.getBonusRandom())) instanceof Monster) || c instanceof Growth || c instanceof Inanimate) {
      // do nothing
    }
    return (Actor) c;
  }
}
